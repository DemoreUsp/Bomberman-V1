Entrega 02/06 via Tidia  “Upload Projeto”
Construção do jogo eletrônico Robbo (https://www.youtube.com/watch?v=d2KBt1zTABI&), ou um jogo que você conheça, pode
ser qualquer jogo que possa ser implementado com lógica tile-based, e que possua scrolling.
+Grupos de até 3 alunos!
+Implemente as 5 primeiras fases do jogo; há vídeos de gameplay no youtube;
+Não é preciso implementar tela de início, pontuação, e tela de status – qualquer informação necessária ao jogador pode
ser um simples println;
+Utilize o projeto Netbeans protótipo fornecido na disciplina.
No protótipo, há uma classe abstrata Personagem. Esta classe, por ser abstrata, não pode ser instanciada, mas contém tudo aquilo
que é comum às outras classes. Nesta classe estão definidos os métodos abstratos que deverão ser sobrescritos nas subclasses
visando polimorfismo. Como exemplos temos os métodos desenho(), move(), pegaPosicao(), atira(), etc
Crie as subclasses de Personagem; um possível projeto é ter subclasses Estatico e Animado, e subclasses subsequentes dentro
destas categorias.
Requisitos:
1) o jogo deve funcionar de acordo com o original em suas 4 primeiras fases, a aparência não precisa ser a mesma.
Sugere-se criar screenshots do jogo original e usar um editor simples como o paint brush para extrair partes da imagem (como os
personagens e o background) e reproduzir sua aparência original.
2) estude o jogo antes de implementar, e implemente tudo: personagens estáticos de interação (cubos, setas, itens etc), e elementos
animados que se movem (robôs, monstros, etc).
3) controle do jogo – número de vidas, início, fim, reinício, passagem para uma nova fase.
Opcional: ao se terminar a 4ª. fase, coloque uma mensagem de fim de jogo com o nome dos criadores.
4) Crie uma classe Fase que será composta por um conjunto de personagens desenhados no início de cada nova Fase; dessa
maneira, trocar de fase significa apenas substituir o objeto fase atual.
5) Use comandos de teclado para permitir que o jogo seja salvo em arquivo e carregado para continuar de onde parou.
Faça o melhor jogo que puder, exercitando sua criatividade, e explorando os recursos da programação orientada a objetos – altere
o projeto, proponha soluções elegantes, tente alternativas.
Documentação básica:
Os seguintes itens devem ser entregues em um documento anexo com no máximo 2 páginas:
- Nome e número USP dos integrantes do grupo;
- Diagrama UML simplificado de classes e interfaces, pode-se usar um gerador automático ou fazer na mão;
- Quaisquer esclarecimentos necessários para executar/compilar.

```
Bomberman-V1
├─ build.xml
├─ imgs
│  ├─ billbala.png
│  ├─ blackTile.png
│  ├─ bloco.png
│  ├─ bowser.png
│  ├─ casco.png
│  ├─ goomba.png
│  ├─ interrogacao.png
│  ├─ koopa.png
│  └─ mario.png
├─ manifest.mf
├─ nbproject
│  ├─ build-impl.xml
│  ├─ genfiles.properties
│  ├─ private
│  │  ├─ config.properties
│  │  ├─ private.properties
│  │  └─ private.xml
│  ├─ project.properties
│  └─ project.xml
├─ POO.dat
├─ README.md
└─ src
   ├─ Auxiliar
   │  ├─ Consts.java
   │  ├─ Desenho.java
   │  └─ Posicao.java
   ├─ Controler
   │  ├─ ControleDeJogo.java
   │  ├─ Tela.form
   │  └─ Tela.java
   ├─ Main.java
   └─ Modelos
      ├─ BichinhoVaiVemHorizontal.java
      ├─ Caveira.java
      ├─ Fogo.java
      ├─ Heroi.java
      └─ Personagem.java

```