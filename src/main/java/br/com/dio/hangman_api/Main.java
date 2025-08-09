package br.com.dio.hangman_api;

import br.com.dio.hangman_api.model.WordWithHint;
import br.com.dio.hangman_api.model.HangmanChar;
import br.com.dio.hangman_api.model.HangmanGame;
import br.com.dio.hangman_api.model.HangmanGameStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);

		Scanner scanner = new Scanner(System.in);
		HangmanGame game = null;

		// Mapa de categorias com lista de palavras e dicas
		Map<String, List<WordWithHint>> categories = new HashMap<>();
		categories.put("Animais", List.of(
				new WordWithHint("CACHORRO", "Animal doméstico que late"),
				new WordWithHint("GATO", "Animal doméstico que gosta de dormir"),
				new WordWithHint("ELEFANTE", "O maior animal terrestre"),
				new WordWithHint("TIGRE", "Felino listrado e feroz"),
				new WordWithHint("PANDA", "Urso branco e preto que come bambu")
		));
		categories.put("Frutas", List.of(
				new WordWithHint("MAÇÃ", "Fruta vermelha ou verde, símbolo de saúde"),
				new WordWithHint("BANANA", "Fruta amarela e curvada"),
				new WordWithHint("LARANJA", "Fruta cítrica rica em vitamina C"),
				new WordWithHint("MELANCIA", "Fruta grande, vermelha e cheia de sementes"),
				new WordWithHint("ABACAXI", "Fruta com casca espinhosa e sabor doce")
		));
		categories.put("Países", List.of(
				new WordWithHint("BRASIL", "País do futebol e do carnaval"),
				new WordWithHint("ARGENTINA", "País vizinho famoso pelo tango"),
				new WordWithHint("CHILE", "País longo e estreito na América do Sul"),
				new WordWithHint("FRANÇA", "País conhecido pela Torre Eiffel"),
				new WordWithHint("JAPÃO", "País das cerejeiras e tecnologia")
		));

		while (true) {
			try {
				System.out.println("=== MENU JOGO DA FORCA ===");
				System.out.println("1 - Jogar");
				System.out.println("2 - Regras");
				System.out.println("3 - Reiniciar jogo");
				System.out.println("4 - Sair");
				System.out.print("Escolha uma opção: ");

				String option = scanner.nextLine().trim();

				switch (option) {
					case "1":
						// Seleção da categoria
						List<String> catList = new ArrayList<>(categories.keySet());
						System.out.println("Escolha uma categoria:");
						for (int i = 0; i < catList.size(); i++) {
							System.out.printf("%d - %s%n", i + 1, catList.get(i));
						}
						System.out.print("Digite o número da categoria: ");

						String catOption = scanner.nextLine().trim();
						int catIndex;
						try {
							catIndex = Integer.parseInt(catOption);
							if (catIndex < 1 || catIndex > catList.size()) {
								System.out.println("Categoria inválida. Voltando ao menu.");
								break;
							}
						} catch (NumberFormatException e) {
							System.out.println("Entrada inválida. Voltando ao menu.");
							break;
						}

						String chosenCategory = catList.get(catIndex - 1);
						List<WordWithHint> words = categories.get(chosenCategory);

						// Escolhe palavra aleatória da categoria
						WordWithHint chosenWord = words.get(new Random().nextInt(words.size()));

						System.out.println("DICA: " + chosenWord.getHint());

						// Inicializa jogo com a palavra escolhida
						List<HangmanChar> secretChars = new ArrayList<>();
						for (char c : chosenWord.getWord().toCharArray()) {
							secretChars.add(new HangmanChar(c, 0));
						}
						game = new HangmanGame(secretChars);

						System.out.println("Categoria escolhida: " + chosenCategory);

						// Loop do jogo enquanto estiver pendente
						while (game.getStatus() == HangmanGameStatus.PENDING) {
							System.out.println(game.getHangman());
							System.out.print("Digite uma letra: ");
							String input = scanner.nextLine().trim();

							if (input.isEmpty()) {
								System.out.println("Por favor, digite uma letra.");
								continue;
							}

							char ch = Character.toUpperCase(input.charAt(0));
							try {
								game.inputCharacter(ch);
							} catch (IllegalStateException e) {
								System.out.println("[Aviso] " + e.getMessage());
								break;
							} catch (Exception e) {
								System.out.println("[Erro inesperado] Não foi possível processar a letra: " + ch);
								System.out.println("Detalhes: " + e);
							}
						}

						System.out.println(game.getHangman());

						if (game.getStatus() == HangmanGameStatus.WIN) {
							System.out.println("Parabéns! Você ganhou!");
						} else if (game.getStatus() == HangmanGameStatus.LOSE) {
							System.out.println("Você perdeu! A palavra era: " + chosenWord.getWord());
						}
						break;

					case "2":
						System.out.println("=== REGRAS DO JOGO DA FORCA ===");
						System.out.println("- Tente adivinhar a palavra secreta letra por letra.");
						System.out.println("- Você tem um número limitado de tentativas.");
						System.out.println("- Se errar muitas vezes, perde o jogo.");
						System.out.println("- Boa sorte!");
						break;

					case "3":
						game = null;
						System.out.println("Jogo reiniciado.");
						break;

					case "4":
						System.out.println("Saindo do jogo. Até a próxima!");
						scanner.close();
						return;

					default:
						System.out.println("Opção inválida. Tente novamente.");
						break;
				}
			} catch (Exception e) {
				System.out.println("[Erro fatal] Ocorreu um erro inesperado: " + e.getMessage());
				e.printStackTrace();
			}

			System.out.println();
		}
	}
}
