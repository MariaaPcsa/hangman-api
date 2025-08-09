package br.com.dio.hangman_api.gui; // Sugestão: mudar para um pacote GUI separado

import br.com.dio.hangman_api.model.HangmanGame;
import br.com.dio.hangman_api.model.HangmanChar;
import br.com.dio.hangman_api.model.HangmanGameStatus;
import br.com.dio.hangman_api.model.WordWithHint;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class HangmanGUI extends JFrame {

    private JComboBox<String> categoryComboBox;
    private JLabel hintLabel;
    private JLabel wordLabel;
    private JLabel messageLabel;
    private JTextField inputField;
    private JButton guessButton;
    private JButton restartButton;

    private HangmanGame game;
    private WordWithHint currentWord;
    private Map<String, List<WordWithHint>> categories;

    public HangmanGUI() {
        setTitle("Jogo da Forca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        initCategories();
        initComponents();
    }

    private void initCategories() {
        categories = new HashMap<>();
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
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        JPanel bottomPanel = new JPanel(new FlowLayout());

        // Categoria
        categoryComboBox = new JComboBox<>(categories.keySet().toArray(new String[0]));
        categoryComboBox.setSelectedIndex(-1);
        JButton startButton = new JButton("Iniciar Jogo");

        topPanel.add(new JLabel("Categoria:"));
        topPanel.add(categoryComboBox);
        topPanel.add(startButton);

        // Dica
        hintLabel = new JLabel("Dica: ");
        // Palavra oculta
        wordLabel = new JLabel("Palavra: ");
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        // Mensagem de status
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.BLUE);

        // Entrada de letra
        inputField = new JTextField(5);
        guessButton = new JButton("Adivinhar");

        centerPanel.add(hintLabel);
        centerPanel.add(wordLabel);
        centerPanel.add(messageLabel);
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Letra: "));
        inputPanel.add(inputField);
        inputPanel.add(guessButton);
        centerPanel.add(inputPanel);

        restartButton = new JButton("Reiniciar");
        bottomPanel.add(restartButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);

        // Eventos
        startButton.addActionListener(e -> startGame());
        guessButton.addActionListener(e -> guessLetter());
        restartButton.addActionListener(e -> resetGame());

        setInputEnabled(false);
    }

    private void startGame() {
        int idx = categoryComboBox.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, escolha uma categoria!");
            return;
        }
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        List<WordWithHint> words = categories.get(selectedCategory);
        Random rand = new Random();
        currentWord = words.get(rand.nextInt(words.size()));

        List<HangmanChar> secretChars = currentWord.getWord().chars()
                .mapToObj(c -> new HangmanChar((char) c, 0))
                .collect(Collectors.toList());

        game = new HangmanGame(secretChars);

        hintLabel.setText("Dica: " + currentWord.getHint());
        updateWordLabel();
        messageLabel.setText(" ");

        setInputEnabled(true);
    }

    private void guessLetter() {
        if (game == null || game.getStatus() != HangmanGameStatus.PENDING) {
            return;
        }
        String input = inputField.getText().trim();
        if (input.isEmpty() || !Character.isLetter(input.charAt(0))) {
            JOptionPane.showMessageDialog(this, "Digite uma letra válida.");
            inputField.setText("");
            return;
        }
        char ch = Character.toUpperCase(input.charAt(0));
        try {
            game.inputCharacter(ch);
            updateWordLabel();
            if (game.getStatus() == HangmanGameStatus.WIN) {
                messageLabel.setText("🎉 Parabéns! Você ganhou!");
                setInputEnabled(false);
            } else if (game.getStatus() == HangmanGameStatus.LOSE) {
                messageLabel.setText("😞 Você perdeu! A palavra era: " + currentWord.getWord());
                updateWordLabel(true);
                setInputEnabled(false);
            } else {
                messageLabel.setText("Continue tentando...");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        } finally {
            inputField.setText("");
            inputField.requestFocus();
        }
    }

    private void updateWordLabel() {
        updateWordLabel(false);
    }

    private void updateWordLabel(boolean revealAll) {
        StringBuilder display = new StringBuilder();
        for (HangmanChar hc : game.getSecretChars()) {
            if (hc.isRevealed() || revealAll) {
                display.append(hc.getCharacter());
            } else {
                display.append("_");
            }
            display.append(" ");
        }
        wordLabel.setText(display.toString().trim());
    }

    private void setInputEnabled(boolean enabled) {
        inputField.setEnabled(enabled);
        guessButton.setEnabled(enabled);
    }

    private void resetGame() {
        game = null;
        currentWord = null;
        hintLabel.setText("Dica: ");
        wordLabel.setText("Palavra: ");
        messageLabel.setText(" ");
        setInputEnabled(false);
        categoryComboBox.setSelectedIndex(-1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HangmanGUI gui = new HangmanGUI();
            gui.setVisible(true);
        });
    }
}
