package gitlet;

import java.io.File;
import java.io.IOException;

public class Pointer {
    public static void makePointer(String name) {
        File pointer = Utils.join(Repository.REFS, name);
        try {
            pointer.createNewFile();
        } catch (IOException excp) {
            System.out.println("makePointer error");
        }
    }

    public static void makePointer(String name, String data) {
        makePointer(name);
        writePointer(name, data);
    }

    public static void writePointer(String pointerName, String data) {
        File pointer = Utils.join(Repository.REFS, pointerName);
        Utils.writeContents(pointer, data);
    }

    public static String getData(String pointerName) {
        File pointer = Utils.join(Repository.REFS, pointerName);
        if (!pointer.exists()) {
            System.out.println("File doesn't exist.");
            System.exit(0);
        }
        return Utils.readContentsAsString(pointer);
    }
}