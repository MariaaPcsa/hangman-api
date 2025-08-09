package br.com.dio.hangman_api.model;

import java.util.Objects;

public class WordWithHint {
    private final String word;
    private final String hint;

    public WordWithHint(String word, String hint) {
        if (word == null || word.isBlank()) {
            throw new IllegalArgumentException("A palavra não pode ser nula ou vazia.");
        }
        if (hint == null || hint.isBlank()) {
            throw new IllegalArgumentException("A dica não pode ser nula ou vazia.");
        }
        this.word = word.toUpperCase();
        this.hint = hint;
    }

    public String getWord() {
        return word;
    }

    public String getHint() {
        return hint;
    }

    @Override
    public String toString() {
        return "WordWithHint{" +
                "word='" + word + '\'' +
                ", hint='" + hint + '\'' +
                '}';
    }

    // Opcional: equals e hashCode para comparar objetos

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WordWithHint)) return false;
        WordWithHint that = (WordWithHint) o;
        return word.equals(that.word) && hint.equals(that.hint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, hint);
    }
}
