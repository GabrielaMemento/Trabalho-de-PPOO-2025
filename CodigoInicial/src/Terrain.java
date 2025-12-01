/**
 * Tipos de terreno possíveis no campo da simulação.
 * @author Grupo 1
 * @version 2025
 */
public enum Terrain {
    PLAIN,          // Campo aberto
    BURROW,         // Toca (ex: coelho)
    CAVE,           // Caverna (ex: raposa, lobo)
    MOUNTAIN,       // Montanha (bloqueia a maioria dos terrestres)
    RIVER,          // Rio (barreira natural)
    DENSE_VEGETATION // Vegetação densa
}