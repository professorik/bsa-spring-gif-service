package com.bsa.boot.repository;

import com.bsa.boot.entity.Category;
import com.bsa.boot.entity.Game;
import org.springframework.stereotype.Repository;

import java.util.*;

// @Repository annotates classes at the persistence layer
@Repository
public class GameRepository {
    private static final Map<UUID, Game> games = new HashMap<>();

    public GameRepository() {
        seedData();
    }

    private void seedData() {
        var data = List.of(
            new Game(
                UUID.fromString("fc9704f3-4f9a-477c-af1b-377144c92cc3"),
                "Witcher",
                "Kill the monsters...",
                Category.RPG
            ),
            new Game(
                UUID.fromString("59bafff9-ca23-48a4-b429-dc469b7ba29c"),
                "Counter Strike",
                "Kill the terrorists...",
                Category.SHOOTER
            ),
            new Game(
                UUID.fromString("fee3146f-c986-44fa-ba15-515382fcba4c"),
                "Warcraft",
                "Kill anyone...",
                Category.STRATEGY
            )
        );

        games.putAll(
            Map.of(
                data.get(0).getId(), data.get(0),
                data.get(1).getId(), data.get(1),
                data.get(2).getId(), data.get(2)
            )
        );
    }

    public List<Game> getAll() {
        return new ArrayList<>(games.values());
    }

    public Optional<Game> findById(UUID id) {
        return Optional.ofNullable(games.get(id));
    }

    public void save(Game game) {
        games.put(game.getId(), game);
    }

    public void deleteById(UUID id) {
        games.remove(id);
    }
}
