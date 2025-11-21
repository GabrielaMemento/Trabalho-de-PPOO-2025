import java.util.HashMap;
import java.util.Iterator;

/**
 * Classe responsável por coletar e fornecer estatísticas sobre o estado do campo
 * 
 * O {@code FieldStats} mantém contadores para cada tipo de entidade presente
 * na simulação (raposas, coelhos, plantas, caçadores, etc.). É flexível: cria
 * automaticamente um contador para qualquer classe encontrada no campo
 *
 * Essas estatísticas são utilizadas pela interface gráfica {@link SimulatorView}
 * para exibir informações populacionais e verificar se a simulação ainda é viável
 *
 */
public class FieldStats {
    // Mapa de contadores para cada tipo de entidade
    private HashMap<Class, Counter> counters;
    // Indica se os contadores estão atualizados
    private boolean countsValid;

    // Constrói um objeto de estatísticas do campo
    public FieldStats() {
        counters = new HashMap<>();
        countsValid = true;
    }

    /**
     * Retorna uma string com detalhes populacionais do campo
     * Exemplo: "Raposa: 10 Coelho: 25"
     *
     * @param field campo da simulação
     * @return string com estatísticas populacionais
     */
    public String getPopulationDetails(Field field) {
        StringBuffer buffer = new StringBuffer();
        if(!countsValid) {
            generateCounts(field);
        }
        Iterator<Class> keys = counters.keySet().iterator();
        while(keys.hasNext()) {
            Counter info = counters.get(keys.next());
            buffer.append(info.getName());
            buffer.append(": ");
            buffer.append(info.getCount());
            buffer.append(' ');
        }
        return buffer.toString();
    }

    
     //Invalida as estatísticas atuais e reseta todos os contadores.
    public void reset() {
        countsValid = false;
        Iterator<Class> keys = counters.keySet().iterator();
        while(keys.hasNext()) {
            Counter cnt = counters.get(keys.next());
            cnt.reset();
        }
    }

    /**
     * Incrementa o contador de uma classe de entidade
     * Cria um novo contador se ainda não existir
     *
     * @param entityClass classe da entidade (ex.: Raposa, Coelho)
     */
    public void incrementCount(Class entityClass) {
        Counter cnt = counters.get(entityClass);
        if(cnt == null) {
            cnt = new Counter(entityClass.getName());
            counters.put(entityClass, cnt);
        }
        cnt.increment();
    }

    // Marca que os contadores estão atualizados. 
    public void countFinished() {
        countsValid = true;
    }

    /**
     * Verifica se a simulação ainda é viável
     * Considera viável se houver mais de uma espécie viva
     *
     * @param field campo da simulação
     * @return true se houver mais de uma espécie com população > 0
     */
    public boolean isViable(Field field) {
        int nonZero = 0;
        if(!countsValid) {
            generateCounts(field);
        }
        Iterator<Class> keys = counters.keySet().iterator();
        while(keys.hasNext()) {
            Counter info = counters.get(keys.next());
            if(info.getCount() > 0) {
                nonZero++;
            }
        }
        return nonZero > 1;
    }

    /**
     * Gera contadores atualizados percorrendo todo o campo
     * Este método é chamado apenas quando necessário
     *
     * @param field campo da simulação
     */
    private void generateCounts(Field field) {
        reset();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object entity = field.getObjectAt(row, col);
                if(entity != null) {
                    incrementCount(entity.getClass());
                }
            }
        }
        countsValid = true;
    }
}
