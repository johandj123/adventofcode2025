import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class Day6 {
    public static void main(String[] args) throws IOException {
        first();
        second();
    }

    private static void first() throws IOException {
        List<List<String>> input = InputUtil.readAsLines("input6.txt").stream()
                .map(line -> Arrays.stream(line.trim().split("\\s+")).toList())
                .toList();
        long result = 0L;
        for (int col = 0; col < input.getFirst().size(); col++) {
            String op = input.getLast().get(col);
            BinaryOperator<Long> func = switch (op) {
                case "*" -> (a, b) -> a * b;
                case "+" -> Long::sum;
                default -> throw new IllegalStateException();
            };
            List<Long> numbers = new ArrayList<>();
            for (int row = 0; row < input.size() - 1; row++) {
                numbers.add(Long.parseLong(input.get(row).get(col)));
            }
            long columnResult = numbers.stream().reduce(func).orElseThrow();
            result += columnResult;
        }
        System.out.println(result);
    }

    private static void second() throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input6.txt")).transpose();
        String inputString = Arrays.stream(charMatrix.toString().split("\n")).map(String::trim).collect(Collectors.joining("\n"));
        List<String> problems = List.of(inputString.split("\n\n"));
        long result = 0L;
        for (String problem : problems) {
            BinaryOperator<Long> func;
            if (problem.contains("*")) {
                func = (a, b) -> a * b;
            } else if (problem.contains("+")) {
                func = Long::sum;
            } else {
                throw new IllegalStateException();
            }
            long problemResult = InputUtil.extractPositiveLongs(problem).stream().reduce(func).orElseThrow();
            result += problemResult;
        }
        System.out.println(result);
    }
}
