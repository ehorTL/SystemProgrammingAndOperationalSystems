public class chown {

    /**
     * The name of this program. This is the program name that is used when
     * displaying error messages.
     */
    public static final String PROGRAM_NAME = "chown";

    /**
     * Lists information about named files or directories.
     *
     * @exception java.lang.Exception if an exception is thrown by an underlying
     *                                operation
     */

    public static void main(String[] argv) throws Exception {
        // initialize the file system simulator kernel
        Kernel.initialize();

        // if no paths to link
        if (argv.length < 2) {
            System.err.println(PROGRAM_NAME + ": usage: java " + PROGRAM_NAME + " <uid_number> <entry_names>...");
            Kernel.exit(1);
        }

        short uid = Short.valueOf(argv[0]); //uid   java chown 45
        for (int i = 1; i < argv.length; i++) {
            int status = Kernel.chown(argv[i], uid);
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