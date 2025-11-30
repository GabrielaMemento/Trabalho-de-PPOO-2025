import java.util.Random;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Representa o campo (grade) da simulação, responsável por armazenar objetos
 * (animais e plantas) em células e manter os tipos de terreno de cada célula.
 *
 * - Cada célula pode conter um único objeto.
 * - Cada célula possui um tipo de terreno (PLAIN, BURROW, CAVE, MOUNTAIN, RIVER, DENSE_VEGETATION).
 * - Fornece utilitários para localizar posições adjacentes e livres.
 *
 * Esta classe foi expandida para suportar tipos de terreno conforme a proposta
 * do trabalho prático (rios, montanhas, cavernas, tocas, vegetação densa).
 *
 * @author David J. Barnes
 * @author Michael Kolling
 * @version 2002-04-23 (revisado 2025-11 para terrenos e utilitários)
 */
public class Field {
    /** Profundidade (número de linhas) da grade. */
    private final int depth;
    /** Largura (número de colunas) da grade. */
    private final int width;
    /** Matriz que armazena os objetos posicionados nas células. */
    private Object[][] field;
    /** Matriz que armazena o tipo de terreno de cada célula. */
    private Terreno[][] terrain;

    /**
     * Cria um novo campo com profundidade e largura especificadas.
     * Inicializa as estruturas internas e gera os tipos de terreno.
     *
     * @param depth número de linhas do campo.
     * @param width número de colunas do campo.
     */
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
        field = new Object[depth][width];
        terrain = new Terreno[depth][width];
        generateTerrain();
    }

    /**
     * Limpa todo o campo, removendo todos os objetos das células.
     * Não altera os tipos de terreno.
     */
    public void clear() {
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                field[row][col] = null;
            }
        }
    }

    /**
     * Limpa apenas uma célula específica, removendo o objeto nela contido.
     * Útil quando um animal morre ou se move de posição.
     *
     * @param location posição a limpar.
     */
    public void clear(Location location) {
        field[location.getRow()][location.getCol()] = null;
    }

    /**
     * Posiciona um objeto em uma localização específica do campo.
     *
     * @param obj objeto a ser colocado (Animal, Plant, etc.).
     * @param location posição alvo.
     */
    public void place(Object obj, Location location) {
        field[location.getRow()][location.getCol()] = obj;
    }

    /**
     * Recupera o objeto presente em uma determinada localização.
     *
     * @param location posição alvo.
     * @return objeto contido na célula ou null se vazia.
     */
    public Object getObjectAt(Location location) {
        return field[location.getRow()][location.getCol()];
    }

    /**
     * Obtém o tipo de terreno de uma determinada localização.
     *
     * @param location posição alvo.
     * @return tipo de terreno da célula.
     */
    public Terreno getTerrainAt(Location location) {
        return terrain[location.getRow()][location.getCol()];
    }

    /**
     * @return profundidade (linhas) do campo.
     */
    public int getDepth() { return depth; }

    /**
     * @return largura (colunas) do campo.
     */
    public int getWidth() { return width; }

    /**
     * Gera tipos de terreno aleatórios para cada célula do campo.
     * As probabilidades foram ajustadas para criar diversidade:
     * - 5% MOUNTAIN
     * - 5% RIVER
     * - 10% DENSE_VEGETATION
     * - 5% CAVE
     * - 5% BURROW
     * - Restante: PLAIN
     *
     * Pode ser substituído por um gerador determinístico em versões futuras.
     */
    private void generateTerrain() {
        Random rand = new Random();
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                double r = rand.nextDouble();
                if (r < 0.05) terrain[row][col] = Terreno.MOUNTAIN;
                else if (r < 0.10) terrain[row][col] = Terreno.RIVER;
                else if (r < 0.20) terrain[row][col] = Terreno.DENSE_VEGETATION;
                else if (r < 0.25) terrain[row][col] = Terreno.CAVE;
                else if (r < 0.30) terrain[row][col] = Terreno.BURROW;
                else terrain[row][col] = Terreno.PLAIN;
            }
        }
    }

    /**
     * Retorna uma localização adjacente livre (sem objeto) aleatoriamente,
     * dentre as disponíveis próximas à posição fornecida.
     *
     * @param location posição de referência.
     * @return uma localização adjacente livre ou null se não houver.
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
     * Lista todas as localizações adjacentes (8 vizinhos em torno da célula).
     * A lista é embaralhada para reduzir viés de direção.
     *
     * @param location posição de referência.
     * @return lista embaralhada de vizinhos válidos (dentro dos limites).
     */
    public List<Location> adjacentLocations(Location location) {
        List<Location> locations = new ArrayList<>();
        int row = location.getRow();
        int col = location.getCol();
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                int newRow = row + r;
                int newCol = col + c;
                if (r == 0 && c == 0) continue;
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
     *
     * @param location posição de referência.
     * @return lista de posições vagas ao redor da posição fornecida.
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
