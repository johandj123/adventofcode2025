package lib.simplex;

import lib.BigRational;

public class Variable {
    private final VariableType type;
    private int physVar;
    private final BigRational goalFactor;

    Variable(VariableType type, BigRational goalFactor) {
        this.type = type;
        this.goalFactor = goalFactor;
    }

    VariableType getType() {
        return type;
    }

    int getPhysVar() {
        return physVar;
    }

    void setPhysVar(int physVar) {
        this.physVar = physVar;
    }

    BigRational getGoalFactor() {
        return goalFactor;
    }
}
