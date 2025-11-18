import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Mapa de terreno que define o tipo de solo em cada célula do campo.
 * Lido a partir de um arquivo de texto.
 * 
 *
 *
 */
public class TerrainMap {
    private TerrainType[][] terrain;

    /**
     * Constrói o mapa a partir de um arquivo.
     * Cada caractere representa um tipo de terreno.
     */
    public TerrainMap(String filePath, int depth, int width) throws IOException {
        terrain = new TerrainType[depth][width];
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int row = 0;

        while ((line = reader.readLine()) != null && row < depth) {
            for (int col = 0; col < Math.min(line.length(), width); col++) {
                terrain[row][col] = charToTerrain(line.charAt(col));
            }
            row++;
        }

        reader.close();
    }

    /**
     * Converte um caractere do arquivo em um tipo de terreno.
     */
    private TerrainType charToTerrain(char c) {
        switch (c) {
            case '.': return TerrainType.OPEN_FIELD;
            case '#': return TerrainType.MOUNTAIN;
            case '~': return TerrainType.RIVER;
            case '^': return TerrainType.DENSE_VEGETATION;
            case 'C': return TerrainType.CAVE;
            case 'T': return TerrainType.BURROW;
            default: return TerrainType.OPEN_FIELD;
        }
    }

    /**
     * Retorna o tipo de terreno em uma localização.
     */
    public TerrainType getTerrainAt(Location loc) {
        return terrain[loc.getRow()][loc.getCol()];
    }

    /**
     * Verifica se um ator pode se mover para uma determinada célula.
     */
    public boolean isAccessible(Animal actor, Location loc) {
        TerrainType type = getTerrainAt(loc);

        if (actor instanceof Eagle) return true; // Águia ignora restrições
        if (actor instanceof Rabbit) return type != TerrainType.MOUNTAIN;
        if (actor instanceof Fox || actor instanceof Wolf) return type != TerrainType.MOUNTAIN && type != TerrainType.RIVER;
        if (actor instanceof Snake) return type != TerrainType.MOUNTAIN && type != TerrainType.RIVER && type != TerrainType.CAVE;
        if (actor instanceof Hunter) return type != TerrainType.CAVE && type != TerrainType.BURROW;

        return true;
    }
}