import lib.Graph;
import lib.InputUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 {
	static void main() throws IOException {
		List<String> input = InputUtil.readAsLines("input11.txt");
		Graph<String> graph = new Graph<>();
		for (String line : input) {
			String[] sp = line.split(":");
			String left = sp[0].trim();
			sp = sp[1].split(" ");
			for (String right : sp) {
				graph.addLink(right.trim(), left);
			}
		}
		Map<String, Long> cache = new HashMap<>();
		long first = pathCount(graph, "out", cache);
		System.out.println(first);
	}

	private static long pathCount(Graph<String> graph, String node, Map<String, Long> cache) {
		if ("you".equals(node)) {
			return 1L;
		}
		if (cache.containsKey(node)) {
			return cache.get(node);
		}
		long count = 0L;
		for (String child : graph.getNeighbours(node)) {
			count += pathCount(graph, child, cache);
		}
		cache.put(node, count);
		return count;
	}
}
