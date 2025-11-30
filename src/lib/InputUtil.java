package lib;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InputUtil {
    private InputUtil() {
    }

    public static String readAsString(String filename) throws IOException {
        return Files.readString(new File(filename).toPath()).trim();
    }

    public static List<String> readAsStringGroups(String filename) throws IOException {
        String input = readAsString(filename);
        String[] parts = input.split("\n\n");
        return Arrays.asList(parts);
    }

    public static List<String> readAsLines(String filename) throws IOException {
        return Files.readAllLines(new File(filename).toPath());
    }

    public static List<Integer> readAsIntegers(String filename) throws IOException {
        return splitIntoIntegers(readAsString(filename));
    }

    public static List<Integer> splitIntoIntegers(String input) {
        String[] parts = input.trim().split("\\s+");
        return Arrays.stream(parts).map(Integer::parseInt).collect(Collectors.toList());
    }

    public static List<Long> splitIntoLongs(String input) {
        String[] parts = input.trim().split("\\s+");
        return Arrays.stream(parts).map(Long::parseLong).collect(Collectors.toList());
    }

    public static List<Integer> extractPositiveIntegers(String input) {
        String[] parts = input.split("\\D");
        return Arrays.stream(parts)
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public static List<String> extract(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        List<String> result = new ArrayList<>();
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }
}
