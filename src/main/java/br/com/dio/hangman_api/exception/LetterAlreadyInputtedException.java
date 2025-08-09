package br.com.dio.hangman_api.exception;

public class LetterAlreadyInputtedException extends RuntimeException {

  public LetterAlreadyInputtedException(String message) {
    super(message);
  }

  public LetterAlreadyInputtedException() {
    super("A letra já foi inserida anteriormente.");
  }
}
