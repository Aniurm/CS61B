package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Stage implements Serializable, Dumpable {
    // Map: filename<-->sha1(blob name)
    private Map<String, String> map;
    Stage() {
        map = new HashMap<>();
    }
    public Map<String, String> getMap() {
        return map;
    }
    @Override
    public void dump() {
        System.out.printf("size: %d%nmapping: %s%n", _size, _mapping);
    }
    int _size;
    TreeMap<String, String> _mapping = new TreeMap<>();
}
