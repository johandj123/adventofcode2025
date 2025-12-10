import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day10 {
    public static void main(String[] args) throws IOException {
        List<Machine> machines = InputUtil.readAsLines("input10.txt").stream().map(Machine::new).toList();
        System.out.println(machines.stream().mapToInt(Machine::presses).sum());
    }

    static class Machine {
        int indicator = 0;
        List<Integer> buttons = new ArrayList<>();

        Machine(String line) {
            String p1 = InputUtil.extract(line, "\\[[.#]+\\]").getFirst();
            for (int i = 0; i < p1.length() - 2; i++) {
                if (p1.charAt(i + 1) == '#') {
                    indicator |= (1 << i);
                }
            }
            List<String> p2s = InputUtil.extract(line, "\\([^)]+\\)");
            for (String p2 : p2s) {
                List<Integer> l = InputUtil.extractPositiveIntegers(p2);
                int button = 0;
                for (int i : l) {
                    button |= (1 << i);
                }
                buttons.add(button);
            }
        }

        int presses() {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < (1 << buttons.size()); i++) {
                int current = 0;
                int presses = 0;
                for (int j = 0; j < buttons.size(); j++) {
                    if ((i & (1 << j)) != 0) {
                        current ^= buttons.get(j);
                        presses++;
                    }
                }
                if (current == indicator) {
                    best = Math.min(best, presses);
                }
            }
            return best;
        }
    }
}
