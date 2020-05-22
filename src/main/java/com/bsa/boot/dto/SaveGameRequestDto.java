package com.bsa.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class SaveGameRequestDto {
    private final String name;
    private final String description;
    private final String category;
}
