import java.util.Random;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Representa o campo (grade) da simulação, armazenando objetos (atores)
 * em células e mantendo os tipos de {@link Terrain} de cada célula.
 *
 * @author Grupo 1
 * @version 2025
 */
public class Field {
    
    // Atributos
    private final int depth;
    private final int width;
    private final Object[][] cells; // Matriz que armazena os objetos (Actor)
    private final Terrain[][] terrainMap; // Matriz que armazena o tipo de terreno
    private static final Random RAND = new Random();

    /**
     * Construtor auxiliar.
     * @param depth Número de linhas do campo.
     * @param width Número de colunas do campo.
     * @param initTerrain Se deve gerar o terreno aleatoriamente.
     */
    private Field(int depth, int width, boolean initTerrain) {
        this.depth = depth;
        this.width = width;
        cells = new Object[depth][width];
        terrainMap = new Terrain[depth][width];
        if (initTerrain) {
            generateTerrain();
        } else {
            // Inicializa com terreno PLAIN por padrão se não for para gerar
            for (int r = 0; r < depth; r++) {
                for (int c = 0; c < width; c++) {
                    terrainMap[r][c] = Terrain.PLAIN;
                }
            }
        }
    }
    
    /**
     * Construtor de cópia (para criar o campo de destino no ciclo act).
     * @param sourceField O campo a ser copiado.
     */
    public Field(Field sourceField) {
        this(sourceField.getDepth(), sourceField.getWidth(), false);
        // Copia apenas os tipos de terreno
        for (int r = 0; r < depth; r++) {
            System.arraycopy(sourceField.terrainMap[r], 0, this.terrainMap[r], 0, width);
        }
    }
    
    /**
     * Construtor principal que também gera o terreno.
     * @param depth Número de linhas do campo.
     * @param width Número de colunas do campo.
     */
    public Field(int depth, int width) {
        this(depth, width, true);
    }

    /**
     * Limpa todo o campo (apenas objetos), mantendo o terreno.
     */
    public void clear() {
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                cells[row][col] = null;
            }
        }
    }

    /**
     * Limpa apenas uma célula específica.
     * @param location Posição a limpar.
     */
    public void clear(Location location) {
        cells[location.getRow()][location.getCol()] = null;
    }

    /**
     * Posiciona um objeto em uma localização específica do campo.
     * @param obj Objeto a ser colocado (Actor, Plant, etc.).
     * @param location Posição alvo.
     */
    public void place(Object obj, Location location) {
        cells[location.getRow()][location.getCol()] = obj;
    }

    /**
     * Recupera o objeto presente em uma determinada localização.
     * @param location Posição alvo.
     * @return Objeto contido na célula ou null se vazia.
     */
    public Object getObjectAt(Location location) {
        return cells[location.getRow()][location.getCol()];
    }

    /**
     * Obtém o tipo de terreno de uma determinada localização.
     * @param location Posição alvo.
     * @return Tipo de terreno da célula.
     */
    public Terrain getTerrainAt(Location location) {
        return terrainMap[location.getRow()][location.getCol()];
    }

    /** @return Profundidade (linhas) do campo. */
    public int getDepth() { return depth; }

    /** @return Largura (colunas) do campo. */
    public int getWidth() { return width; }

    /**
     * Gera tipos de terreno aleatórios para cada célula do campo.
     */
    private void generateTerrain() {
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                double r = RAND.nextDouble();
                if (r < 0.05) terrainMap[row][col] = Terrain.MOUNTAIN;
                else if (r < 0.10) terrainMap[row][col] = Terrain.RIVER;
                else if (r < 0.20) terrainMap[row][col] = Terrain.DENSE_VEGETATION;
                else if (r < 0.25) terrainMap[row][col] = Terrain.CAVE;
                else if (r < 0.30) terrainMap[row][col] = Terrain.BURROW;
                else terrainMap[row][col] = Terrain.PLAIN;
            }
        }
    }

    /**
     * Retorna uma localização adjacente livre (sem objeto) aleatoriamente.
     * @param location Posição de referência.
     * @return Uma localização adjacente livre ou null se não houver.
     */
    public Location freeAdjacentLocation(Location location) {
        List<Location> adjacent = adjacentLocations(location);
        for (Location loc : adjacent) {
            if (getObjectAt(loc) == null) {
                return loc;
            }
        }
        return null;
    }

    /**
     * Lista todas as localizações adjacentes (8 vizinhos), embaralhada.
     * @param location Posição de referência.
     * @return Lista embaralhada de vizinhos válidos.
     */
    public List<Location> adjacentLocations(Location location) {
        List<Location> locations = new ArrayList<>();
        int row = location.getRow();
        int col = location.getCol();
        
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                if (r == 0 && c == 0) continue; // Pula a própria localização
                
                int newRow = row + r;
                int newCol = col + c;
                
                // Verifica se está dentro dos limites do campo
                if (newRow >= 0 && newRow < depth && newCol >= 0 && newCol < width) {
                    locations.add(new Location(newRow, newCol));
                }
            }
        }
        Collections.shuffle(locations);
        return locations;
    }

    /**
     * Retorna todas as localizações adjacentes que estão livres (sem objeto).
     * @param location Posição de referência.
     * @return Lista de posições vagas ao redor da posição fornecida.
     */
    public List<Location> getFreeAdjacent(Location location) {
        List<Location> free = new ArrayList<>();
        for (Location loc : adjacentLocations(location)) {
            if (getObjectAt(loc) == null) {
                free.add(loc);
            }
        }
        return free;
    }
}