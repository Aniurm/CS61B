package gitlet;

import java.io.Serializable;
import java.util.TreeMap;

public class Blob implements Serializable, Dumpable {
    private final String data;
    public Blob(String data) {
        this.data = data;
    }

    public String generateHash() {
        return Utils.sha1(data);
    }

    @Override
    public void dump() {
        System.out.printf("size: %d%nmapping: %s%n", _size, _mapping);
    }
    int _size;
    TreeMap<String, String> _mapping = new TreeMap<>();
}