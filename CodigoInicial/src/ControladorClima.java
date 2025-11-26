import java.util.Random;

/**
 * Controla as condições climáticas da simulação
 */
public class ControladorClima {
    private Clima climaAtual;
    private int duracaoClimaAtual;
    private int passosParaMudanca;
    private Random rand;
    
    // Probabilidades de cada clima (soma = 1.0)
    private static final double PROB_NORMAL = 0.60;
    private static final double PROB_SECA = 0.15;
    private static final double PROB_CHUVA = 0.15;
    private static final double PROB_NEVE = 0.07;
    private static final double PROB_TEMPESTADE = 0.03; // AGORA É USADA
    
    // Limites acumulados para verificação explícita
    private static final double LIMITE_NORMAL = PROB_NORMAL;
    private static final double LIMITE_SECA = LIMITE_NORMAL + PROB_SECA;
    private static final double LIMITE_CHUVA = LIMITE_SECA + PROB_CHUVA;
    private static final double LIMITE_NEVE = LIMITE_CHUVA + PROB_NEVE;
    private static final double LIMITE_TEMPESTADE = LIMITE_NEVE + PROB_TEMPESTADE; // = 1.0
    
    public ControladorClima() {
        this.rand = new Random();
        this.climaAtual = Clima.NORMAL;
        this.duracaoClimaAtual = 0;
        this.passosParaMudanca = 10 + rand.nextInt(20);
    }
    
    public void atualizar() {
        duracaoClimaAtual++;
        
        if (duracaoClimaAtual >= passosParaMudanca) {
            mudarClima();
            duracaoClimaAtual = 0;
            passosParaMudanca = 10 + rand.nextInt(20);
        }
    }
    
    private void mudarClima() {
        double probabilidade = rand.nextDouble();
        
        // USO EXPLÍCITO DE TODAS AS CONSTANTES
        if (probabilidade < LIMITE_NORMAL) {
            climaAtual = Clima.NORMAL;
        } else if (probabilidade < LIMITE_SECA) {
            climaAtual = Clima.SECA;
        } else if (probabilidade < LIMITE_CHUVA) {
            climaAtual = Clima.CHUVA;
        } else if (probabilidade < LIMITE_NEVE) {
            climaAtual = Clima.NEVE;
        } else if (probabilidade < LIMITE_TEMPESTADE) { // USA PROB_TEMPESTADE
            climaAtual = Clima.TEMPESTADE;
        } else {
            // Fallback - não deve acontecer pois LIMITE_TEMPESTADE = 1.0
            climaAtual = Clima.NORMAL;
        }
    }
    
    // ... resto dos métodos permanece igual ...
    
    public double getMultiplicadorReproducao() {
        switch(climaAtual) {
            case CHUVA: return 1.3;
            case SECA: return 0.8;
            case NEVE: return 0.6;
            case TEMPESTADE: return 0.4; // USA TEMPESTADE
            default: return 1.0;
        }
    }
    
    public double getMultiplicadorComida() {
        switch(climaAtual) {
            case CHUVA: return 1.2;
            case SECA: return 0.5;
            case NEVE: return 0.3;
            case TEMPESTADE: return 0.7; // USA TEMPESTADE
            default: return 1.0;
        }
    }
    
    public double getMultiplicadorMovimento() {
        switch(climaAtual) {
            case NEVE: return 0.7;
            case TEMPESTADE: return 0.5; // USA TEMPESTADE
            default: return 1.0;
        }
    }
    
    public Clima getClimaAtual() { return climaAtual; }
    public int getDuracaoClimaAtual() { return duracaoClimaAtual; }
    public int getPassosParaMudanca() { return passosParaMudanca - duracaoClimaAtual; }
}