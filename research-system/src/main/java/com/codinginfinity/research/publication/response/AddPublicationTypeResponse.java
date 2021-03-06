package com.codinginfinity.research.publication.response;

import com.codinginfinity.research.publication.type.PublicationType;
import com.codinginfinity.research.services.Response;

/**
 * Created by andrew on 2016/04/11.
 */
public class AddPublicationTypeResponse implements Response {

    private static final long serialVersionUID = -996260364046174560L;

    private long newPublicationType;

    public AddPublicationTypeResponse(long newPublicationType) {
        this.newPublicationType = newPublicationType;
    }

    public AddPublicationTypeResponse(PublicationType publicationType) {
    }

    public long getNewPublicationType() {
        return newPublicationType;
    }

    public void setNewPublicationType(long newPublicationType) {
        this.newPublicationType = newPublicationType;
    }
}
