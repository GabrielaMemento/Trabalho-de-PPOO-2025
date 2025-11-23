import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

/**
 * Represent a rectangular grid of field positions.
 * Each position is able to store a single animal.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-09
 */
public class Field
{    
    // The depth and width of the field.
    private int depth, width;
    // Storage for the animals.
    private Object[][] field;

    /**
     * Represent a field of the given dimensions.
     * @param depth The depth of the field.
     * @param width The width of the field.
     */
    public Field(int depth, int width)
    {
        this.depth = depth;
        this.width = width;
        field = new Object[depth][width];
    }

    /**
     * Remove todos os objetos do campo
     */
    public void clear()
    {
        for(int row = 0; row < depth; row++) {
            for(int col = 0; col < width; col++) {
                field[row][col] = null;
            }
        }
    }

    /**
     * Remove tudo em uma posição específica
     */
    public void clear(Location location) 
    {
        field[location.getRow()][location.getCol()] = null;
    }

    /**
     * Coloca um objeto na posição específica (row, col)
     */
    public void place(Object animal, int row, int col)
    {
        place(animal, new Location(row, col));
    }
    
    /**
     * Coloca um objeto em uma localização específica
     */
    public void place(Object object, Location location)
    {
        field[location.getRow()][location.getCol()] = object;
    }
    

    /**
     * Retorna a posição de um objeto (col,row)
     */
    public Object getObjectAt(Location location)
    {
        return getObjectAt(location.getRow(), location.getCol());
    }
    

    /**
     * Retorna o objeto em um determinado lugar
     */
    public Object getObjectAt(int row, int col)
    {
        return field[row][col];
    }

    /**
     * libera uma posição adjacente
     */
    public Location freeAdjacentLocation(Location location)
    {
        List<Location> free = getFreeAdjacent(location);
        if (free.isEmpty()) {
            return null;
        } else {
            return free.get(0);
        }
    }

    /**
     * Retorna uma posição adjacente livre
     */
    public List<Location> getFreeAdjacent(Location location) 
    {
        List<Location> free = new LinkedList<>();
        Iterator<Location> it = adjacentLocations(location);
        while (it.hasNext()) {
            Location loc = it.next();
            if (getObjectAt(loc) == null) {
                free.add(loc);
            }
        }
        return free;
    }

    /**
     * Retorna uma lista aleatória de posições adjacentes 
     */
    public Iterator<Location> adjacentLocations(Location location)
    {
        int row = location.getRow();
        int col = location.getCol();
        LinkedList<Location> locations = new LinkedList<>();
        for(int roffset = -1; roffset <= 1; roffset++) {
            int nextRow = row + roffset;
            if(nextRow >= 0 && nextRow < depth) {
                for(int coffset = -1; coffset <= 1; coffset++) {
                    int nextCol = col + coffset;
                    if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                        locations.add(new Location(nextRow, nextCol));
                    }
                }
            }
        }
        Collections.shuffle(locations);
        return locations.iterator();
    }


    /**
     * Retorna uma localização aleatória
     */
    public Location randomLocation() 
    {
        int row = (int) (Math.random() * depth);
        int col = (int) (Math.random() * width);
        return new Location(row, col);
    }

    /**
     * Retorna o número de linhas de um campo
     */
    public int getDepth()
    {
        return depth;
    }

    /**
     * Retorna o número de colunas de um campo
     */
    public int getWidth()
    {
        return width;
    }
}
