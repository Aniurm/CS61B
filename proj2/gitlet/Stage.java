package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class Stage implements Serializable, Dumpable {
    // Map: filename<-->sha1(blob name)
    private Map<String, String> addMap;
    private Set<String> deleteSet;
    Stage() {
        addMap = new HashMap<>();
        deleteSet = new HashSet<>();
    }
    public Map<String, String> getAddMap() {
        return addMap;
    }

    public Set<String> getDeleteSet() {
        return deleteSet;
    }

    public static Stage getStage() {
        return Utils.readObject(Repository.INDEX, Stage.class);
    }

    public void saveStage() {
        Utils.writeObject(Repository.INDEX, this);
    }

    public void clear() {
        addMap = new HashMap<>();
        deleteSet = new HashSet<>();
    }

    public void deleteFromAdd(String fileName) {
        if (!addMap.containsKey(fileName)) {
            System.out.println("Not in add area");
            System.exit(0);
        }
        addMap.remove(fileName);
    }

    public boolean inAddArea(String fileName) {
        return addMap.containsKey(fileName);
    }

    public void stageForRemoval(String fileName) {
        deleteSet.add(fileName);
    }

    // Delete the corresponding blob of file
    public void deleteBlob(String filename) {
        String blobName = this.addMap.get(filename);
        File blobFile = Utils.join(Repository.BLOBS, blobName);
        Repository.deleteFile(blobFile);
    }

    @Override
    public void dump() {
        System.out.printf("addMap size: %d%naddMapping: %s%ndeleteSet size: %d\ndeleteSet:%s\n", addMap.size(), addMap, deleteSet.size(), deleteSet);
    }
}
