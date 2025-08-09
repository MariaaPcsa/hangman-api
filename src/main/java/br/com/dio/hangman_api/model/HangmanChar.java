package br.com.dio.hangman_api.model;

/**
 * Representa um caractere no jogo da forca,
 * com a letra, posição no desenho e se já foi revelado ao jogador.
 */
public class HangmanChar {

    private final char character;   // letra do jogo
    private boolean revealed;       // se já foi revelado
    private int position;           // posição no desenho da forca

    public HangmanChar(char character, int position) {
        this.character = character;
        this.position = position;
        this.revealed = false;
    }

    public char getCharacter() {
        return character;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
