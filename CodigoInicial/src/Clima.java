/**
 * Tipos de condições climáticas que afetam a simulação
 */
public enum Clima {
    NORMAL,     // Condições normais - sem efeitos especiais
    SECA,       // Reduz disponibilidade de plantas
    CHUVA,      // Aumenta reprodução de alguns animais
    NEVE,       // Reduz movimento e reprodução
    TEMPESTADE  // Afeta negativamente todos os animais
}