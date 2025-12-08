package lib;

import java.util.*;
import java.util.stream.Collectors;

public class Graph<T> {
    private final Set<T> nodes = new HashSet<>();
    private final Map<T, Set<T>> links = new HashMap<>();

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
        return links.computeIfAbsent(from, _ -> new HashSet<>()).add(to);
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

    public Set<Set<T>> maxCliques() {
        Set<Set<T>> cliques = new HashSet<>();
        BronKerbosch2(new HashSet<>(), new HashSet<>(nodes), new HashSet<>(), cliques);
        return cliques;
    }

    private void BronKerbosch2(Set<T> R, Set<T> P, Set<T> X, Set<Set<T>> cliques) {
        if (P.isEmpty() && X.isEmpty()) {
            cliques.add(R);
            return;
        }
        T u = !P.isEmpty() ? P.iterator().next() : X.iterator().next();
        Set<T> Pu = new HashSet<>(P);
        Pu.removeAll(links.get(u));
        for (T v : Pu) {
            Set<T> R2 = new HashSet<>(R);
            R2.add(v);
            Set<T> n = links.get(v);
            Set<T> P2 = P.stream().filter(n::contains).collect(Collectors.toSet());
            Set<T> X2 = X.stream().filter(n::contains).collect(Collectors.toSet());
            BronKerbosch2(R2, P2, X2, cliques);
            P.add(v);
            X.remove(v);
        }
    }
}
