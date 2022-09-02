package gitlet;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.List;
import java.util.TreeMap;

/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author Aniurm
 */
public class Commit implements Serializable, Dumpable {
    /*
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private String timestamp;
    private String father;
    private String mother;
    private List<String> blobs;
    private String id;

    public Commit(String message, String parent) {
        this.message = message;
        this.father = parent;
        this.mother = null;
        Date date;
        // init commit
        if (parent == null) {
            date = new Date(0);
        } else {
            date = new Date();
        }
        timestamp = date.toString();
        blobs = new ArrayList<>();
        id = generateHash();
    }

    public String generateHash() {
        StringBuilder shaContent = new StringBuilder();
        shaContent.append(message);
        shaContent.append(timestamp);
        shaContent.append(father);
        shaContent.append(mother);
        shaContent.append(blobs);
        return Utils.sha1(shaContent.toString());
    }

    @Override
    public void dump() {
        System.out.printf("size: %d%nmapping: %s%n", _size, _mapping);
    }
        int _size;
        TreeMap<String, String> _mapping = new TreeMap<>();
}