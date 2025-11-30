/**
 * Classe utilitária que fornece um contador para um participante da simulação.
 * 
 * Cada instância de Counter representa uma espécie ou tipo de objeto
 * dentro do simulador (por exemplo, "Fox" ou "Rabbit").
 * 
 * Ela armazena:
 * - Um nome identificador (string).
 * - Um valor numérico (count) que indica quantos objetos desse tipo existem
 *   no campo em determinado momento.
 * 
 * É usada pela classe FieldStats para calcular e exibir as estatísticas
 * populacionais na interface gráfica.
 * 
 * @author David J. Barnes
 * @author Michael Kolling
 * @version 2002-04-23
 */
public class Counter {
    /** Nome identificador do tipo de participante (ex.: "Fox", "Rabbit"). */
    private String name;
    /** Quantidade atual desse tipo de participante na simulação. */
    private int count;

    /**
     * Construtor: cria um contador para um tipo específico.
     * Inicialmente o valor é zero.
     * 
     * @param name Nome do tipo (ex.: "Fox").
     */
    public Counter(String name) {
        this.name = name;
        count = 0;
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
     * Usado quando um novo objeto desse tipo é encontrado no campo.
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
