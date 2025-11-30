import java.util.Iterator;

/**
 * Representa uma águia no ecossistema.
 *
 * - Águias são predadoras de coelhos.
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
public class Aguia extends Animal {

    private static final int BREEDING_AGE = 15;
    private static final int MAX_AGE = 200;
    private static final double BREEDING_PROBABILITY = 0.04;
    private static final int MAX_LITTER_SIZE = 2;
    private static final int FOOD_VALUE = 10;
    
    /**
     * Constrói uma águia, possivelmente dando-lhe uma idade aleatória e níveis
     * de fome iniciais aleatórias para diversidade.
     *
     * @param randomAge se true, inicializa com idade e fome aleatórias.
     * @param field campo da simulação.
     * @param location localização inicial.
     */
    public Aguia(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location);
    }


    /**
     * Procura por coelhos nas células adjacentes. Se encontrar um coelho vivo,
     * mata-o, restaura o nível de fome e retorna a localização do coelho (para mover-se).
     *
     * @return localização da comida encontrada ou null se não houver.
     */
    public Location findFood() {
        Iterator<Location> adjacent = getField().adjacentLocations(getLocation()).iterator();
        while (adjacent.hasNext()) {
            Location where = adjacent.next();
            Object obj = getField().getObjectAt(where);
            if (obj instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) obj;
                if (rabbit.isAlive()) {
                    rabbit.setDead();
                    setFoodLevel(FOOD_VALUE);
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

    public int getFoodValue() {
        return FOOD_VALUE;
    }

}