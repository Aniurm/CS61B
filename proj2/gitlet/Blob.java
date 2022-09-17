package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;

import static gitlet.Utils.join;
import static gitlet.Utils.writeObject;

public class Blob implements Serializable, Dumpable {
    private final String data;
    public Blob(String data) {
        this.data = data;
    }

    public String generateHash() {
        return Utils.sha1(data);
    }

    public static void writeBlob(String content) {
        Blob newBlob = new Blob(content);
        String newSha1 = newBlob.generateHash();
        writeObject(join(Repository.BLOBS, newSha1), newBlob);
    }

    public static String getBlobDataByName(String blobName) {
        return Utils.readObject(join(Repository.BLOBS, blobName), Blob.class).data;
    }

    @Override
    public void dump() {
        System.out.printf("data: %s\n", data);
    }
}