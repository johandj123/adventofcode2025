package lib;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Counter<T> {
    private final Map<T, Integer> map = new HashMap<>();

    public int get(T key) {
        return map.getOrDefault(key, 0);
    }

    public void set(T key,int count) {
        map.put(key, count);
    }

    public void inc(T key) {
        map.put(key, map.getOrDefault(key, 0) + 1);
    }

    public void add(T key,int count) {
        map.put(key, map.getOrDefault(key, 0) + count);
    }

    public Map<T, Integer> getMap() {
        return map;
    }

    public Set<T> keySet() {
        return map.keySet();
    }

    public Collection<Integer> values() {
        return map.values();
    }

    public Set<Map.Entry<T, Integer>> entrySet() {
        return map.entrySet();
    }

    public Map.Entry<T, Integer> maxCount() {
        return map.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
