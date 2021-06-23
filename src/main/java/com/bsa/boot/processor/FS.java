package com.bsa.boot.processor;

import com.bsa.boot.entity.GIF;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;

/**
 * @author professorik
 * @created 22/06/2021 - 09:57
 * @project boot
 */
@Service
public class FS {
    private final String PATH = "D:\\IdeaProjects\\bsa-spring-boot-intro\\src\\main\\java\\com\\bsa\\boot\\giphy\\";

    public File downloadGifByUrl(GIF file) {
        try (var inputStream = new BufferedInputStream(new URL(file.getUrl()).openStream())) {
            File directory = new File(getPath("cache\\", file.getQuery()));
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File gif = new File(directory, file.getId() + ".gif");
            var fileOutputStream = new FileOutputStream(gif);
            byte[] buffer = new byte[1024];
            int inputBytes;
            while ((inputBytes = inputStream.read(buffer, 0, 1024)) != -1) {
                fileOutputStream.write(buffer, 0, inputBytes);
            }
            return gif;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    public File getGifPath(GIF file) {
        File gif = new File(getPath("cache\\", file.getQuery()), file.getId() + ".gif");
        return gif.exists() ? gif : null;
    }

    public File getGifPath(String query) {
        File gif = new File(getPath("cache\\", query));
        File[] files = gif.listFiles();
        return files != null ? files[new Random().nextInt(files.length)] : null;
    }

    public File getGifPathFromUser(String query, String id) {
        File gif = new File(getPath("users\\", id, "\\", query));
        File[] files = gif.listFiles();
        return files != null ? files[new Random().nextInt(files.length)] : null;
    }

    public File getFromUserFolder(String id, String query) {
        File userGif = new File(getPath("users\\", id), query);
        if (userGif.listFiles() != null) {
            return userGif.listFiles()[new Random().nextInt(userGif.listFiles().length)];
        }
        return null;
    }

    public void addGifToUserFolder(String id, GIF file) {
        String path = getPath("\\users\\", id, "\\", file.getQuery(), "\\");
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File cacheGif;
        cacheGif = getGifPath(file);
        if (cacheGif != null) {
            cacheGif = new File(cacheGif.getAbsolutePath());
        } else {
            cacheGif = downloadGifByUrl(file);
        }
        copyToUserFolder(id, file.getQuery(), cacheGif.getPath());
    }

    public File copyToUserFolder(String id, String query, String cachePath) {
        File source = new File(cachePath);
        File dest = new File(getPath("\\users\\", id, "\\", query));
        try {
            dest.mkdirs();
            dest = new File(dest, source.getName());
            Files.copy(source.toPath(), dest.toPath());
            historyWriter(id, query, dest);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return new File(dest, source.getName());
    }

    public void historyWriter(String user_id, String query, File savedFile) {
        File userFile = new File(getPath("\\users", "\\", user_id), "history.csv");
        try (PrintStream printer = new PrintStream(new FileOutputStream(userFile, true))) {
            if (!userFile.exists()) {
                userFile.createNewFile();
            }
            String history = LocalDate.now().toString() + "," + query + "," + savedFile.getAbsolutePath();
            printer.println(history);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean clearCache() {
        File cachePath = new File(getPath("cache"));
        try {
            FileUtils.cleanDirectory(cachePath);
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public void clearUserCache(String user_id) {
        File userCache = new File(getPath("users\\", user_id));
//        userCacheDeleter(userCache);
        try {
            FileUtils.forceDelete(userCache);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public Map<String, File[]> getFullCache(String query) {
        return getFullCache(query, "cache");
    }

    public Map<String, File[]> getFullUserCache(String id) {
        return getFullCache(null, "users\\" + id);
    }

    public Map<String, File[]> getFullCache(String query, String specifier) {
        String[] folders = query == null || query.isEmpty() ? getFNameCache(specifier) : new String[]{query};
        var map = new HashMap<String, File[]>();
        if (!specifier.equals("cache") || query != null) {
            for (String fName : folders) {
                File[] files = getByQueryFiles(specifier, fName);
                if (files != null) {
                    map.put(fName, files);
                }
            }
        } else {
            List<File> children = new ArrayList<>();
            for (String fName : folders) {
                children.addAll(Arrays.asList(getByQueryFiles(specifier, fName)));
            }
            map.put("gifs", children.toArray(new File[0]));
        }
        return map;
    }

    public File[] getByQueryFiles(String specifier, String query) {
        var file = new File(getPath(specifier, "\\", query));
        File[] files = file.listFiles();
        return files;
    }

    public String[] getFNameCache(String specifier) {
        var file = new File(getPath(specifier));
        String[] files = file.list();
        return files;
    }

    public File getHistory(String id) {
        return new File(getPath("users\\", id, "\\history.csv"));
    }

    public boolean deleteHistory(String id) {
        return new File(getPath("users\\", id, "\\history.csv")).delete();
    }

    public String getPATH() {
        return PATH;
    }

    private String getPath(String... args) {
        StringBuilder builder = new StringBuilder(PATH);
        for (String i : args) {
            builder.append(i);
        }
        return builder.toString();
    }
}
