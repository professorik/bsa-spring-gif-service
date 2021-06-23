package com.bsa.boot.util;

import org.springframework.stereotype.Component;

/**
 * @author professorik
 * @created 22/06/2021 - 09:12
 * @project boot
 */
@Component
public class User {
    /**
     * @param id
     * @return true if id has characters in
     * square brackets or id is empty or
     * comprises whitespaces.
     */
    public boolean validate(String id) {
        return id.isBlank() || id.isEmpty() || id.matches(".*[|*?<>:/\"].*");
    }
}
