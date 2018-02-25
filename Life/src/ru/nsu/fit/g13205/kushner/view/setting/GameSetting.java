package ru.nsu.fit.g13205.kushner.view.setting;

/**
 * Created by Konstantin on 25.02.2016.
 */
public class GameSetting {

    private final double lifeBegin;
    private final double lifeEnd;
    private final double birthBegin;
    private final double birthEnd;
    private final double FSTImpact;
    private final double SNDImpact;

    public GameSetting(double lifeBegin, double lifeEnd, double birthBegin, double birthEnd, double FSTImpact, double SNDImpact) {
        this.lifeBegin = lifeBegin;
        this.lifeEnd = lifeEnd;
        this.birthBegin = birthBegin;
        this.birthEnd = birthEnd;
        this.FSTImpact = FSTImpact;
        this.SNDImpact = SNDImpact;
    }

    public double getLifeBegin() {
        return lifeBegin;
    }

    public double getLifeEnd() {
        return lifeEnd;
    }

    public double getBirthBegin() {
        return birthBegin;
    }

    public double getBirthEnd() {
        return birthEnd;
    }

    public double getFSTImpact() {
        return FSTImpact;
    }

    public double getSNDImpact() {
        return SNDImpact;
    }
}
