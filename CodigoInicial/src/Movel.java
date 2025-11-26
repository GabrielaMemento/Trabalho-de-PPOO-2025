/**@author Leonardo Elias Rodrigues 
 * Interface para entidades que podem se mover pelo campo,
 * com restrições baseadas no tipo de terreno.
 */
public interface Movel {
    
    /**
     * Verifica se a entidade pode se mover para um determinado tipo de terreno
     * @param terreno o tipo de terreno a ser verificado
     * @return true se o movimento é permitido
     */
    boolean podeMoverPara(Terreno terreno);
    
    /**
     * Encontra uma localização adjacente válida para movimento
     * @return Localização válida ou null se não houver movimento possível
     */
    Location encontrarProximaLocalizacao();
}