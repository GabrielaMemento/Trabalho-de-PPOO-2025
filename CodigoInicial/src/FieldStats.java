import java.util.HashMap;

/**
 * Responsável por calcular e manter estatísticas de população no campo.
 *
 * - Conta quantos objetos de cada classe (animais, plantas) existem no campo.
 * - Armazena os resultados em um mapa de contadores (Counter).
 * - Permite verificar se a simulação ainda é viável (mais de uma espécie presente).
 *
 * Convenções:
 * - As contagens são recalculadas apenas quando necessário (lazy evaluation).
 * - O método {@link #reset()} invalida as contagens, forçando nova geração.
 * - O método {@link #generateCounts(Field)} percorre todo o campo e atualiza os contadores.
 *
 * Usado principalmente pela classe {@link SimulatorView} para exibir estatísticas
 * e determinar se a simulação deve continuar.
 *
 * @author
 *   Base: Barnes & Kolling
 * @version
 *   2002-04-23 (comentado e revisado em 2025-11)
 */
public class FieldStats {
    /** Mapa de contadores, indexado pela classe de cada entidade (Animal, Plant, etc.). */
    private final HashMap<Class<?>, Counter> counters = new HashMap<>();
    /** Indica se as contagens atuais são válidas ou precisam ser recalculadas. */
    private boolean countsValid = true;

    /**
     * Retorna uma string com os detalhes da população atual.
     * Exemplo: "Rabbit: 25 Fox: 10 Alecrim: 15"
     *
     * @param field campo da simulação.
     * @return string com nome e quantidade de cada classe presente.
     */
    public String getPopulationDetails(Field field) {
        if (!countsValid) generateCounts(field);
        StringBuilder buffer = new StringBuilder();
        for (Counter c : counters.values()) {
            buffer.append(c.getName()).append(": ").append(c.getCount()).append(" ");
        }
        return buffer.toString().trim();
    }

    /**
     * Invalida as contagens atuais e zera todos os contadores.
     * Deve ser chamado antes de recalcular estatísticas.
     */
    public void reset() {
        countsValid = false;
        for (Counter c : counters.values()) c.reset();
    }

    /**
     * Incrementa o contador associado a uma classe específica.
     * Se ainda não existir contador para a classe, cria um novo.
     *
     * @param entityClass classe da entidade (ex.: Rabbit.class, Fox.class).
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
     * @param field campo da simulação.
     * @return true se mais de uma espécie está presente; false caso contrário.
     */
    public boolean isViable(Field field) {
        if (!countsValid) generateCounts(field);
        int nonZero = 0;
        for (Counter c : counters.values()) {
            if (c.getCount() > 0) nonZero++;
        }
        return nonZero > 1;
    }

    /**
     * Percorre todo o campo e gera novas contagens de população.
     * - Zera os contadores existentes.
     * - Para cada célula ocupada, incrementa o contador da classe correspondente.
  *   
     * @param field campo da simulação.
     */
    private void generateCounts(Field field) {
        reset();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Object obj = field.getObjectAt(new Location(row, col));
                if (obj != null) incrementCount(obj.getClass());
            }
        }
        countsValid = true;
    }

    /**
     * Compatibilidade com versões antigas: finaliza a contagem.
     * Se sua lógica não usar este método, pode ser omitido.
     */
    public void countFinished() {
        countsValid = true;
    }
}
