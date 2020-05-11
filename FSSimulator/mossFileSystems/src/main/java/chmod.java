public class chmod {
    private static String PROGRAM_NAME = "chmod";

    public static void main(String[] args) throws Exception {
        Kernel.initialize();

        if (args.length < 2) {
            Kernel.perror(PROGRAM_NAME + ": wrong signature. It has to be java chmod [mode] [names]");
            Kernel.exit(1);
        }

        int value = Integer.parseInt(args[0], 8);
        short mode = (short) value;

        if (mode < 00 || mode > 0777) {
            Kernel.perror(PROGRAM_NAME + ": wrong mode. Mode should be from 000 to 777");
            Kernel.exit(1);
        }

        short status;
        for (int i = 1; i < args.length; i++) {
            status = (short) Kernel.chmod(args[i], mode);

            if (status < 0) {
                Kernel.perror(PROGRAM_NAME + ": Can't find inode\n");
                Kernel.exit(1);
            }

            if (status != mode) {
                Kernel.perror(PROGRAM_NAME + ": Mode is not changed\n");
                Kernel.exit(1);
            }
        }

        Kernel.exit(0);
    }
}