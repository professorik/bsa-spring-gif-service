package com.bsa.boot.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author professorik
 * @created 22/06/2021 - 09:36
 * @project boot
 */
@Getter
@Setter
public class History {
    private LocalDate date;
    private String query;
    private String gif;
}
