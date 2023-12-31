package net.ledestudio.loomify.io;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Lson {

    private final Gson gson;

    public Lson(@NotNull Gson gson) {
        this.gson = gson;
    }

    public <T> void save(@NotNull Path path, @NotNull T obj) {
        // Check file and directory created
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Write object to file
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path.toString())));
            String json = gson.toJson(obj);

            out.write(json);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally { // close output stream
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Nullable
    public <T> T load(@NotNull Path path, @NotNull Class<T> clazz) {
        // Check file and directory created
        if (Files.notExists(path)) {
            return null;
        }

        // Read object from file
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(path.toString())));
            return gson.fromJson(in, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        } finally { // close input stream
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public void delete(@NotNull Path path) {
        if (Files.notExists(path)) {
            return;
        }

        if (Files.isDirectory(path)) { // Find child and delete if path is directory.
            try {
                Files.list(path).forEach(this::delete);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Delete current file or directory.
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull Gson getGson() {
        return gson;
    }

}
