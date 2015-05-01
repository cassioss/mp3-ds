package maekawa;

/**
 * @author Cassio
 * @version 1.0
 */
public class Mutex {

    public static void main(String[] args) {
        if (args.length < 3 || args.length > 4) {
            System.out.println("Usage: java maekawa.Mutex [cs_int] [next_req] [tot_exec_time] [option]");
            return;
        }

        int criticalSectionInterval = Integer.valueOf(args[0]);
        int timeForNextRequest = Integer.valueOf(args[1]);
        int totalExecutionTime = Integer.valueOf(args[2]);
        int option = 0;

        if (args.length == 4 && Integer.valueOf(args[3]) == 1)
            option = 1;
        else
            throw new IllegalArgumentException("Error: invalid option");
    }
}
