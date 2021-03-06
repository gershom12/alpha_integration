package com.codinginfinity.research.publication.type;

/**
 * Created by andrew on 2016/04/11.
 */
public class Active extends PublicationTypeState {

    private static final long serialVersionUID = 3253872974618373609L;

    private double accreditationPoints;

    public Active() {
    }

    public Active(double accreditationPoints) {
        this.accreditationPoints = accreditationPoints;
    }

    public double getAccreditationPoints() {
        return accreditationPoints;
    }

    public void setAccreditationPoints(double accreditationPoints) {
        this.accreditationPoints = accreditationPoints;
    }
}
