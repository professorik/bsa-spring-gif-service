package com.bsa.boot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public final class Game {
    private UUID id;
    private String name;
    private String description;
    private Category category;
}
