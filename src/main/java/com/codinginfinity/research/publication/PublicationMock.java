package com.codinginfinity.research.publication;

import com.codinginfinity.research.publication.exception.AlreadyPublishedException;
import com.codinginfinity.research.publication.exception.PublicationDoesntExist;
import com.codinginfinity.research.publication.exception.PublicationTypeExistsException;
import com.codinginfinity.research.publication.exception.PublicationWithTitleExistsForAuthorsException;
import com.codinginfinity.research.publication.lifecycle.InProgress;
import com.codinginfinity.research.publication.lifecycle.Published;
import com.codinginfinity.research.publication.request.*;
import com.codinginfinity.research.publication.response.*;
import com.codinginfinity.research.publication.type.Active;
import com.codinginfinity.research.publication.type.PublicationType;
import com.codinginfinity.research.publication.type.exception.PublicationTypeDoesntExist;
import com.codinginfinity.research.services.RequestNotValidException;
import com.codinginfinity.research.services.mocking.BaseMock;
import com.codinginfinity.research.services.mocking.Mock;
import com.codinginfinity.research.validation.beanvalidation.services.ServiceValidationUtilities;
import org.springframework.stereotype.Service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Month;

/**
 * Created by andrew on 2016/04/13.
 */
@Stateless
@Service
public class PublicationMock extends BaseMock implements IPublication {

    @Inject
    private ServiceValidationUtilities serviceValidationUtilities;

    public enum State implements Mock.State{externalRequirementsMet,
        notAuthorized,
        publicationTypeExists,
        publicationTypeDoesntExist,
        publicationWithTitleExistsForAuthor,
        publicationDoesntExist }

    @Override
    public AddPublicationResponse addPublication(AddPublicationRequest addPublicationRequest) throws RequestNotValidException, PublicationWithTitleExistsForAuthorsException {

        serviceValidationUtilities.validateRequest(AddPublicationRequest.class, addPublicationRequest);

        if (addPublicationRequest.getInitialState().getPublicationDetails().getTitle().equals("A Guided Approach to Agile Software Engineering") &&
                addPublicationRequest.getInitialState().getOwner() == 3)
            throw new PublicationWithTitleExistsForAuthorsException();

        CreatePublicationResponse response = createPublication(new CreatePublicationRequest(addPublicationRequest.getInitialState()));

        return new AddPublicationResponse(response.getPublication());
    }

    @Override
    public GetPublicationResponse getPublication(GetPublicationRequest getPublicationRequest) throws RequestNotValidException, PublicationDoesntExist {

        serviceValidationUtilities.validateRequest(GetPublicationRequest.class, getPublicationRequest);

        if (getPublicationRequest.getPublication() < 0)
            throw new RequestNotValidException();

        if (getPublicationRequest.getPublication() != 55)
            throw new PublicationDoesntExist();

        return new GetPublicationResponse(createNormalPublication());
    }

    @Override
    public CreatePublicationResponse createPublication(CreatePublicationRequest createPublicationRequest) throws RequestNotValidException {

        serviceValidationUtilities.validateRequest(CreatePublicationRequest.class, createPublicationRequest);

        PublicationDetails publicationDetailsRequest = createPublicationRequest.getInitialState().getPublicationDetails();
        PublicationDetails publicationDetails = new PublicationDetails(publicationDetailsRequest.getTitle(), publicationDetailsRequest.getEnvisagedPublicationDate());
        publicationDetails.setId(55);

        PublicationState publicationStateRequest = createPublicationRequest.getInitialState();
        PublicationState publicationState = new PublicationState(publicationStateRequest.getDate(),
                publicationStateRequest.getReason(),
                publicationDetails,
                createPublicationRequest.getInitialState().getLifeCycleState(),
                createPublicationRequest.getInitialState().getPublicationTarget(),
                createPublicationRequest.getInitialState().getPublicationType(),
                createPublicationRequest.getInitialState().getOwner()
        );
        Publication publication = new Publication(publicationState);

        return new CreatePublicationResponse(publication);
    }

    @Override
    public ChangePublicationStateResponse changePublicationState(ChangePublicationStateRequest changePublicationStateRequest) throws RequestNotValidException, PublicationDoesntExist, AlreadyPublishedException {

        serviceValidationUtilities.validateRequest(ChangePublicationStateRequest.class, changePublicationStateRequest);
        if (changePublicationStateRequest.getModifiedPublication() < 0)
            throw new RequestNotValidException();

        if (changePublicationStateRequest.getModifiedPublication() != 55)
            throw new PublicationDoesntExist();

        if(changePublicationStateRequest.getPublicationToModify().getLifeCycleState() instanceof Published)
            throw new AlreadyPublishedException();

        Publication publication = createNormalPublication();
        publication.addStateEntry(changePublicationStateRequest.getPublicationToModify());
        return new ChangePublicationStateResponse(changePublicationStateRequest.getModifiedPublication(), publication);
    }

    @Override
    public AddPublicationTypeResponse addPublicationType(AddPublicationTypeRequest addPublicationTypeRequest) throws RequestNotValidException, PublicationTypeExistsException {

        serviceValidationUtilities.validateRequest(AddPublicationTypeRequest.class, addPublicationTypeRequest);

        if (addPublicationTypeRequest.getNewPublicationType().getName().toLowerCase().equals("e-journal"))
            throw new PublicationTypeExistsException();

        Active active = new Active(((Active)(addPublicationTypeRequest.getStateEntry())).getAccreditationPoints());
        active.setId(45);

        PublicationType publicationType = new PublicationType(addPublicationTypeRequest.getNewPublicationType().getName(), active);
        publicationType.setId(89);

        return new AddPublicationTypeResponse(publicationType);
    }

    @Override
    public GetPublicationTypeResponse getPublicationType(GetPublicationTypeRequest getPublicationTypeRequest) throws RequestNotValidException, PublicationTypeDoesntExist {

        serviceValidationUtilities.validateRequest(GetPublicationTypeRequest.class, getPublicationTypeRequest);

        if (getPublicationTypeRequest.getPublicationType() < 0)
            throw new RequestNotValidException();

        if (getPublicationTypeRequest.getPublicationType() != 89)
            throw new PublicationTypeDoesntExist();

        Active active = new Active(5.5);
        active.setId(45);

        PublicationType publicationType = new PublicationType("e-journal", active);
        publicationType.setId(89);

        return new GetPublicationTypeResponse(publicationType);
    }

    @Override
    public ModifyPublicationTypeResponse modifyPublicationType(ModifyPublicationTypeRequest modifyPublicationTypeRequest) throws RequestNotValidException {
        serviceValidationUtilities.validateRequest(ModifyPublicationTypeRequest.class, modifyPublicationTypeRequest);

        return null;
    }

    @Override
    public GetPublicationsForPersonResponse getPublicationsForPerson(GetPublicationsForPersonRequest getPublicationsForPersonRequest) throws RequestNotValidException {

        serviceValidationUtilities.validateRequest(GetPublicationsForPersonRequest.class, getPublicationsForPersonRequest);
        return null;
    }

    @Override
    public GetPublicationsForGroupResponse getPublicationsForGroup(GetPublicationsForGroupRequest getPublicationsForGroupRequest) throws RequestNotValidException {

        serviceValidationUtilities.validateRequest(GetPublicationsForGroupRequest.class, getPublicationsForGroupRequest);
        return null;
    }

    @Override
    public CalcAccreditationPointsForPersonResponse calcAccreditationPointsForPerson(CalcAccreditationPointsForPersonRequest calcAccreditationPointsForPersonRequest) throws RequestNotValidException {

        serviceValidationUtilities.validateRequest(CalcAccreditationPointsForPersonRequest.class, calcAccreditationPointsForPersonRequest);
        return null;
    }

    @Override
    public CalcAccreditationPointsForGroupResponse calcAccreditationPointsForGroup(CalcAccreditationPointsForGroupRequest calcAccreditationPointsForGroupRequest) throws RequestNotValidException {

        serviceValidationUtilities.validateRequest(CalcAccreditationPointsForGroupRequest.class, calcAccreditationPointsForGroupRequest);
        return null;
    }

    private static Publication createNormalPublication() {

        Publication publication = new Publication(createNewNormalPublicationState());
        publication.setId(174);

        return publication;
    }

    private static PublicationState createNewNormalPublicationState() {

        PublicationDetails publicationDetails = new PublicationDetails("Agile Software Engineering", LocalDate.of(2016, Month.APRIL, 13));
        publicationDetails.setId(55);

        return new PublicationState(LocalDate.of(2017, Month.APRIL, 1), "Have a new idea", publicationDetails, new InProgress(0), 4, 2, 3);
    }


}
