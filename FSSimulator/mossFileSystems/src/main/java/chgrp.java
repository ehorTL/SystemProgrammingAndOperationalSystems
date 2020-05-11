
public class chgrp {
    /**
     * The name of this program. This is the program name that is used when
     * displaying error messages.
     */
    private static String PROGRAM_NAME = "chgrp";

    public static void main(String[] args) throws Exception {

        // initialize the file system simulator kernel
        Kernel.initialize();

        // if no paths to link
        if (args.length < 2) {
            Kernel.perror("USAGE: chown <gid_number> <entry_names>...");
            Kernel.exit(1);
        }
        short gid = Short.valueOf(args[0]);
        for (int i = 1; i < args.length; i++) {
            int status = Kernel.chgrp(args[i], gid);
            if (status < 0) {
                Kernel.perror(PROGRAM_NAME);
                System.err.println(PROGRAM_NAME + ": unable to find inode");
                Kernel.exit(1);
            }
        }

        // exit with success if we read all the files without error
        Kernel.exit(0);
    }
}