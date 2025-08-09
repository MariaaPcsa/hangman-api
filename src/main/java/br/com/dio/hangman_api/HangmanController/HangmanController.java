package br.com.dio.hangman_api.HangmanController;

import br.com.dio.hangman_api.model.HangmanGame;
import br.com.dio.hangman_api.model.HangmanChar;
import br.com.dio.hangman_api.model.HangmanGameStatus;
import br.com.dio.hangman_api.model.WordWithHint;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/hangman")
public class HangmanController {

    private final Map<String, HangmanGame> games = new HashMap<>();
    private final Map<String, WordWithHint> words = new HashMap<>();

    private final Map<String, List<WordWithHint>> categories = new HashMap<>() {{
        put("Animais", List.of(
                new WordWithHint("CACHORRO", "Animal doméstico que late"),
                new WordWithHint("GATO", "Animal doméstico que gosta de dormir"),
                new WordWithHint("ELEFANTE", "O maior animal terrestre"),
                new WordWithHint("TIGRE", "Felino listrado e feroz"),
                new WordWithHint("PANDA", "Urso branco e preto que come bambu")
        ));
        put("Frutas", List.of(
                new WordWithHint("MAÇÃ", "Fruta vermelha ou verde, símbolo de saúde"),
                new WordWithHint("BANANA", "Fruta amarela e curvada"),
                new WordWithHint("LARANJA", "Fruta cítrica rica em vitamina C"),
                new WordWithHint("MELANCIA", "Fruta grande, vermelha e cheia de sementes"),
                new WordWithHint("ABACAXI", "Fruta com casca espinhosa e sabor doce")
        ));
        put("Países", List.of(
                new WordWithHint("BRASIL", "País do futebol e do carnaval"),
                new WordWithHint("ARGENTINA", "País vizinho famoso pelo tango"),
                new WordWithHint("CHILE", "País longo e estreito na América do Sul"),
                new WordWithHint("FRANÇA", "País conhecido pela Torre Eiffel"),
                new WordWithHint("JAPÃO", "País das cerejeiras e tecnologia")
        ));
    }};

    @GetMapping("/categories")
    public Map<String, List<WordWithHint>> getCategories() {
        return categories;
    }

    @PostMapping("/start/{category}")
    public Map<String, String> startGame(@PathVariable String category) {
        List<WordWithHint> wordList = categories.get(category);
        if (wordList == null || wordList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria inválida ou sem palavras.");
        }

        Random random = new Random();
        WordWithHint currentWord = wordList.get(random.nextInt(wordList.size()));

        List<HangmanChar> chars = new ArrayList<>();
        for (char c : currentWord.getWord().toCharArray()) {
            chars.add(new HangmanChar(c, 0));
        }
        HangmanGame newGame = new HangmanGame(chars);

        String gameId = UUID.randomUUID().toString();
        games.put(gameId, newGame);
        words.put(gameId, currentWord);

        return Map.of(
                "gameId", gameId,
                "hint", currentWord.getHint(),
                "message", "Jogo iniciado! Dica: " + currentWord.getHint()
        );
    }

    @PostMapping("/guess/{gameId}/{letter}")
    public Map<String, Object> guessLetter(@PathVariable String gameId, @PathVariable char letter) {
        HangmanGame currentGame = games.get(gameId);
        WordWithHint currentWord = words.get(gameId);

        if (currentGame == null || currentWord == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogo não encontrado. Inicie um novo jogo.");
        }
        if (currentGame.getStatus() != HangmanGameStatus.PENDING) {
            return Map.of("message", "Jogo finalizado.", "status", currentGame.getStatus());
        }
        try {
            currentGame.inputCharacter(Character.toUpperCase(letter));
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }

        return getGameState(gameId);
    }

    @GetMapping("/state/{gameId}")
    public Map<String, Object> getGameState(@PathVariable String gameId) {
        HangmanGame currentGame = games.get(gameId);
        WordWithHint currentWord = words.get(gameId);

        if (currentGame == null || currentWord == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogo não encontrado.");
        }

        StringBuilder display = new StringBuilder();
        for (HangmanChar hc : currentGame.getSecretChars()) {
            display.append(hc.isRevealed() ? hc.getCharacter() : "_").append(" ");
        }

        return Map.of(
                "word", display.toString().trim(),
                "hint", currentWord.getHint(),
                "status", currentGame.getStatus(),
                "failAttempts", currentGame.getFailAttempts()
        );
    }
}
