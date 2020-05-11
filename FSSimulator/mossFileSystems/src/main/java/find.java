//java find [name]

class find {
    public static String PROGRAM_NAME = "find";

    public static void main(String[] args) throws Exception {
        Kernel.initialize();

        if (args.length != 1) {
            Kernel.perror(PROGRAM_NAME + ": wrong signature. It has to be : java find [name]");
            Kernel.exit(1);
        }

        searchFiles(args[0]);
    }

    private static void searchFiles(String name) throws Exception {
        // stat the name to get information about the file or directory
        Stat stat = new Stat();
        int status = Kernel.stat(name, stat);

        if (status < 0) {
            Kernel.perror(PROGRAM_NAME);
            Kernel.exit(1);
        }

        // mask the file type from the mode  S_IFMT - file type mask
        short type = (short) (stat.getMode() & Kernel.S_IFMT);

        // if name is a regular file, print the info
        if (type == Kernel.S_IFREG) {
            System.out.println(name);
        }
        // if name is a directory open it and read the contents
        else if (type == Kernel.S_IFDIR) {
            System.out.println(name);

            int fd = Kernel.open(name, Kernel.O_RDONLY);

            if (fd < 0) {
                Kernel.perror(PROGRAM_NAME);
                System.err.println(PROGRAM_NAME + ": unable to open \"" + name + "\" for reading");
                Kernel.exit(1);
            } else {
                // create a directory entry structure to hold data as we read
                DirectoryEntry directoryEntry = new DirectoryEntry();

                // while we can read, print the information on each entry
                while (true) {
                    // read an entry; quit loop if error or nothing read
                    status = Kernel.readdir(fd, directoryEntry);
                    if (status <= 0)
                        break;

                    String entryName = directoryEntry.getName();

                    if (!entryName.equals(".") && !entryName.equals("..")) {
                        String fullName = getFullName(name, entryName);
                        searchFiles(fullName);
                    }
                }
            }
            Kernel.close(fd);
        }
    }

    private static String getFullName(String name, String entryName) {
        StringBuilder stringBuilder = new StringBuilder(name);
        if (!name.equals("/")) {
            stringBuilder.append("/");
        }
        stringBuilder.append(entryName);

        return stringBuilder.toString();
    }
}