package com.bsa.boot.service;

import com.bsa.boot.dto.GameDto;
import com.bsa.boot.dto.SaveGameRequestDto;
import com.bsa.boot.entity.Category;
import com.bsa.boot.entity.Game;
import com.bsa.boot.exception.EntityNotFoundException;
import com.bsa.boot.exception.ValidationException;
import com.bsa.boot.repository.GameRepository;
import com.bsa.boot.util.GameToDtoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public final class GameOperationService {
    private final GameRepository gameRepository;
    private final GameToDtoMapper gameToDtoMapper;

    // not using @Autowired here
    public GameOperationService(GameRepository gameRepository, GameToDtoMapper gameToDtoMapper) {
        this.gameRepository = gameRepository;
        this.gameToDtoMapper = gameToDtoMapper;
    }

    public List<GameDto> getAll() {
        return gameToDtoMapper.mapCollection(gameRepository.getAll());
    }

    public GameDto findById(String id) throws EntityNotFoundException, ValidationException {
        try {
            var uuid = UUID.fromString(id);

            return gameToDtoMapper.map(
                gameRepository
                    .findById(uuid)
                    .orElseThrow(EntityNotFoundException::new)
            );
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Id value is invalid");
        }
    }

    public GameDto add(SaveGameRequestDto saveGameRequestDto) throws ValidationException {
        try {
            var category = Category.valueOf(saveGameRequestDto.getCategory().toUpperCase());
            var game = new Game(
                UUID.randomUUID(),
                saveGameRequestDto.getName(),
                saveGameRequestDto.getDescription(),
                category
            );

            gameRepository.save(game);

            return gameToDtoMapper.map(game);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Category value is invalid");
        }
    }

    public GameDto update(
        String id,
        SaveGameRequestDto saveGameRequestDto) throws ValidationException, EntityNotFoundException {
        try {
            var uuid = UUID.fromString(id);
            var category = Category.valueOf(saveGameRequestDto.getCategory().toUpperCase());
            var game = gameRepository
                .findById(uuid)
                .orElseThrow(EntityNotFoundException::new);

            game.setCategory(category);
            game.setName(saveGameRequestDto.getName());
            game.setDescription(saveGameRequestDto.getDescription());

            gameRepository.save(game);

            return gameToDtoMapper.map(game);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(ex.getMessage());
        }
    }

    public void delete(String id) throws ValidationException {
        try {
            var uuid = UUID.fromString(id);

            if (gameRepository.findById(uuid).isEmpty()) {
                throw new EntityNotFoundException();
            }

            gameRepository.deleteById(uuid);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Id value is invalid");
        }
    }
}
