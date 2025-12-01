/**
 * Representa uma localização em uma grade retangular.
 * @author Grupo 1
 * @version 2025
 */
public class Location
{
    // Posições de linha e coluna.
    private final int row;
    private final int col;

    /**
     * Representa uma linha e coluna.
     * @param row A linha.
     * @param col A coluna.
     */
    public Location(int row, int col)
    {
        this.row = row;
        this.col = col;
    }
    
    /**
     * Implementa a igualdade de conteúdo.
     */
    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Location other) {
            return row == other.getRow() && col == other.getCol();
        }
        return false;
    }
    
    /**
     * Retorna uma string no formato linha,coluna.
     * @return Uma representação em string da localização.
     */
    @Override
    public String toString()
    {
        return row + "," + col;
    }
    
    /**
     * Calcula um código de hash único para cada par (linha, coluna).
     */
    @Override
    public int hashCode()
    {
        return (row << 16) + col;
    }
    
    /**
     * @return O valor da linha.
     */
    public int getRow()
    {
        return row;
    }
    
    /**
     * @return O valor da coluna.
     */
    public int getCol()
    {
        return col;
    }
}