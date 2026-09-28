import java.io.*;

/**
 * Handles basic file I/O so records can persist between application sessions.
 * Uses Java object serialization to save/load the entire AppData bundle.
 */
public class FileHandler {

    /** Saves the given data to disk. Throws IOException on failure. */
    public static void save(AppData data, String fileName) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(data);
        }
    }

    /**
     * Loads data from disk. If the file does not exist yet (first run),
     * returns a fresh, empty AppData instead of failing.
     */
    public static AppData load(String fileName) throws IOException, ClassNotFoundException {
        File file = new File(fileName);
        if (!file.exists()) {
            return new AppData();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (AppData) in.readObject();
        }
    }
}
