/**
 * Classe utilitária que fornece um contador simples para um participante da simulação.
 * Usada pela classe {@link FieldStats} para calcular e exibir estatísticas populacionais.
 * @author Grupo 1
 * @version 2025
 */
public class Counter {
    /** Nome identificador do tipo de participante (ex.: "Fox", "Rabbit"). */
    private final String name;
    /** Quantidade atual desse tipo de participante na simulação. */
    private int count;

    /**
     * Construtor: cria um contador para um tipo específico.
     * Inicialmente o valor é zero.
     * @param name Nome do tipo (ex.: "Fox").
     */
    public Counter(String name) {
        this.name = name;
        this.count = 0;
    }
    
    /**
     * @return O nome identificador deste tipo.
     */
    public String getName() {
        return name;
    }

    /**
     * @return O valor atual do contador.
     */
    public int getCount() {
        return count;
    }

    /**
     * Incrementa o contador em uma unidade.
     */
    public void increment() {
        count++;
    }
    
    /**
     * Reseta o contador para zero.
     * Usado antes de recalcular estatísticas.
     */
    public void reset() {
        count = 0;
    }
}