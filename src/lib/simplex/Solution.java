package lib.simplex;

import lib.BigRational;

public class Solution {
    private final BigRational value;
    private final BigRational[] vars;

    public Solution(BigRational value, BigRational[] vars) {
        this.value = value;
        this.vars = vars;
    }

    public BigRational getValue() {
        return value;
    }

    public BigRational[] getVars() {
        return vars;
    }
}
