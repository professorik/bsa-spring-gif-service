package com.bsa.boot.controller;

import com.bsa.boot.dto.GameDto;
import com.bsa.boot.dto.SaveGameRequestDto;
import com.bsa.boot.service.GameOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/games")
public final class ApiGameController {
    private final GameOperationService gameOperationService;

    @Autowired
    public ApiGameController(GameOperationService gameOperationService) {
        this.gameOperationService = gameOperationService;
    }

    @GetMapping
    public List<GameDto> queryGameCollection(@RequestParam Map<String, String> query) {
        System.out.println(query);

        return gameOperationService.getAll(); // Spring serializes List<GameDto> to JSON automatically using Jackson package
    }

    @GetMapping("/{id}")
    public GameDto queryGameById(@PathVariable String id) {
        return gameOperationService.findById(id);
    }

    @PostMapping
    public GameDto createGame(@RequestBody SaveGameRequestDto saveGameRequestDto) {
        return gameOperationService.add(saveGameRequestDto);
    }

    @PutMapping("/{id}")
    public GameDto updateGame(@PathVariable String id, @RequestBody SaveGameRequestDto saveGameRequestDto) {
        return gameOperationService.update(id, saveGameRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable String id) {
        gameOperationService.delete(id);
    }
}
