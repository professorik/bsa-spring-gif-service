package com.bsa.boot.util;

import com.bsa.boot.dto.GameDto;
import com.bsa.boot.entity.Game;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public final class GameToDtoMapper {
    public GameDto map(Game game) {
        return new GameDto(
            game.getId().toString(),
            game.getName(),
            game.getDescription(),
            game.getCategory().toString().toLowerCase()
        );
    }

    public List<GameDto> mapCollection(List<Game> games) {
        return games.stream().map(this::map).collect(Collectors.toList());
    }
}
