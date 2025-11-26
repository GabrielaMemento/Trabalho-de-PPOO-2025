/** @author Leonardo Elias Rodrigues
 * Interface para entidades que podem ser consumidas como alimento
 * por outros animais na simulação.
 */
public interface Comestivel {
    
    /**
     * @return o valor nutricional que este alimento fornece
     */
    int getValorNutricional();
    
    /**
     * @return true se este alimento está disponível para ser consumido
     */
    boolean podeSerComido();
    
    /**
     * Método chamado quando o alimento é consumido
     */
    void foiComido();
}
