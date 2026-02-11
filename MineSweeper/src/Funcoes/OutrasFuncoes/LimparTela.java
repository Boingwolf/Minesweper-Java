package Funcoes.OutrasFuncoes;

public class LimparTela {
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
