import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Gerencia as restrições de movimento de atores (animais) com base no tipo de {@link Terrain}.
 * As restrições são carregadas de um arquivo e armazenadas em um mapa estático.
 * @author Grupo 1
 * @version 2025
 */
public class Barriers {

    private static final String ACTOR_RESTRICTIONS_FILE = "restricoes_atores.txt";
    /** Mapa que armazena: Chave=Nome do Ator (String), Valor=Set de Terrenos Proibidos (Terrain). */
    private static final Map<String, Set<Terrain>> RESTRICTIONS = new HashMap<>();

    /**
     * Carrega as restrições de terreno para todos os atores do arquivo configurado.
     */
    public static void loadRestrictions() {
        try (Scanner scanner = new Scanner(new File(ACTOR_RESTRICTIONS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                
                // Ignora linhas inválidas
                if (line.isEmpty() || line.startsWith("#") || !line.contains("=")) {
                    continue;
                }
                
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String actorName = parts[0].trim().toUpperCase();
                    String[] forbiddenTerrains = parts[1].trim().split(",");
                    
                    Set<Terrain> barriers = new HashSet<>();
                    for (String t : forbiddenTerrains) {
                        try {
                            barriers.add(Terrain.valueOf(t.trim().toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            System.err.println("Aviso: Terreno inválido '" + t + 
                                            "' para o ator " + actorName);
                        }
                    }
                    RESTRICTIONS.put(actorName, barriers);
                }
            }
        } catch (java.io.FileNotFoundException e) {
            System.err.println("ERRO FATAL: Arquivo de restrições não encontrado: " + ACTOR_RESTRICTIONS_FILE);
            System.err.println("Certifique-se de que o arquivo existe e está no diretório correto.");
        }
    }

    /**
     * Consulta se um tipo de terreno é proibido para um determinado ator.
     * @param actorName O nome da classe do ator (ex: "FOX", "RABBIT").
     * @param terrain O tipo de terreno a ser verificado.
     * @return true se o ator não pode entrar no terreno, false caso contrário.
     */
    public static boolean isForbidden(String actorName, Terrain terrain) {
        Set<Terrain> actorBarriers = RESTRICTIONS.getOrDefault(actorName.toUpperCase(), Collections.emptySet());
        return actorBarriers.contains(terrain);
    }
}