package Swing.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Serviço para salvar e carregar partidas pausadas em disco.
 */
public class GameSaveManager {

    private static final String ARQUIVO_NOME = "minesweeper-save.dat";
    private static final String PASTA_DADOS = "MineSweeper";
    private static final String SUBPASTA_DADOS = "data";

    private final Path caminhoArquivo;

    /**
     * Cria o gestor apontando para a pasta de dados dentro do projeto.
     */
    public GameSaveManager() {
        String diretorioExecucao = System.getProperty("user.dir");
        Path caminhoBaseProjeto = Paths.get(diretorioExecucao);

        if (Files.exists(caminhoBaseProjeto.resolve(PASTA_DADOS))) {
            caminhoBaseProjeto = caminhoBaseProjeto.resolve(PASTA_DADOS);
        }

        this.caminhoArquivo = caminhoBaseProjeto.resolve(SUBPASTA_DADOS).resolve(ARQUIVO_NOME);
    }

    /**
     * Salva o estado da partida em disco.
     *
     * @param estado estado serializável do jogo
     * @return true quando salvo com sucesso
     */
    public boolean salvar(GameSaveData estado) {
        try {
            Path diretorio = caminhoArquivo.getParent();
            if (diretorio != null) {
                Files.createDirectories(diretorio);
            }

            try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(caminhoArquivo))) {
                output.writeObject(estado);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Carrega o estado salvo, se existir.
     *
     * @return estado salvo ou null quando indisponível
     */
    public GameSaveData carregar() {
        if (!Files.exists(caminhoArquivo)) {
            return null;
        }

        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(caminhoArquivo))) {
            Object obj = input.readObject();
            if (obj instanceof GameSaveData gameSaveData) {
                return gameSaveData;
            }
            return null;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Verifica se há um arquivo de jogo salvo disponível.
     *
     * @return true quando o arquivo existe
     */
    public boolean existeSave() {
        return Files.exists(caminhoArquivo);
    }

    /**
     * Remove o jogo salvo do disco.
     */
    public void limparSave() {
        try {
            Files.deleteIfExists(caminhoArquivo);
        } catch (IOException ignored) {
        }
    }
}
