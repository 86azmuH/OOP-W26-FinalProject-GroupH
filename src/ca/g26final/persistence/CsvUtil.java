package ca.g26final.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class CsvUtil {
    private CsvUtil() {}

    public static Path resolveDataPath(String fileName) {
        Path dataDir = Paths.get("data");
        return dataDir.resolve(fileName);
    }

    public static void ensureParentDir(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    public static List<String> readAll(Path path) throws IOException {
        if (!Files.exists(path)) return new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
            return lines;
        }
    }

    public static void writeAll(Path path, List<String> lines) throws IOException {
        ensureParentDir(path);
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }
}
