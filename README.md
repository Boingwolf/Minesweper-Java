# Minesweeper em Java

## Descrição

Este projeto implementa o jogo clássico **Minesweeper** (Campo Minado), permitindo ao utilizador revelar células, colocar bandeiras e concluir partidas em diferentes níveis de dificuldade.

O objetivo pedagógico é consolidar competências técnicas em:

- Programação orientada a objetos (classes, encapsulamento e organização por pacotes);
- Lógica algorítmica e gestão de estados de jogo;
- Desenvolvimento de interface gráfica com **Java Swing**;
- Organização de projeto e boas práticas de documentação.

## Funcionalidades

- Geração de tabuleiro com minas;
- Revelação de células e propagação de células vazias;
- Colocação e remoção de bandeiras;
- Controlo de fim de jogo (vitória/derrota);
- Interface gráfica com menus, painel de jogo e estado da partida;
- Gestão de tema (claro/escuro);
- Estatísticas de jogador e níveis de dificuldade.

### Sistema de Ajudas/Dicas

O jogo inclui um sistema de ajudas para apoiar o jogador em momentos críticos:

- **Revelar célula segura aleatória** (nunca revela uma mina);
- **Marcação automática de mina** em célula ainda não sinalizada;
- **Limite por partida** de `3` ajudas no total;
- **Penalização de tempo** de `+15 segundos` por ajuda utilizada;
- **Indicador visual** no painel de estado: `Ajudas: restantes/total`.

## Tecnologias utilizadas

- **Java**
- **Java Swing** (interface gráfica)
- Estrutura modular por pacotes (lógica, interface e utilitários)

## Estrutura do projeto

```text
MineSweeper/
├─ src/
│  ├─ Main.java
│  ├─ Funcoes/
│  │  ├─ CelulaPack/
│  │  ├─ OutrasFuncoes/
│  │  └─ TabuleiroPack/
│  └─ Swing/
│     ├─ components/
│     ├─ icons/
│     ├─ menu/
│     ├─ stats/
│     └─ utils/
└─ bin/
```

## Requisitos

- **JDK 17** ou superior (recomendado);
- Sistema operativo com suporte Java (Windows, Linux ou macOS);
- IDE opcional: VS Code, IntelliJ IDEA ou Eclipse.

## Como executar

### Opção 1 — VS Code

1. Abrir a pasta do projeto `MineSweeper` no VS Code;
2. Garantir que o Java está corretamente configurado no sistema;
3. Executar a classe `Main.java`.

### Opção 2 — Linha de comandos

Na raiz do repositório:

```bash
cd MineSweeper
javac -d bin src/Main.java src/Funcoes/CelulaPack/*.java src/Funcoes/OutrasFuncoes/*.java src/Funcoes/TabuleiroPack/*.java src/Swing/*.java src/Swing/components/*.java src/Swing/icons/*.java src/Swing/menu/*.java src/Swing/stats/*.java src/Swing/utils/*.java
java -cp bin Main
```

## Contexto escolar

Este trabalho foi concebido como projeto prático para demonstrar a capacidade de:

- Planear e desenvolver uma aplicação completa;
- Aplicar conhecimentos de programação adquiridos em contexto letivo;
- Produzir um projeto com organização e apresentação profissional.

## Autor

- **Guilherme Eusébio**
