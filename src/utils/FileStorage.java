package utils;

import java.io.*;

public class FileStorage {

    /**
     * Writes a MyList<String> to a file (one line per string).
     */
    public static void writeLines(String path, MyList<String> lines) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
            for (int i = 0; i < lines.size(); i++) {
                writer.println(lines.get(i));
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to write to file: " + path);
            e.printStackTrace();
        }
    }

    /**
     * Reads all lines from a file and returns a MyList<String>.
     */
    public static MyList<String> readLines(String path) {
        MyList<String> lines = new MyArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("⚠️ File not found or unreadable: " + path);
        }
        return lines;
    }
}
