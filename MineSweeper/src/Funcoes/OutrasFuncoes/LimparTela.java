package Funcoes.OutrasFuncoes;

/**
 * Utilitario para limpar o console no Windows.
 */
public class LimparTela {
    /**
     * Limpa a tela do console utilizando o comando {@code cls} do Windows.
     */
    public static void limparTela() {
        try {
            new ProcessBuilder("cmd", "/c", "cls")
                    .inheritIO()
                    .start()
                    .waitFor();
        } catch (Exception e) {
            System.out.println("Erro ao limpar a tela");
        }
    }
}
