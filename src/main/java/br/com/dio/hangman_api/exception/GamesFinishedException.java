package br.com.dio.hangman_api.exception;

/**
 * Exceção personalizada para indicar que o jogo já foi finalizado
 * e que não devem ser aceitas mais jogadas ou ações relacionadas.
 * Estende RuntimeException, ou seja, é uma exceção não verificada (unchecked).
 */
public class GamesFinishedException extends RuntimeException {

    /**
     * Construtor que recebe uma mensagem de erro personalizada.
     *
     * @param message Mensagem que será exibida quando a exceção for lançada.
     */
    public GamesFinishedException(String message) {
        super(message);}
}