package com.bsa.boot.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * @author professorik
 * @created 22/06/2021 - 09:38
 * @project boot
 */
@Component
@Getter
@Setter
public class CacheDTO {
    private String query;
    private String[] gifs;
}
