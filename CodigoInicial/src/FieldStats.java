import java.util.HashMap;

/**
 * Responsável por calcular e manter estatísticas de população no campo.
 * - Armazena contagens por classe usando um mapa de {@link Counter}.
 * - Permite verificar se a simulação ainda é viável (mais de uma espécie presente).
 *
 * @author Grupo 1
 * @version 2025
 */
public class FieldStats {
    
    /** Mapa de contadores, indexado pela classe de cada entidade (Animal, Plant, etc.). */
    private final HashMap<Class<?>, Counter> counters = new HashMap<>();
    /** Indica se as contagens atuais são válidas ou precisam ser recalculadas. */
    private boolean countsValid = false;

    /**
     * Retorna uma string com os detalhes da população atual.
     *
     * @param field Campo da simulação.
     * @return string com nome e quantidade de cada classe presente.
     */
    public String getPopulationDetails(Field field) {
        if (!countsValid) generateCounts(field);
        
        StringBuilder buffer = new StringBuilder();
        for (Counter c : counters.values()) {
            // Apenas mostra contagens > 0
            if (c.getCount() > 0) {
                buffer.append(c.getName()).append(": ").append(c.getCount()).append(" ");
            }
        }
        return buffer.toString().trim();
    }

    /**
     * Invalida as contagens atuais e zera todos os contadores.
     */
    public void reset() {
        countsValid = false;
        for (Counter c : counters.values()) c.reset();
    }

    /**
     * Incrementa o contador associado a uma classe específica.
     * @param entityClass Classe da entidade (ex.: Rabbit.class, Fox.class).
     */
    public void incrementCount(Class<?> entityClass) {
        Counter cnt = counters.get(entityClass);
        if (cnt == null) {
            cnt = new Counter(entityClass.getSimpleName());
            counters.put(entityClass, cnt);
        }
        cnt.increment();
    }

    /**
     * Verifica se a simulação ainda é viável.
     * Critério: deve haver mais de uma espécie com população > 0.
     *
     * @param field Campo da simulação.
     * @return true se mais de uma espécie está presente; false caso contrário.
     */
    public boolean isViable(Field field) {
        if (!countsValid) generateCounts(field);
        
        long nonZeroCount = counters.values().stream()
                                    .filter(c -> c.getCount() > 0)
                                    .count();
                                    
        return nonZeroCount > 1;
    }

    /**
     * Percorre todo o campo e gera novas contagens de população.
     * @param field Campo da simulação.
     */
    private void generateCounts(Field field) {
        reset();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Object obj = field.getObjectAt(new Location(row, col));
                if (obj != null) {
                    incrementCount(obj.getClass());
                }
            }
        }
        countsValid = true;
    }

    /**
     * Marca a contagem como finalizada (válida).
     */
    public void countFinished() {
        countsValid = true;
    }
}