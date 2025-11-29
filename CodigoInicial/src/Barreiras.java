import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Barreiras {

    // O mapa armazena: Chave=Nome do Ator (String), Valor=Set de Terrenos Proibidos
    private static final Map<String, Set<Terreno>> RESTRICOES = new HashMap<>();

    /**
     * Carrega todas as restrições do arquivo único.
     */
    public static void carregar(String nomeArquivo) {
        try (Scanner scanner = new Scanner(new File(nomeArquivo))) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();
                
                // Ignora linhas vazias, comentários ou que não contenham '='
                if (linha.isEmpty() || linha.startsWith("#") || !linha.contains("=")) {
                    continue;
                }
                
                // Divide a linha em Ator (chave) e Terrenos (valores)
                String[] partes = linha.split("=");
                if (partes.length == 2) {
                    String nomeAtor = partes[0].trim().toUpperCase();
                    String[] terrenosProibidos = partes[1].trim().split(",");
                    
                    Set<Terreno> barreiras = new HashSet<>();
                    for (String t : terrenosProibidos) {
                        try {
                            barreiras.add(Terreno.valueOf(t.trim().toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            System.err.println("Aviso: Terreno inválido '" + t + "' para o ator " + nomeAtor);
                        }
                    }
                    RESTRICOES.put(nomeAtor, barreiras);
                }
            }
        } catch (java.io.FileNotFoundException e) {
            System.err.println("ERRO FATAL: Arquivo de restrições não encontrado: " + nomeArquivo);
        }
    }

    /**
     * Consulta se um terreno é proibido para um determinado ator.
     */
    public static boolean isProibido(String nomeAtor, Terreno terreno) {
        // Busca o Set de restrições para o ator específico
        Set<Terreno> barreirasDoAtor = RESTRICOES.getOrDefault(nomeAtor.toUpperCase(), Collections.emptySet());
        
        // Verifica se o terreno está neste Set
        return barreirasDoAtor.contains(terreno);
    }
}