package Swing.utils;

import Swing.components.StatusPanel;
import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia os passos do tutorial interativo do Minesweeper.
 */
public class TutorialInterativo {

    private final List<PassoTutorial> passos;
    private int indicePassoAtual;

    /**
     * Cria o tutorial com sequência guiada de ações.
     */
    public TutorialInterativo() {
        this.passos = new ArrayList<>();
        this.indicePassoAtual = 0;
        inicializarPassos();
    }

    private void inicializarPassos() {
        passos.add(new PassoTutorial(
                TipoAcaoTutorial.REVELAR,
                0,
                0,
                "Clique com o botão esquerdo na célula (1,1) para revelar uma casa segura.",
                "Ótimo! Agora marque uma mina com clique direito."));

        passos.add(new PassoTutorial(
                TipoAcaoTutorial.MARCAR_BANDEIRA,
                1,
                1,
                "Use clique direito na célula (2,2) para marcar a mina com uma bandeira.",
                "Perfeito! Você aprendeu a marcar minas."));

        passos.add(new PassoTutorial(
                TipoAcaoTutorial.REVELAR,
                2,
                2,
                "Revele a célula (3,3) para entender os números de minas vizinhas.",
                "Boa! O número indica quantas minas existem ao redor da célula."));

        passos.add(new PassoTutorial(
                TipoAcaoTutorial.REVELAR,
                0,
                4,
                "Agora revele a célula (1,5) para ver a abertura automática de áreas vazias.",
                "Excelente! Você concluiu o tutorial interativo."));
    }

    /**
     * Retorna a explicação inicial das mecânicas do jogo.
     *
     * @return texto introdutório
     */
    public String getExplicacaoInicial() {
        return "Bem-vindo ao tutorial!\n\n"
                + "Objetivo: revelar todas as casas sem minas.\n"
                + "- Clique esquerdo: revela célula.\n"
                + "- Clique direito: marca/desmarca bandeira.\n"
                + "- Números mostram quantas minas existem nas células vizinhas.\n\n"
                + "Siga as dicas no topo da tela para completar cada passo.";
    }

    /**
     * Retorna a dica contextual do passo atual.
     *
     * @return texto da dica
     */
    public String getDicaAtual() {
        if (!isAtivo()) {
            return "Tutorial concluído.";
        }
        return passos.get(indicePassoAtual).mensagemInicio();
    }

    /**
     * Indica se o tutorial ainda possui passos pendentes.
     *
     * @return true quando ativo
     */
    public boolean isAtivo() {
        return indicePassoAtual < passos.size();
    }

    /**
     * Valida se a ação do jogador corresponde ao passo esperado.
     *
     * @param linha         linha clicada
     * @param coluna        coluna clicada
     * @param cliqueDireito true para clique direito
     * @param painelEstado  painel para atualizar a dica
     * @return true se a ação está correta para o passo
     */
    public boolean validarAcaoEsperada(int linha, int coluna, boolean cliqueDireito, StatusPanel painelEstado) {
        if (!isAtivo()) {
            return true;
        }

        PassoTutorial passoAtual = passos.get(indicePassoAtual);
        boolean tipoCorreto = (cliqueDireito && passoAtual.tipoAcao() == TipoAcaoTutorial.MARCAR_BANDEIRA)
                || (!cliqueDireito && passoAtual.tipoAcao() == TipoAcaoTutorial.REVELAR);

        boolean celulaCorreta = passoAtual.linha() == linha && passoAtual.coluna() == coluna;

        if (!tipoCorreto || !celulaCorreta) {
            painelEstado.atualizarDicaTutorial(passoAtual.mensagemInicio());
            return false;
        }

        return true;
    }

    /**
     * Avança para o próximo passo após uma ação válida.
     *
     * @param painelEstado painel para exibir dicas contextuais
     */
    public void concluirPasso(StatusPanel painelEstado) {
        if (!isAtivo()) {
            return;
        }

        PassoTutorial passoConcluido = passos.get(indicePassoAtual);
        indicePassoAtual++;

        if (isAtivo()) {
            painelEstado.atualizarDicaTutorial(passos.get(indicePassoAtual).mensagemInicio());
        } else {
            painelEstado.atualizarDicaTutorial(passoConcluido.mensagemSucesso());
        }
    }

    private enum TipoAcaoTutorial {
        REVELAR,
        MARCAR_BANDEIRA
    }

    private record PassoTutorial(
            TipoAcaoTutorial tipoAcao,
            int linha,
            int coluna,
            String mensagemInicio,
            String mensagemSucesso) {
    }
}
