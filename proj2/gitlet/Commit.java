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
        if (sha1 == null || sha1.length() == 0) {
            return null;
        }
        File targetFile = Utils.join(Repository.COMMITS, sha1);
        if (!targetFile.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        return Utils.readObject(targetFile, Commit.class);
    }

    // naively approach to find commit using shortened id
    public static Commit getCommitShort(String prefix) {
        List<String> allCommits = Utils.plainFilenamesIn(Repository.COMMITS);
        for (String each : allCommits) {
            if(each.startsWith(prefix)) {
                File commitFile = Utils.join(Repository.COMMITS, each);
                return Utils.readObject(commitFile, Commit.class);
            }
        }
        System.out.println("No commit with that id exists.");
        System.exit(0);
        return null;
    }

    public static Commit getCommitByPointer(String pointerName) {
        if (pointerName == null || pointerName.length() == 0) {
            return null;
        }
        File pointer = Utils.join(Repository.REFS, pointerName);
        if (!pointer.exists()) {
            System.out.println("Pointer not exist");
            System.exit(0);
        }
        String sha1 = Utils.readContentsAsString(pointer);
        File commitFile = Utils.join(Repository.COMMITS, sha1);
        return Utils.readObject(commitFile, Commit.class);
    }

    public String getFather() {
        return father;
    }

    public String getMessage() {
        return message;
    }

    public String getBlobData(String filename) {
        if (!blobs.containsKey(filename)) {
            System.out.println("File does not exist in that commit");
            System.exit(0);
        }
        return Blob.getBlobDataByName(blobs.get(filename));
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

    public void addBlobsFromStage(Stage stage) {
        Map<String, String> stageMap = stage.getAddMap();
        for (String filename : stageMap.keySet()) {
            addBlob(filename, stageMap.get(filename));
        }
    }

    private void addBlob(String filename, String sha1) {
        this.blobs.put(filename, sha1);
    }

    public void saveCommit() {
        File commitFile = Utils.join(Repository.COMMITS, this.generateHash());
        Utils.writeObject(commitFile, this);
    }

    public boolean fileInCommit(String fileName) {
        return this.blobs.containsKey(fileName);
    }

    public void displayCommit() {
        System.out.println("===");
        System.out.println("commit " + this.generateHash());
        System.out.println("Date: " + this.timestamp);
        System.out.println(message);
        System.out.println(" ");
    }


    @Override
    public void dump() {
        System.out.printf("message: %s\ntimestamp: %s\nfather: %s\nmother: %s\nblobs:%s\n", message, timestamp, father, mother, blobs);
    }
}