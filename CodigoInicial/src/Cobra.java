import java.util.Iterator;

/**
 * Representa uma cobra no ecossistema.
 *
 * - Cobras são predadoras de coelhos.
 * - Movimentação é restringida por tipos de terreno: não atravessam RIVER ou MOUNTAIN.
 * - Procuram por comida nas células adjacentes e caçam coelhos.
 * - Reproduzem-se com probabilidade menor que coelhos e têm maior longevidade.
 *
 * Esta classe foi expandida para respeitar restrições de terreno e manter
 * compatibilidade com a lógica original da simulação.
 *
 * @author Base: Barnes & Kolling  
 * @version 2002-04-23 (revisado 2025-11 para terrenos)
 */
public class Cobra extends Animal {
    /** Idade mínima para reprodução. */
    private static final int BREEDING_AGE = 8;
    /** Idade máxima. */
    private static final int MAX_AGE = 100;
    /** Probabilidade de reprodução a cada passo. */
    private static final double BREEDING_PROBABILITY = 0.07;
    /** Tamanho máximo da ninhada. */
    private static final int MAX_LITTER_SIZE = 4;
    /** Valor nutricional do coelho (restaura fome). */
    private static final int RABBIT_FOOD_VALUE = 6;
    /** RNG local. */

    /**
     * Constrói uma cobra, possivelmente dando-lhe uma idade aleatória e níveis
     * de fome iniciais aleatórias para diversidade.
     *
     * @param randomAge se true, inicializa com idade e fome aleatórias.
     * @param field campo da simulação.
     * @param location localização inicial.
     */
    public Cobra(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location);
    }

    /**
     * Procura por coelhos nas células adjacentes. Se encontrar um coelho vivo,
     * mata-o, restaura o nível de fome e retorna a localização do coelho (para mover-se).
     *
     * @return localização da comida encontrada ou null se não houver.
     */
    @Override
    public Location findFood() {
        Iterator<Location> adjacent = getField().adjacentLocations(getLocation()).iterator();
        while (adjacent.hasNext()) {
            Location where = adjacent.next();
            Object obj = getField().getObjectAt(where);
            if (obj instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) obj;
                if (rabbit.isAlive()) {
                    rabbit.setDead();
                    setFoodLevel(RABBIT_FOOD_VALUE);
                    return where;
                }
            }
        }
        return null;
    }

    @Override
    public int getBreedingAge() {
        return BREEDING_AGE;
    }

    @Override
    public double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }

    @Override
    public int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

}