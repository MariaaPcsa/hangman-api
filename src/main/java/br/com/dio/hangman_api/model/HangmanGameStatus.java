package br.com.dio.hangman_api.model;

/**
 * Enum HangmanGameStatus
 *
 * Representa os possíveis estados do jogo da forca.
 * Utilizamos 'enum' (enumeration) para definir um conjunto fixo de constantes
 * que representam as fases finais ou intermediárias de um jogo.
 */
public enum HangmanGameStatus {

    /**
     * PENDING:
     * Estado inicial ou intermediário do jogo.
     * Significa que o jogador ainda está tentando adivinhar a palavra
     * e o jogo não foi finalizado.
     */
    PENDING,

    /**
     * WIN:
     * Estado em que o jogador venceu o jogo.
     * Isso acontece quando todas as letras foram reveladas
     * antes do limite de tentativas acabar.
     */
    WIN,

    /**
     * LOSE:
     * Estado em que o jogador perdeu o jogo.
     * Isso acontece quando o jogador atinge o número máximo de erros permitidos
     * sem conseguir adivinhar a palavra.
     */
    LOSE
}
