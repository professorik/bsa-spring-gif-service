package com.bsa.boot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author professorik
 * @created 22/06/2021 - 09:30
 * @project boot
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GifFile implements Serializable {
    private Object data;
}