package lib;

import java.util.*;
import java.util.stream.Collectors;

public class Graph<T> {
    private Set<T> nodes = new HashSet<>();
    private Map<T, Set<T>> links = new HashMap<>();

    public boolean addNode(T node) {
        Objects.requireNonNull(node);
        return nodes.add(node);
    }

    public Set<T> getNodes() {
        return Collections.unmodifiableSet(nodes);
    }

    public boolean addLink(T from,T to) {
        addNode(from);
        addNode(to);
        return links.computeIfAbsent(from, key -> new HashSet<>()).add(to);
    }

    public void addLinkBidirectional(T node1,T node2) {
        addLink(node1, node2);
        addLink(node2, node1);
    }

    public Set<T> getNeighbours(T node) {
        return links.getOrDefault(node, Collections.emptySet());
    }

    public List<T> getLeafNodes() {
        return nodes.stream()
                .filter(node -> getNeighbours(node).isEmpty())
                .collect(Collectors.toList());
    }

    public Set<T> getRootNodes() {
        Set<T> result = new HashSet<>(nodes);
        links.values().stream().flatMap(Collection::stream).forEach(result::remove);
        return result;
    }

    public List<Set<T>> components() {
        return GraphUtil.components(nodes, this::getNeighbours);
    }
}
