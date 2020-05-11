public class umask {
    /**
     * The name of this program. This is the program name that is used when
     * displaying error messages.
     */
    public static final String PROGRAM_NAME = "umask";

    private static void showUsage() throws Exception {
        System.err.println(PROGRAM_NAME + ": usage: java " + PROGRAM_NAME + " <octal number from 000 to 777>");
        Kernel.exit(1);
    }

    /**
     * Change umask
     *
     * @exception java.lang.Exception if an exception is thrown by an underlying
     *                                operation
     */
    public static void main(String[] argv) throws Exception {
        // initialize the file system simulator kernel
        Kernel.initialize();

        // make sure we got the correct number of parameters
        if (argv.length != 1) {
            showUsage();
        }

        //check if octal number is entered
        String umask = argv[0];
        if (!umask.matches("[0-7]+")) {
            showUsage();
        }

        //parse decimal number form octal string
        int intUmask = Integer.parseInt(umask, 8);
        //cutting octal number to 3 digits
        intUmask &= 511; //511 = 111 111 111 = 0777

        //pass newmask as decimal number
        Kernel.umask((short) intUmask);

        Kernel.exit(0);
    }
}