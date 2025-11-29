import java.util.List;
import java.util.Iterator;

/**
 * Representa uma raposa no ecossistema.
 *
 * - Raposas são predadoras de coelhos.
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
public class Fox extends Animal {
    /** Idade mínima para reprodução. */
    private static final int BREEDING_AGE = 10;
    /** Idade máxima. */
    private static final int MAX_AGE = 150;
    /** Probabilidade de reprodução a cada passo. */
    private static final double BREEDING_PROBABILITY = 0.09;
    /** Tamanho máximo da ninhada. */
    private static final int MAX_LITTER_SIZE = 3;
    /** Valor nutricional do coelho (restaura fome). */
    private static final int FOOD_VALUE = 7;

    /**
     * Constrói uma raposa, possivelmente dando-lhe uma idade aleatória e níveis
     * de fome iniciais aleatórias para diversidade.
     *
     * @param randomAge se true, inicializa com idade e fome aleatórias.
     * @param field campo da simulação.
     * @param location localização inicial.
     */
    public Fox(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location);
    }

    /**
     * Executa as ações da raposa em um passo de simulação:
     * - Envelhece e aumenta a fome.
     * - Tenta reproduzir.
     * - Procura comida (coelho) nas adjacências; se encontrar, move-se para lá.
     * - Caso não encontre, tenta mover-se para uma célula adjacente livre.
     * - Respeita restrições de terreno: não entra em RIVER ou MOUNTAIN.
     * - Morre de fome se foodLevel chegar a zero; pode morrer por superlotação.
     *
     * @param newAnimals lista onde novos animais nascidos são adicionados.
     */
    @Override
    public void act(List<Animal> newAnimals) {
        incrementAge();
        incrementHunger();
        if (!isAlive()) return;

        giveBirth(newAnimals);

        // Procura comida (coelhos) nas adjacências
        Location newLocation = findFood();
        if (newLocation == null) {
            // Caso não encontre comida, tenta mover para um espaço livre
            newLocation = getField().freeAdjacentLocation(getLocation());
        }

        // Movimento respeitando restrições de terreno
        if (newLocation != null) {
            Terreno terrain = getField().getTerrainAt(newLocation);
            if (terrain != Terreno.MOUNTAIN && terrain != Terreno.RIVER) {
                setLocation(newLocation);
            }
        } else {
            // Sem espaço livre: morre por superlotação
            setDead();
        }
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
    public int getMaxAge() { 
        return MAX_AGE; 
    }

    @Override
    public double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }

    @Override
    public int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }

    public int getFoodValue() {
        return FOOD_VALUE;
    }

}