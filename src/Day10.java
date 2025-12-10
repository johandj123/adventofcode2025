import lib.BigRational;
import lib.InputUtil;
import lib.simplex.ConstraintType;
import lib.simplex.Simplex;
import lib.simplex.Solution;
import lib.simplex.VariableType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day10 {
    public static void main(String[] args) throws IOException {
        List<Machine> machines = InputUtil.readAsLines("input10.txt").stream().map(Machine::new).toList();
        System.out.println(machines.stream().mapToInt(Machine::presses).sum());
        System.out.println(machines.stream().mapToInt(Machine::pressesJoltage).sum());
    }

    static class Machine {
        int indicator = 0;
        List<Integer> buttonsMasks = new ArrayList<>();
        List<List<Integer>> buttons = new ArrayList<>();
        List<Integer> joltageRequirement;

        Machine(String line) {
            String p1 = InputUtil.extract(line, "\\[[.#]+\\]").getFirst();
            for (int i = 0; i < p1.length() - 2; i++) {
                if (p1.charAt(i + 1) == '#') {
                    indicator |= (1 << i);
                }
            }
            List<String> p2s = InputUtil.extract(line, "\\([^)]+\\)");
            for (String p2 : p2s) {
                List<Integer> button = InputUtil.extractPositiveIntegers(p2);
                buttons.add(button);
                int buttonMask = 0;
                for (int i : button) {
                    buttonMask |= (1 << i);
                }
                buttonsMasks.add(buttonMask);
            }
            String p3 = InputUtil.extract(line, "\\{[^}]+\\}").getFirst();
            joltageRequirement = InputUtil.extractPositiveIntegers(p3);
        }

        int presses() {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < (1 << buttonsMasks.size()); i++) {
                int current = 0;
                int presses = 0;
                for (int j = 0; j < buttonsMasks.size(); j++) {
                    if ((i & (1 << j)) != 0) {
                        current ^= buttonsMasks.get(j);
                        presses++;
                    }
                }
                if (current == indicator) {
                    best = Math.min(best, presses);
                }
            }
            return best;
        }

        int pressesJoltage() {
            Simplex simplex = new Simplex();
            for (int i = 0; i < buttons.size(); i++) {
                simplex.addVariable(VariableType.INTEGER_NONNEGATIVE, new BigRational(-1));
            }
            for (int i = 0; i < joltageRequirement.size(); i++) {
                int requirement = joltageRequirement.get(i);
                simplex.addConstraint(ConstraintType.EQUAL, new BigRational(requirement));
                for (int j = 0; j < buttons.size(); j++) {
                    var button = buttons.get(j);
                    if (button.contains(i)) {
                        simplex.addConstraintTerm(BigRational.ONE, j);
                    }
                }
            }
            Solution solution = simplex.solve();
            return (int) -solution.getValue().longValueExact();
        }
    }
}
