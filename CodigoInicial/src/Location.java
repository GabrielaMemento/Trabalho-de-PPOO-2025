/**
 * Classe que representa uma localização em uma grade retangular
 * 
 * Uma instância de {@code Location} armazena coordenadas de linha e coluna,
 * utilizadas pelas classes {@link Field}, {@link Animal} e outras para definir
 * posições dentro do campo da simulação
 *
 * <p>Inclui métodos para comparação de igualdade, geração de hash code e
 * representação textual
 */
public class Location {
    // Linha da posição na grade
    private int row;
    // Coluna da posição na grade
    private int col;

    /**
     * Cria uma localização com linha e coluna específicas
     *
     * @param row linha da posição
     * @param col coluna da posição
     */
    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Verifica se duas localizações são iguais (mesma linha e coluna)
     *
     * @param obj objeto a comparar
     * @return true se for uma {@code Location} com mesma linha e coluna
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Location) {
            Location other = (Location) obj;
            return row == other.getRow() && col == other.getCol();
        } else {
            return false;
        }
    }

    /**
     * Retorna uma string no formato "linha,coluna"
     *
     * @return representação textual da localização
     */
    @Override
    public String toString() {
        return row + "," + col;
    }

    /**
     * Gera um código hash único para cada par (linha, coluna)
     * Usa os 16 bits superiores para a linha e os inferiores para a coluna
     *
     * @return código hash da localização
     */
    @Override
    public int hashCode() {
        return (row << 16) + col;
    }

    // @return linha da posição
    public int getRow() {
        return row;
    }

    // @return coluna da posição
    public int getCol() {
        return col;
    }
}  

