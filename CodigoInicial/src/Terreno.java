/**
 * Tipos de terreno possíveis no campo da simulação.
 */
public enum Terreno {
    PLAIN,          // campo aberto
    BURROW,         // toca de coelho
    CAVE,           // caverna (raposas e lobos)
    MOUNTAIN,       // montanha (bloqueia animais terrestres)
    RIVER,          // rio (barreira natural)
    DENSE_VEGETATION // vegetação densa (cobra se camufla melhor)
}