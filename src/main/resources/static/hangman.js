// Categorias e palavras (exemplo simples)
const categories = {
  Animais: [
    { word: "CACHORRO", hint: "Animal doméstico que late" },
    { word: "GATO", hint: "Animal doméstico que gosta de dormir" },
    { word: "ELEFANTE", hint: "O maior animal terrestre" }
  ],
  Frutas: [
    { word: "MAÇÃ", hint: "Fruta vermelha ou verde, símbolo de saúde" },
    { word: "BANANA", hint: "Fruta amarela e curvada" }
  ],
  Países: [
    { word: "BRASIL", hint: "País do futebol e do carnaval" },
    { word: "ARGENTINA", hint: "País vizinho famoso pelo tango" },
    { word: "CHILE", hint: "País longo e estreito na América do Sul" },
    { word: "FRANÇA", hint: "País conhecido pela Torre Eiffel" },
    { word: "JAPÃO", hint: "País das cerejeiras e tecnologia" }
  ]
};

// Estado do jogo
let selectedCategory = null;
let currentWord = null;
let currentHint = null;
let guessedLetters = [];
let maxErrors = 6;
let errors = 0;

// Elementos DOM
const menuDiv = document.getElementById("menu");
const rulesDiv = document.getElementById("rules");
const playDiv = document.getElementById("play");
const gameAreaDiv = document.getElementById("gameArea");

const categorySelect = document.getElementById("categorySelect");
const startBtn = document.getElementById("startBtn");
const hintP = document.getElementById("hint");
const wordDisplayP = document.getElementById("wordDisplay");
const letterInput = document.getElementById("letterInput");
const guessBtn = document.getElementById("guessBtn");
const gameMessageP = document.getElementById("gameMessage");
const hangmanImage = document.getElementById("hangmanImage");

// Preenche o select de categorias no carregamento
function populateCategories() {
  categorySelect.innerHTML = "";
  const defaultOption = document.createElement("option");
  defaultOption.value = "";
  defaultOption.textContent = "-- Selecione uma categoria --";
  categorySelect.appendChild(defaultOption);

  for (const cat in categories) {
    const option = document.createElement("option");
    option.value = cat;
    option.textContent = cat;
    categorySelect.appendChild(option);
  }
}

// Mostra o menu principal
function showMenu() {
  menuDiv.style.display = "block";
  rulesDiv.style.display = "none";
  playDiv.style.display = "none";
  resetGameState();
}

// Mostra as regras
function showRules() {
  menuDiv.style.display = "none";
  rulesDiv.style.display = "block";
  playDiv.style.display = "none";
}

// Mostra a tela de jogar
function showPlay() {
  menuDiv.style.display = "none";
  rulesDiv.style.display = "none";
  playDiv.style.display = "block";
  gameAreaDiv.style.display = "none";
  populateCategories();
  gameMessageP.textContent = "";
  hangmanImage.src = "hangman0.png";
}

// Voltar para o menu do jogo
function backToMenu() {
  showMenu();
}

// Reiniciar o jogo (mesma função de reset)
function restartGame() {
  resetGameState();
  showMenu();
}

// Reinicia variáveis do jogo
function resetGameState() {
  selectedCategory = null;
  currentWord = null;
  currentHint = null;
  guessedLetters = [];
  errors = 0;
  hangmanImage.src = "hangman0.png";
  wordDisplayP.textContent = "";
  hintP.textContent = "";
  gameMessageP.textContent = "";
  letterInput.value = "";
  letterInput.disabled = false;
  guessBtn.disabled = false;
  gameAreaDiv.style.display = "none";
  categorySelect.value = "";
}

// Iniciar o jogo após escolher categoria
function startGame() {
  selectedCategory = categorySelect.value;
  if (!selectedCategory) {
    alert("Escolha uma categoria!");
    return;
  }

  // Escolhe palavra aleatória
  const words = categories[selectedCategory];
  const randomIndex = Math.floor(Math.random() * words.length);
  currentWord = words[randomIndex].word.toUpperCase();
  currentHint = words[randomIndex].hint;

  guessedLetters = [];
  errors = 0;

  hintP.textContent = "Dica: " + currentHint;
  gameMessageP.textContent = "";
  updateWordDisplay();
  hangmanImage.src = "hangman0.png";

  gameAreaDiv.style.display = "block";
  letterInput.value = "";
  letterInput.focus();
}

// Atualiza o display da palavra oculta
function updateWordDisplay() {
  let display = "";
  for (const ch of currentWord) {
    if (guessedLetters.includes(ch)) {
      display += ch + " ";
    } else if (ch === " ") {
      display += "  "; // espaço para palavras compostas
    } else {
      display += "_ ";
    }
  }
  wordDisplayP.textContent = display.trim();
}

// Processa a letra digitada
function guessLetter() {
  const input = letterInput.value.toUpperCase();
  if (!input || input.length !== 1 || !input.match(/^[A-ZÀ-Ÿ]$/)) {
    alert("Digite uma letra válida.");
    letterInput.value = "";
    letterInput.focus();
    return;
  }

  if (guessedLetters.includes(input)) {
    alert("Você já tentou essa letra.");
    letterInput.value = "";
    letterInput.focus();
    return;
  }

  guessedLetters.push(input);

  // Mostrar a letra digitada na mensagem
  gameMessageP.textContent = `Você digitou: ${input}`;

  if (currentWord.includes(input)) {
    updateWordDisplay();
    if (wordDisplayP.textContent.replace(/\s/g, "") === currentWord.replace(/\s/g, "")) {
      gameMessageP.textContent = `Você digitou: ${input} — Parabéns! Você ganhou! 🎉`;
      letterInput.disabled = true;
      guessBtn.disabled = true;
    } else {
      gameMessageP.textContent += " — Boa! Continue tentando...";
    }
  } else {
    errors++;
    hangmanImage.src = `hangman${errors}.png`;
    if (errors >= maxErrors) {
      gameMessageP.textContent = `Você digitou: ${input} — Você perdeu! A palavra era: ${currentWord}`;
      updateWordDisplay(); // mostra palavra completa
      letterInput.disabled = true;
      guessBtn.disabled = true;
    } else {
      gameMessageP.textContent += ` — Letra incorreta! Erros: ${errors} de ${maxErrors}`;
    }
  }

  letterInput.value = "";
  letterInput.focus();
}

// Funções ligadas a botões da interface (usar no HTML)
window.showPlay = showPlay;
window.showRules = showRules;
window.backToMenu = backToMenu;
window.restartGame = restartGame;

// Eventos dos botões Start e Guess
startBtn.addEventListener("click", startGame);
guessBtn.addEventListener("click", guessLetter);

// Inicializa o menu ao carregar página
showMenu();
