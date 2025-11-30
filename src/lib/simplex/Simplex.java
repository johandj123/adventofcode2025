package lib.simplex;

import lib.BigRational;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simplex {
    private final List<Variable> variables = new ArrayList<>();
    private final List<Constraint> constraints = new ArrayList<>();
    private final List<ConstraintTerm> terms = new ArrayList<>();

    public int addVariable(VariableType type, BigRational goalFactor) {
        int c = variables.size();
        variables.add(new Variable(type, goalFactor));
        if (type == VariableType.BOOLEAN) {
            addConstraint(ConstraintType.LESS_OR_EQUAL, BigRational.ONE);
        }
        return c;
    }

    public void addConstraint(ConstraintType type, BigRational value) {
        constraints.add(new Constraint(type, value, terms.size()));
    }

    public void addConstraintTerm(BigRational factor, int var) {
        terms.add(new ConstraintTerm(factor, var));
        constraints.get(constraints.size() - 1).incCountTerms();
    }

    private Solution solveInner() {
        int countPhysVars = 0;
        for (Variable variable : variables) {
            variable.setPhysVar(countPhysVars);
            if (variable.getType().isNonNegative()) {
                countPhysVars++;
            } else {
                countPhysVars += 2;
            }
        }
        int countPhysConstraints = 0;
        for (Constraint constraint : constraints) {
            constraint.setPhysConstraint(countPhysConstraints);
            if (constraint.getType() == ConstraintType.EQUAL) {
                countPhysConstraints += 2;
            } else {
                countPhysConstraints++;
            }
        }

        int width = countPhysVars + countPhysConstraints + 2;
        int height = countPhysConstraints + 1;
        Tableau t = new Tableau(width, height);

        // Fill in b
        boolean aux = false;
        int rowIndex = 1;
        for (Constraint constraint : constraints) {
            if (constraint.getValue().isNegative() || (constraint.getType() == ConstraintType.EQUAL && !constraint.getValue().isZero())) {
                aux = true;
            }
            t.add(rowIndex++, width - 1, constraint.getValue());
            if (constraint.getType() == ConstraintType.EQUAL) {
                t.add(rowIndex++, width - 1, constraint.getValue().negate());
            }
        }

        // Fill in A
        rowIndex = 1;
        for (Constraint constraint : constraints) {
            for (int k = constraint.getFirstTerm(); k < constraint.getFirstTerm() + constraint.getCountTerms(); k++) {
                ConstraintTerm term = terms.get(k);
                Variable v = variables.get(term.getVar());
                t.add(rowIndex, v.getPhysVar(), term.getFactor());
                if (!v.getType().isNonNegative()) {
                    t.add(rowIndex, v.getPhysVar() + 1, term.getFactor().negate());
                }
            }
            rowIndex++;

            if (constraint.getType() == ConstraintType.EQUAL) {
                for (int k = constraint.getFirstTerm(); k < constraint.getFirstTerm() + constraint.getCountTerms(); k++) {
                    ConstraintTerm term = terms.get(k);
                    Variable v = variables.get(term.getVar());
                    t.add(rowIndex, v.getPhysVar(), term.getFactor().negate());
                    if (!v.getType().isNonNegative()) {
                        t.add(rowIndex, v.getPhysVar() + 1, term.getFactor());
                    }
                }
                rowIndex++;
            }
        }
        for (int i = 0; i < countPhysConstraints; i++) {
            t.add(i + 1, i + countPhysVars, BigRational.ONE);
        }

        // Determine initial base
        int[] baseColumns = new int[width];
        Arrays.fill(baseColumns, -1);
        for (int i = 0; i < countPhysConstraints; i++) {
            baseColumns[countPhysVars + i] = i;
        }
        int[] baseVectors = new int[countPhysConstraints];
        for (int i = 0; i < countPhysConstraints; i++) {
            baseVectors[i] = countPhysVars + i;
        }

        // Solve auxiliary problem, if needed
        if (aux) {
            t.minusOneColumn();

            // Solve aux tableau
            boolean first = true;
            while (true) {
                int i;
                int j;
                if (first) {
                    first = false;

                    // Column is the -1 column
                    i = width - 2;
                    j = t.findSmallestB();
                } else {
                    // Determine column to work on
                    i = t.findPivotColumn(true);
                    if (i == -1) {
                        break;
                    }

                    // Determine row to work on
                    j = t.findPivotRow(i);
                }

                // If now row coule be found, there is an unbounded direction
                if (j == -1) {
                    return null;
                }

                // Pivot
                t.pivot(j, i);

                // Update base
                baseColumns[baseVectors[j - 1]] = -1;
                baseVectors[j - 1] = i;
                baseColumns[i] = j - 1;
            }

            if (!t.getTopRow(width - 1).isZero()) {
                // No feasible base
                return null;
            }

            // Make sure that the -1 column is no longer in the base
            if (baseColumns[width - 2] != -1) {
                int j = baseColumns[width - 2] + 1;
                int i;
                for (i = 0; i < width - 2; i++) {
                    if (baseColumns[i] != -1) {
                        continue;
                    }
                    if (!t.get(j, i).isZero()) {
                        break;
                    }
                }
                if (i == width - 2) {
                    // No feasible base
                    return null;
                }

                // Pivot
                t.pivot(j, i);

                // Update Base
                baseColumns[baseVectors[j - 1]] = -1;
                baseVectors[j - 1] = i;
                baseColumns[i] = j - 1;
            }
        }

        // Fill in -c
        rowIndex = 0;
        for (Variable variable : variables) {
            t.set(0, rowIndex++, variable.getGoalFactor().negate());
            if (!variable.getType().isNonNegative()) {
                t.set(0, rowIndex++, variable.getGoalFactor());
            }
        }
        while (rowIndex < width) {
            t.set(0, rowIndex++, BigRational.ZERO);
        }

        if (aux) {
            for (int i = 0; i < width - 2; i++) {
                if (baseColumns[i] != -1) {
                    BigRational d = t.getTopRow(i);
                    if (!d.isZero()) {
                        for (int k = 0; k < width; k++) {
                            BigRational e = t.get(baseColumns[i] + 1, k);
                            if (!e.isZero()) {
                                t.add(0, k, d.multiply(e).negate());
                            }
                        }
                    }
                }
            }

            t.emptyMinusOneColumn();
        }

        // Solve
        while (true) {
            // Determine column to work on
            int i = t.findPivotColumn(false);
            if (i == -1) {
                break;
            }

            // Determine row to work on
            int j = t.findPivotRow(i);

            // If now row could be found, there is an unbounded direction
            if (j == -1) {
                return null;
            }

            // Change this row into a unit vector
            t.pivot(j, i);

            // Update Base
            baseColumns[baseVectors[j - 1]] = -1;
            baseVectors[j - 1] = i;
            baseColumns[i] = j - 1;
        }

        // Determine solution
        BigRational[] physsol = new BigRational[countPhysVars + countPhysConstraints];
        Arrays.fill(physsol, BigRational.ZERO);
        for (int i = 0; i < countPhysConstraints; i++) {
            BigRational v = t.get(i + 1, width - 1);
            if (v.isNegative()) {
                // Not feasible, row base_vectors[i], value v
                return null;
            }
            physsol[baseVectors[i]] = v;
        }
        BigRational[] sol = new BigRational[variables.size()];
        rowIndex = 0;
        for (int i = 0; i < variables.size(); i++) {
            Variable variable = variables.get(i);
            if (variable.getType().isNonNegative()) {
                sol[i] = physsol[rowIndex++];
            } else {
                sol[i] = physsol[rowIndex].subtract(physsol[rowIndex + 1]);
                rowIndex += 2;
            }
        }
        BigRational value = t.getTopRowOrDefault(width - 1);
        return new Solution(value, sol);
    }

    public Solution solve() {
        return solveIntegerBranchAndBound(null);
    }

    private Solution solveIntegerBranchAndBound(Solution solution) {
        // Determine the non-integral solution
        Solution nonIntegralSolution = solveInner();

        // Return if there is no solution or it has a lower value than the one we already have
        if (nonIntegralSolution == null ||
                (solution != null && nonIntegralSolution.getValue().compareTo(solution.getValue()) <= 0)) {
            return solution;
        }

        int i = 0;
        while (i < variables.size()) {
            if (variables.get(i).getType().isInteger() && !nonIntegralSolution.getVars()[i].isIntegral()) {
                break;
            }
            i++;
        }

        if (i == variables.size()) {
            return nonIntegralSolution;
        }

        // Branch and bound
        addConstraint(ConstraintType.LESS_OR_EQUAL, nonIntegralSolution.getVars()[i].floor());
        addConstraintTerm(BigRational.ONE, i);
        solution = solveIntegerBranchAndBound(solution);
        constraints.remove(constraints.size() - 1);
        terms.remove(terms.size() - 1);

        addConstraint(ConstraintType.LESS_OR_EQUAL, nonIntegralSolution.getVars()[i].ceil().negate());
        addConstraintTerm(BigRational.MINUS_ONE, i);
        solution = solveIntegerBranchAndBound(solution);
        constraints.remove(constraints.size() - 1);
        terms.remove(terms.size() - 1);

        return solution;
    }
}
