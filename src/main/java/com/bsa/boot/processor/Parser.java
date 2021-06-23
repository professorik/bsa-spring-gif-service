package com.bsa.boot.processor;

import com.bsa.boot.dto.CacheDTO;
import com.bsa.boot.dto.History;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * @author professorik
 * @created 22/06/2021 - 09:52
 * @project boot
 */
@Service
public class Parser {
    public CacheDTO[] parseFullCache(Map<String, File[]> queriedCache) {
        Stack<CacheDTO> cacheDTO = new Stack<>();
        for (Map.Entry<String, File[]> path : queriedCache.entrySet()) {
            ArrayList<String> fnames = new ArrayList<>();
            for (File file : queriedCache.get(path.getKey())) {
                if (file.listFiles() != null) {
                    for (File gif : file.listFiles()) {
                        fnames.add(gif.getAbsolutePath());
                    }
                } else {
                    fnames.add(file.getAbsolutePath());
                }
            }
            cacheDTO.push(new CacheDTO());
            cacheDTO.peek().setQuery(path.getKey());
            cacheDTO.peek().setGifs(fnames.toArray(new String[0]));
        }
        return cacheDTO.toArray(new CacheDTO[0]);
    }

    public String[] parseOnlyFiles(Map<String, File[]> queriedCache) {
        var cacheDto = parseFullCache(queriedCache);
        return cacheDto[0].getGifs();
    }

    public ArrayList<History> historyParser(File file) {
        ArrayList<History> historyList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                var history = new History();
                String[] result = line.split(",");
                history.setDate(LocalDate.parse(result[0]));
                history.setQuery(result[1]);
                history.setGif(result[2]);
                historyList.add(history);
            }
        } catch (IOException ex) {
            System.err.println("It isn't possible to parse this...");
        }
        return historyList;
    }

    public History[] parseHistory(File file) {
        ArrayList<History> resultList = historyParser(file);
        return resultList.toArray(new History[0]);
    }
}
