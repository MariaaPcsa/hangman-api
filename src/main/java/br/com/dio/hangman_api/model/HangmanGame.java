package br.com.dio.hangman_api.model;

import br.com.dio.hangman_api.exception.GamesFinishedException;
import br.com.dio.hangman_api.exception.LetterAlreadyInputtedException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Representa o jogo da forca, gerenciando letras, falhas e desenho do boneco.
 */
public class HangmanGame {

    private static final int MAX_FAILS = 6;

    private final int lineSize;
    private final int hangmanInitialSize;
    private final LinkedList<HangmanChar> hangmanParts; // Partes do boneco da forca
    private String hangman;                              // Representação textual do jogo da forca
    private HangmanGameStatus status;
    private final List<HangmanChar> characters;         // Letras da palavra secreta
    private final List<Character> failAttempts = new ArrayList<>(); // Letras erradas já tentadas

    public HangmanGame(final List<HangmanChar> characters) {
        this.status = HangmanGameStatus.PENDING;

        String whiteSpace = " ".repeat(characters.size());
        String characterSpace = "-".repeat(characters.size());

        this.lineSize = 10 + whiteSpace.length(); // Ajuste conforme necessário

        buildHangmanGameDesign(whiteSpace, characterSpace);
        this.hangmanInitialSize = hangman.length();
        this.characters = setCharacterSpacePositionGame(characters);
        this.hangmanParts = new LinkedList<>(buildHangmanPartsPositions());
    }

    public void inputCharacter(final char character) {
        if (this.status != HangmanGameStatus.PENDING) {
            throw new GamesFinishedException(
                    this.status == HangmanGameStatus.WIN ? "Parabéns, você ganhou!" : "Você perdeu, tente novamente."
            );
        }

        if (failAttempts.contains(character) || characters.stream()
                .anyMatch(c -> c.getCharacter() == character && c.isRevealed())) {
            throw new LetterAlreadyInputtedException("Letra '" + character + "' já foi informada anteriormente");
        }

        List<HangmanChar> found = characters.stream()
                .filter(c -> c.getCharacter() == character)
                .toList();

        if (found.isEmpty()) {
            failAttempts.add(character);

            if (failAttempts.size() >= MAX_FAILS) {
                this.status = HangmanGameStatus.LOSE;
            }

            if (!hangmanParts.isEmpty()) {
                rebuildHangman(hangmanParts.removeFirst());
            }
            return;
        }

        found.forEach(c -> c.setRevealed(true));

        if (characters.stream().allMatch(HangmanChar::isRevealed)) {
            this.status = HangmanGameStatus.WIN;
        }

        rebuildHangman(found.toArray(new HangmanChar[0]));
    }

    private List<HangmanChar> buildHangmanPartsPositions() {
        final int HEAD_LINE = 3;
        final int BODY_LINE = 4;
        final int LEGS_LINE = 5;

        return List.of(
                new HangmanChar('O', this.lineSize * HEAD_LINE + 6),
                new HangmanChar('|', this.lineSize * BODY_LINE + 6),
                new HangmanChar('/', this.lineSize * BODY_LINE + 5),
                new HangmanChar('\\', this.lineSize * BODY_LINE + 7),
                new HangmanChar('/', this.lineSize * LEGS_LINE + 5),
                new HangmanChar('\\', this.lineSize * LEGS_LINE + 7)
        );
    }

    private List<HangmanChar> setCharacterSpacePositionGame(final List<HangmanChar> characters) {
        final int LINE_LETTER = 6;
        for (int i = 0; i < characters.size(); i++) {
            characters.get(i).setPosition(this.lineSize * LINE_LETTER + 9 + i); // 9 é tamanho inicial da linha de letras
        }
        return characters;
    }

    private void rebuildHangman(final HangmanChar... hangmanChars) {
        StringBuilder hangmanBuilder = new StringBuilder(this.hangman);
        for (HangmanChar h : hangmanChars) {
            hangmanBuilder.setCharAt(h.getPosition(), h.getCharacter());
        }

        String failMessage = failAttempts.isEmpty() ? "" : " Tentativas: " + failAttempts;
        this.hangman = hangmanBuilder.substring(0, hangmanInitialSize) + failMessage;
    }

    public void buildHangmanGameDesign(final String whiteSpace, final String characterSpace) {
        this.hangman = "-----" + whiteSpace + System.lineSeparator() +
                " |     |" + whiteSpace + System.lineSeparator() +
                " |     |" + whiteSpace + System.lineSeparator() +
                " |      " + whiteSpace + System.lineSeparator() +
                " |      " + whiteSpace + System.lineSeparator() +
                " |      " + whiteSpace + System.lineSeparator() +
                " |      " + whiteSpace + System.lineSeparator() +
                " ====== " + characterSpace + System.lineSeparator();
    }

    public String getHangman() {
        return hangman;
    }

    public HangmanGameStatus getStatus() {
        return status;
    }

    public List<Character> getFailAttempts() {
        return failAttempts;
    }

    public List<HangmanChar> getSecretChars() {
        return characters;
    }

    @Override
    public int hashCode() {
        return hangman != null ? hangman.hashCode() : 0;
    }

    @Override
    public String toString() {
        return hangman;
    }
}
