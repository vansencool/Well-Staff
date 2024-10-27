package dev.vansen.utility.resource;

import dev.vansen.welldevelopment.Holder;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class ResourceSaver {

    public static void save() {
        try {
            String jarPath = Holder.get().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);
            ZipFile zipFile = new ZipFile(jarPath);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();

                if (entryName.equals("plugin.yml") || entryName.startsWith("META-INF/")) {
                    continue;
                }

                if (entryName.endsWith(".yml") || entryName.endsWith(".yaml") || entryName.endsWith(".txt") || entryName.endsWith(".md") || entryName.endsWith(".json") || entryName.endsWith(".text")) {
                    File outFile = new File(Holder.get().getDataFolder(), entryName);
                    if (!outFile.exists()) {
                        InputStream inputStream = zipFile.getInputStream(entry);
                        create(outFile);
                        copy(inputStream, outFile);
                    }
                }
            }
            zipFile.close();
        } catch (IOException e) {
            Holder.get()
                    .getComponentLogger()
                    .error("Failed to save resources", e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void create(@NotNull File file) {
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    private static void copy(@NotNull InputStream inputStream, @NotNull File file) throws IOException {
        try (inputStream; OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
    }
}
