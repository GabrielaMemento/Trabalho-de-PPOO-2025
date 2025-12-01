import java.util.List;

/**
 * Contrato para todos os agentes ativos no simulador.
 * Um ator é qualquer entidade que pode realizar ações durante a simulação.
 * @author Grupo 1
 * @version 2025
 */
public interface Actor {
    /**
     * Executa as ações do ator em um passo da simulação.
     * @param currentField O estado atual do campo (somente leitura).
     * @param updatedField O campo onde as novas posições e animais serão colocados.
     * @param newActors Lista onde os novos atores (animais/plantas) nascidos devem ser adicionados.
     */
    void act(Field currentField, Field updatedField, List<Actor> newActors);
    
    /**
     * Retorna a localização atual do ator.
     * Necessário para o Field localizar o ator durante o ciclo de simulação.
     * @return A localização atual.
     */
    Location getLocation();
    
    /**
     * Define a localização do ator.
     * @param location A nova localização.
     */
    void setLocation(Location location);
}