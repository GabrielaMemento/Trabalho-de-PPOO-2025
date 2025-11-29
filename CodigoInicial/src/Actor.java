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

    Location findFood();
    void act(List<Animal> newAnimals);
    
}
