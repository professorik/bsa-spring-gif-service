package com.bsa.boot.entity;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author professorik
 * @created 22/06/2021 - 08:58
 * @project boot
 */
@Component
public class Cache {
    private Map<String, Map<String, ArrayList<String>>> history;

    private Cache() {
        history = new HashMap<>();
    }

    public void updateCache(String user_id, String query, String gifId) {
        ArrayList<String> tempList = new ArrayList<>();
        if (history.containsKey(user_id)) {
            if (history.get(user_id).get(query) != null) {
                tempList = history.get(user_id).get(query);
                tempList.add(tempList.size(), gifId);
            } else {
                tempList.add(gifId);
            }
            history.get(user_id).put(query, tempList);
        } else {
            HashMap<String, ArrayList<String>> userMap = new HashMap<>();
            tempList.add(gifId);
            userMap.put(query, tempList);
            history.put(user_id, userMap);
        }
    }

    public Optional<String> getGif(String user_id, String query) {
        if (history.containsKey(user_id)) {
            if (history.get(user_id).get(query) != null) {
                ArrayList<String> queriedList = history.get(user_id).get(query);
                return Optional.ofNullable(queriedList.get(new Random().nextInt(queriedList.size())));
            }
        }
        return Optional.empty();
    }

    public void resetUser(String user_id) {
        this.history.remove(user_id);
    }

    public void resetUser(String user_id, String query) {
        this.history.get(user_id).remove(query);
    }
}
