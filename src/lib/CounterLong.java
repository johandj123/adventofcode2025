package lib;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CounterLong<T> {
    private final Map<T, Long> map = new HashMap<>();

    public long get(T key) {
        return map.getOrDefault(key, 0L);
    }

    public void set(T key,long count) {
        map.put(key, count);
    }

    public void inc(T key) {
        map.put(key, map.getOrDefault(key, 0L) + 1);
    }

    public void add(T key,long count) {
        map.put(key, map.getOrDefault(key, 0L) + count);
    }

    public Map<T, Long> getMap() {
        return map;
    }

    public Set<T> keySet() {
        return map.keySet();
    }

    public Collection<Long> values() {
        return map.values();
    }

    public Set<Map.Entry<T, Long>> entrySet() {
        return map.entrySet();
    }

    public Map.Entry<T, Long> maxCount() {
        return map.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
