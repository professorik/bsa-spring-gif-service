package com.bsa.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author professorik
 * @created 22/06/2021 - 09:24
 * @project boot
 */
@AllArgsConstructor
@Getter
@Setter
public class Query {
    private String query;
    private Boolean force;
}
