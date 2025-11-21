import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Classe que representa o campo da simulação em forma de grade retangular
 * 
 * Cada posição da grade pode armazenar um único objeto (animal, planta ou
 * outro elemento). Os métodos permitem posicionar, remover e consultar objetos,
 * além de gerar localizações adjacentes para movimentação
 *
 * É utilizada pelas classes de animais para definir onde estão e como se
 * movimentam dentro do ambiente
 *
 */
public class Field {
    // Gerador de números aleatórios para movimentação
    private static final Random rand = new Random();

    // Profundidade (número de linhas) do campo
    private int depth;
    // Largura (número de colunas) do campo
    private int width;
    // Matriz que armazena os objetos do campo
    private Object[][] field;

    /**
     * Cria um campo com dimensões específicas.
     *
     * @param depth profundidade (linhas)
     * @param width largura (colunas)
     */
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
        field = new Object[depth][width];
    }

    // Limpa o campo, removendo todos os objetos
    public void clear() {
        for(int row = 0; row < depth; row++) {
            for(int col = 0; col < width; col++) {
                field[row][col] = null;
            }
        }
    }

    /**
     * Posiciona um objeto em uma coordenada específica
     * 
     * @param animal objeto a ser colocado
     * @param row linha da posição
     * @param col coluna da posição
     */
    public void place(Object animal, int row, int col) {
        place(animal, new Location(row, col));
    }

    /**
     * Posiciona um objeto em uma localização específica
     * 
     * @param animal objeto a ser colocado
     * @param location localização desejada
     */
    public void place(Object animal, Location location) {
        field[location.getRow()][location.getCol()] = animal;
    }

    /**
     * Retorna o objeto presente em uma localização
     * 
     * @param location localização desejada
     * @return objeto na posição ou null se estiver vazio
     */
    public Object getObjectAt(Location location) {
        return getObjectAt(location.getRow(), location.getCol());
    }

    /**
     * Retorna o objeto presente em uma coordenada
     * 
     * @param row linha desejada
     * @param col coluna desejada
     * @return objeto na posição ou null se estiver vazio
     */
    public Object getObjectAt(int row, int col) {
        return field[row][col];
    }

    /**
     * Gera uma localização aleatória adjacente à posição atual
     * Pode retornar a mesma posição se não houver espaço válido
     *
     * @param location posição atual.
     * @return localização válida adjacente ou a mesma posição
     */
    public Location randomAdjacentLocation(Location location) {
        int row = location.getRow();
        int col = location.getCol();
        int nextRow = row + rand.nextInt(3) - 1;
        int nextCol = col + rand.nextInt(3) - 1;
        if(nextRow < 0 || nextRow >= depth || nextCol < 0 || nextCol >= width) {
            return location;
        } else if(nextRow != row || nextCol != col) {
            return new Location(nextRow, nextCol);
        } else {
            return location;
        }
    }

    /**
     * Procura uma posição livre adjacente à atual
     * Se não houver, retorna a própria posição se estiver livre
     * Caso contrário, retorna null
     *
     * @param location posição atual
     * @return localização livre ou null se não houver
     */
    public Location freeAdjacentLocation(Location location) {
        Iterator<Location> adjacent = adjacentLocations(location);
        while(adjacent.hasNext()) {
            Location next = adjacent.next();
            if(field[next.getRow()][next.getCol()] == null) {
                return next;
            }
        }
        if(field[location.getRow()][location.getCol()] == null) {
            return location;
        } else {
            return null;
        }
    }

    /**
     * Gera uma lista embaralhada de posições adjacentes à atual
     * Não inclui a própria posição
     *
     * @param location posição atual
     * @return iterador sobre localizações adjacentes
     */
    public Iterator<Location> adjacentLocations(Location location) {
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
        Collections.shuffle(locations, rand);
        return locations.iterator();
    }

    // @return profundidade do campo
    public int getDepth() {
        return depth;
    }

    // @return largura do campo
    public int getWidth() {
        return width;
    }
}
