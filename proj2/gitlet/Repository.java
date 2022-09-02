package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author Aniurm
 */
public class Repository {
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    private static final File COMMITS = join(GITLET_DIR, "commits");
    private static final File BLOBS = join(GITLET_DIR, "blobs");
    private static final File REFS = join(GITLET_DIR, "refs");
    private static final File INDEX = join(GITLET_DIR, "index");


    // store all commands
    private static Set<String> commands = new HashSet<>(Arrays.asList("init", "add", "commit", "rm", "log", "global-log", "find", "status", "checkout", "branch", "rm-branch", "reset", "merge"));

    // true means has error, false means no error
    public static void basicCommandError(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        } else if (!commands.contains(args[0])) {
            System.out.println("No command with that name exists.");
            System.exit(0);
        } else if (!args[0].equals("init") && !initialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        String firstArg = args[0];
        int len = args.length;
        boolean operandError = (firstArg.equals("init") && len > 1) ||
                (firstArg.equals("commit") && len != 2) ||
                ((firstArg.equals("add") || firstArg.equals("rm")) && len != 2) ||
                ((firstArg.equals("log") || firstArg.equals("global-log")) && len != 1) ||
                (firstArg.equals("find") && len != 2) ||
                (firstArg.equals("status") && len != 1) ||
                ((firstArg.equals("branch") || firstArg.equals("rm-branch")) && len != 2) ||
                ((firstArg.equals("reset") || firstArg.equals("merge")) && len != 2);

        if (firstArg.equals("checkout")) {
            if ((len > 4) || (len == 1) || (len == 3 && !args[1].equals("--")) || (len == 4 && !args[2].equals("--"))) {
                operandError = true;
            }
        }

        if (operandError) {
            System.out.println("Incorrect operands");
            System.exit(0);
        }
    }

    public static void init() {
        // check
        if (initialized()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }

        // build folders
        GITLET_DIR.mkdir();
        COMMITS.mkdir();
        BLOBS.mkdir();
        REFS.mkdir();

        // make staging area
        try {
            INDEX.createNewFile();
        } catch (IOException excep) {
            System.out.println("error");
        }
        Stage area = new Stage();
        writeObject(INDEX, area);

        // make commit and pointer
        Commit initCommit = new Commit("initial commit", null);
        String shaID = initCommit.generateHash();
        File commitFile = join(COMMITS, shaID);
        File master = join(REFS, "master");
        try {
            master.createNewFile();
        } catch (IOException excp) {
            System.out.println("error");
        }

        // store information
        writeContents(master, shaID);
        writeObject(commitFile, initCommit);
    }

    private static boolean initialized() {
        return GITLET_DIR.exists();
    }

    //Adds a copy of the file as it currently exists to the staging area
    public static void add(String[] args) {
        // read staging area
        Stage area = readObject(INDEX, Stage.class);
        Map<String, String> map = area.getMap();
        // get file pointer
        String name = args[1];
        File target = join(CWD, name);
        // not exist
        if (!target.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        // already in staging area
        if (map.containsKey(name)) {
            // delete old blob
            String oldSha1 = map.get(name);
            File oldBlob = join(BLOBS, oldSha1);
            if (!restrictedDelete(oldBlob)) {
                System.out.println("Fail to delete.");
                System.exit(0);
            }
        }
        // write new blob
        String newContent = readContentsAsString(target);
        Blob newBlob = new Blob(newContent);
        String newSha1 = newBlob.generateHash();
        writeObject(join(BLOBS, newSha1), newBlob);
        // change stage information
        map.put(name, newSha1);
        // renew stage
        writeObject(INDEX, area);
    }
}
