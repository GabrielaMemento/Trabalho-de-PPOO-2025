/**
 * Classe responsável por contar participantes de um tipo específico na simulação.
 * 
 * Cada instância de {@code Counter} representa uma espécie ou elemento
 * (ex.: "Raposa", "Coelho") e mantém o número atual de indivíduos desse tipo
 * presentes no campo da simulação.
 *
 * É utilizada pela classe {@link FieldStats} para calcular estatísticas
 * populacionais e exibir os resultados na interface gráfica.
 *
 */
public class Counter {
    // Nome que identifica o tipo de participante (ex.: "Raposa")
    private String name;
    // Quantidade atual desse tipo na simulação
    private int count;

    /**
     * Cria um contador para um tipo específico
     *
     * @param name nome do tipo (ex.: "Coelho")
     */
    public Counter(String name) {
        this.name = name;
        count = 0;
    }

    // @return o nome do tipo associado a este contador
    public String getName() {
        return name;
    }

    // @return o valor atual do contador. 
    public int getCount() {
        return count;
    }

    // Incrementa o contador em uma unidade
    public void increment() {
        count++;
    }

    // Reseta o contador para zero
    public void reset() {
        count = 0;
    }
}
