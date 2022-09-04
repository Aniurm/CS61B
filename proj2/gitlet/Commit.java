package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.*;

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
    private final String message;
    private final String timestamp;
    private final String father;
    private final String mother;

    // map: filename<-->sha1
    private final Map<String, String> blobs;

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
        blobs = new HashMap<>();
    }

    public String generateHash() {
        String shaContent = message +
                timestamp +
                father +
                mother +
                blobs;
        return Utils.sha1(shaContent);
    }

    // get commit from commits folder according to sha1 value
    public static Commit getCommit(String sha1) {
        File targetFile = Utils.join(Repository.COMMITS, sha1);
        if (!targetFile.exists()) {
            System.out.println("Target file does not exist.");
            System.exit(0);
        }
        return Utils.readObject(targetFile, Commit.class);
    }

    public static Commit getCommitByPointer(String pointerName) {
        File pointer = Utils.join(Repository.REFS, pointerName);
        if (!pointer.exists()) {
            System.out.println("Pointer not exist");
            System.exit(0);
        }
        String sha1 = Utils.readContentsAsString(pointer);
        File commitFile = Utils.join(Repository.COMMITS, sha1);
        return Utils.readObject(commitFile, Commit.class);
    }

    public Map<String, String> getBlobs() {
        return blobs;
    }

    public static void copyBlobs(Commit des, Commit src) {
        Map<String, String> srcBlobs = src.getBlobs();
        for (String key : srcBlobs.keySet()) {
            des.getBlobs().put(key, srcBlobs.get(key));
        }
    }

    public void saveCommit() {
        File commitFile = Utils.join(Repository.COMMITS, this.generateHash());
        Utils.writeObject(commitFile, this);
    }

    public boolean fileInCommit(String fileName) {
        return this.blobs.containsKey(fileName);
    }

    @Override
    public void dump() {
        System.out.printf("message: %s\ntimestamp: %s\nfather: %s\nmother: %s\nblobs:%s\n", message, timestamp, father, mother, blobs);
    }
}