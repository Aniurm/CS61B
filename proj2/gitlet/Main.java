package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Aniurm
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        Repository.basicCommandError(args);
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                Repository.add(args);
                break;
            case "commit":
                Repository.commit(args);
                break;
            case "rm":
                Repository.rm(args);
                break;
            case "log":
                Repository.log();
                break;
        }
    }
}
