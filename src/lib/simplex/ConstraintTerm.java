package lib.simplex;

import lib.BigRational;

public class ConstraintTerm {
    private final BigRational factor;
    private final int var;

    public ConstraintTerm(BigRational factor, int var) {
        this.factor = factor;
        this.var = var;
    }

    public BigRational getFactor() {
        return factor;
    }

    public int getVar() {
        return var;
    }
}
