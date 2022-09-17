package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author Aniurm
 */
public class Repository {
    /*
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMITS = join(GITLET_DIR, "commits");
    public static final File BLOBS = join(GITLET_DIR, "blobs");
    public static final File REFS = join(GITLET_DIR, "refs");
    public static final File INDEX = join(GITLET_DIR, "index");


    // store all commands
    private static final Set<String> commands = new HashSet<>(Arrays.asList("init", "add", "commit", "rm", "log", "global-log", "find", "status", "checkout", "branch", "rm-branch", "reset", "merge"));

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
        Pointer.makePointer("master");
        Pointer.makePointer("HEAD");

        // store information
        Pointer.writePointer("master", shaID);
        Pointer.writePointer("HEAD", shaID);
        initCommit.saveCommit();
    }

    public static void deleteFile(File target) {
        if (!target.delete()) {
            System.out.println("Fail to delete");
            System.exit(0);
        }
    }

    private static boolean initialized() {
        return GITLET_DIR.exists();
    }

    //Adds a copy of the file as it currently exists to the staging area
    public static void add(String[] args) {
        // read staging area
        Stage area = Stage.getStage();
        Map<String, String> addMap = area.getAddMap();
        // get file pointer
        String name = args[1];
        File target = join(CWD, name);
        // not exist
        if (!target.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        // get head commit and blobs
        Commit headCommit = Commit.getCommitByPointer("HEAD");
        Map<String, String> blobs = headCommit.getBlobs();
        // compare with current commit
        String newContent = readContentsAsString(target);
        Blob newBlob = new Blob(newContent);
        String newSha1 = newBlob.generateHash();
        /*
         * If the current working version of the file is identical to
         * the version in the current commit, do not stage it to be added,
         * and remove it from the staging area if it is already there
         */
        if (headCommit.fileInCommit(name) && newSha1.equals(blobs.get(name))) {
            // check if it is in add staging area
            if (area.inAddArea(name)) {
                area.deleteFromAdd(name);
            }
            return;
        }

        // already in staging area
        if (area.inAddArea(name)) {
            // delete old blob
            String oldSha1 = addMap.get(name);
            File oldBlob = join(BLOBS, oldSha1);
            deleteFile(oldBlob);
        }
        writeObject(join(BLOBS, newSha1), newBlob);
        addMap.put(name, newSha1);
        // renew stage
        area.update();
    }

    public static void commit(String[] args) {
        // construct new commit
        String fatherSha1 = Pointer.getData("HEAD");
        Commit newCommit = new Commit(args[1], fatherSha1);
        // get father commit
        Commit father = Commit.getCommit(fatherSha1);
        // copy blobs
        Commit.copyBlobs(newCommit, father);
        // add blobs from staging area:
        // If already exists in the blobMap of father commit, overwrite
        // else add new blob
        Stage stage = Stage.getStage();
        Map<String, String> stageMap = stage.getAddMap();
        Map<String, String> blobMap = newCommit.getBlobs();
        for (String filename : stageMap.keySet()) {
            addBlob(blobMap, stageMap, filename);
        }
        newCommit.saveCommit();
        // update pointers
        String newSha1 = newCommit.generateHash();
        Pointer.writePointer("master", newSha1);
        Pointer.writePointer("HEAD", newSha1);
        // clear staging area
        stage.clear();
        stage.saveStage();
    }

    private static void addBlob(Map<String, String> blobMap, Map<String, String> stageMap, String filename) {
        // get blob name from staging area
        String newBlobName = stageMap.get(filename);
        // change the blobMap of newCommit
        blobMap.put(filename, newBlobName);
    }

    public static void rm(String[] args) {
        String targetName = args[1];
        File targetFile = join(CWD, targetName);

        // get stage and head commit
        Stage stage = Stage.getStage();
        Commit headCommit = Commit.getCommitByPointer("HEAD");

        // check
        if (!stage.inAddArea(targetName) && !headCommit.fileInCommit(targetName)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        // tracked by current commit
        if (headCommit.fileInCommit(targetName)) {
            // stage it for removal
            stage.stageForRemoval(targetName);
            // remove the file from the working directory if the user has not already done so
            if (targetFile.exists()) {
                Utils.restrictedDelete(targetFile);
            }
        }
        // Unstage the file if it is currently staged for addition
        if (stage.inAddArea(targetName)) {
            // delete blob
            stage.deleteBlob(targetName);
            // update stage
            stage.deleteFromAdd(targetName);
        }
        stage.update();
    }

    /*
    * Starting at the current head commit,
    * display information about each commit
    * backwards along the commit tree until the initial commit,
    * following the first parent commit links
    * ignoring any second parents found in merge commits.
    * */
    public static void log() {
        Commit cur = Commit.getCommitByPointer("HEAD");
        String nextSha1 = null;
        while (cur != null) {
            cur.displayCommit();
            nextSha1 = cur.getFather();
            cur = Commit.getCommit(nextSha1);
        }
    }

    public static void global_log() {
        List<String> allCommits = plainFilenamesIn(COMMITS);
        Commit cur;
        for (String commitName : allCommits) {
            cur = Commit.getCommit(commitName);
            cur.displayCommit();
        }
    }

    public static void find(String[] args) {
        List<String> allCommits = plainFilenamesIn(COMMITS);
        List<String> results = new ArrayList<>();
        Commit cur;
        for (String commitName : allCommits) {
            cur = Commit.getCommit(commitName);
            if (cur.getMessage().equals(args[1])) {
                results.add(commitName);
            }
        }
        int size = results.size();
        if (size == 0) {
            System.out.println("Found no commit with that message.");
            return;
        }
        for (String result : results) {
            System.out.println(result);
        }
    }

    public static void status() {
        // Branches
        System.out.println("=== Branches ===");
        List<String> allPointers = plainFilenamesIn(REFS);
        String content;
        String headContent = Pointer.getData("HEAD");
        for (String each : allPointers) {
            if (!each.equals("HEAD")) {
                content = readContentsAsString(join(REFS, each));
                if (content.equals(headContent)) {
                    System.out.print("*");
                }
                System.out.println(each);
            }
        }
        System.out.println(" ");

        // Staged Files
        System.out.println("=== Staged Files ===");
        Stage cur = Stage.getStage();
        Map<String, String> addMap = cur.getAddMap();
        List<String> output = new ArrayList<>(addMap.keySet());
        Collections.sort(output);
        for (String each : output) {
            System.out.println(each);
        }
        System.out.println(" ");
        System.out.println("=== Removed Files ===");
        Set<String> deleteSet = cur.getDeleteSet();
        output = new ArrayList<>(deleteSet);
        for (String each : output) {
            System.out.println(each);
        }
        System.out.println(" ");
    }

    public static void checkout(String[] args) {
        int size = args.length;
        if (size == 3) {
            checkoutOne(args[2]);
        } else if (size == 4) {
            checkoutTwo(args[1], args[3]);
        } else if (size == 2) {
            checkoutThree(args[1]);
        }
    }

    private static void checkoutOne(String filename) {
        Map<String, String> blobs = Commit.getCommitByPointer("HEAD").getBlobs();
        // check exist
        if (!blobs.containsKey(filename)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }

        String srcData = Blob.getBlobDataByName(blobs.get(filename));
        File cwdFile = join(CWD, filename);
        restrictedDelete(cwdFile);
        writeContents(cwdFile, srcData);
    }

    private static void checkoutTwo(String commitID, String filename) {

    }

    private static void checkoutThree(String branchName) {

    }
}
