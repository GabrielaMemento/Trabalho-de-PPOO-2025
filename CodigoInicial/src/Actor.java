import java.util.List;

/**
 * Interface que define o contrato para todos os agentes ativos no simulador.
 * Um Actor (ator) é qualquer entidade que pode realizar ações durante
 * a simulação, incluindo animais e potencialmente plantas que crescem/espalham.
 * Implementações típicas:
 * - Animal (classe abstrata que implementa Actor)
 * - Planta (se plantas tiverem comportamento ativo no futuro)
 * @author Leonardo Elias Rodrigues
 */
public interface Actor {
    
    /**
     * Executa as ações deste ator em um passo da simulação.
     *
     * @param newActors Lista onde novos atores nascidos/criados devem ser adicionados.
     *
     */
    void act(List<Actor> newActors);
    
    /**
     * Verifica se este ator ainda está ativo na simulação.
     * @return true se o ator está vivo e ativo; false se está morto/inativo.
     */
    boolean isAlive();
    
    /**
     * Marca este ator como morto/inativo.
     */
    void setDead();
}
