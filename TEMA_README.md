# Sistema de Temas - MineSweeper

## Estrutura de Imagens

O jogo agora suporta temas **claro** e **escuro** com imagens específicas para cada um.

### Pastas de Imagens

As imagens devem estar organizadas nas seguintes pastas:

```
src/images/
├── TemaClaro/
│   ├── Bomba.png
│   ├── Bandeira.png
│   ├── CelulaFechada.png
│   ├── CelulaAberta.png
│   ├── Explosao.png
│   ├── Numero1.png
│   ├── Numero2.png
│   ├── Numero3.png
│   ├── Numero4.png
│   ├── Numero5.png
│   ├── Numero6.png
│   ├── Numero7.png
│   └── Numero8.png
└── TemaEscuro/
    ├── Bomba.png
    ├── Bandeira.png
    ├── CelulaFechada.png
    ├── CelulaAberta.png
    ├── Explosao.png
    ├── Numero1.png
    ├── Numero2.png
    ├── Numero3.png
    ├── Numero4.png
    ├── Numero5.png
    ├── Numero6.png
    ├── Numero7.png
    └── Numero8.png
```

## Como Funciona

### Seleção de Tema no Menu Inicial

No arquivo **MenuInicial.java**, há um painel "Tema" com opções:

- **Claro** (tema padrão)
- **Escuro**

### Mudança de Tema Durante o Jogo

Durante o jogo, você pode mudar o tema através do menu **Opções > Tema**:

- Claro
- Escuro

### IconManager

O `IconManager` foi refatorado para:

1. Aceitar um `Tema` como parâmetro no construtor
2. Carregar imagens da pasta apropriada (`/images/TemaClaro/` ou `/images/TemaEscuro/`)
3. Ter um método `recarregarParaTema(Tema)` para trocar as imagens em tempo de execução

### GamePanel

O `GamePanel` foi atualizado para:

1. Recarregar os ícones quando `aplicarTema()` é chamado
2. Atualizar a tela com as novas imagens automaticamente

## Gerenciador de Tema

A classe **TemaManager** mantém o tema atual selecionado, permitindo que todos os componentes da interface acessem o tema em tempo real.

## Colors Tema Escuro e Claro

Os temas definem cores para:

- Fundo do gradiente (superior e inferior)
- Grid (base e alternado)
- Painel
- Texto padrão
- Menu (fundo e texto)
- Botões (fundo e texto)

Isso garante que toda a interface se adapte ao tema escolhido, além das imagens dos ícones.

## Fallback Automático

Se as imagens não forem encontradas nas pastas apropriadas, o sistema usa ícones gerados proceduralmente. Esses ícones também se adaptam ao tema escolhido.

## Para Adicionar Suas Imagens

1. Crie imagens PNG (recomendado 24x24 pixels para os ícones de células)
2. Coloque-as nas pastas `/images/TemaClaro/` ou `/images/TemaEscuro/`
3. Use os nomes exatos listados acima
4. Compile e execute o jogo

As imagens serão automaticamente carregadas de acordo com o tema selecionado!
