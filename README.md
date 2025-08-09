
🧠 Hangman API
Uma API RESTful para o jogo da forca, desenvolvida em Java com Spring Boot. Permite jogar via endpoints HTTP, ideal para integração com frontends ou como base para projetos educacionais.

🔗 Acesse o repositório no GitHub

📦 Tecnologias
Java (72%)

JavaScript (16%)

CSS/HTML (7%/4%)

🚀 Funcionalidades
✅ Iniciar um novo jogo

🔤 Enviar tentativas de letras

🧩 Verificar progresso do jogo

🏆 Obter pontuação final

⚙️ Como rodar localmente
Clone o repositório:


git clone https://github.com/MariaaPcsa/hangman-api.git
cd hangman-api
Compile e execute com Maven:


./mvnw spring-boot:run
Acesse a API em: http://localhost:8080

📚 Endpoints principais
GET /api/game/start – Inicia um novo jogo

POST /api/game/guess/{letra} – Envia uma tentativa de letra

GET /api/game/status – Verifica o status atual do jogo
