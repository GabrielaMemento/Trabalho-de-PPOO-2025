import java.util.List;
import java.util.Iterator;
import java.util.Random;

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
    /** Idade mínima para reprodução. */
    private static final int BREEDING_AGE = 15;
    /** Idade máxima. */
    private static final int MAX_AGE = 200;
    /** Probabilidade de reprodução a cada passo. */
    private static final double BREEDING_PROBABILITY = 0.04;
    /** Tamanho máximo da ninhada. */
    private static final int MAX_LITTER_SIZE = 2;
    /** Valor nutricional do coelho (restaura fome). */
    private static final int RABBIT_FOOD_VALUE = 10;
    /** RNG local. */
    private static final Random RAND = new Random();

    /**
     * Constrói uma águia, possivelmente dando-lhe uma idade aleatória e níveis
     * de fome iniciais aleatórias para diversidade.
     *
     * @param randomAge se true, inicializa com idade e fome aleatórias.
     * @param field campo da simulação.
     * @param location localização inicial.
     */
    public Aguia(boolean randomAge, Field field, Location location) {
        super(field, location);
        setFoodLevel(RABBIT_FOOD_VALUE);
        if (randomAge) {
            setAge(RAND.nextInt(MAX_AGE));
            setFoodLevel(RAND.nextInt(RABBIT_FOOD_VALUE) + 1);
        }
    }

    /**
     * Executa as ações da águia em um passo de simulação:
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
     * Incrementa a idade e verifica morte por velhice.
     */
    @Override
    public void incrementAge() {
        setAge(getAge() + 1);
        if (getAge() > MAX_AGE) setDead();
    }

    /**
     * Calcula o número de filhotes gerados neste passo (se reprodução ocorrer).
     *
     * @return número de novos filhotes (0 se não houve reprodução).
     */
    @Override
    public int breed() {
        if (canBreed() && RAND.nextDouble() <= BREEDING_PROBABILITY) {
            return RAND.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return 0;
    }

    /**
     * Verifica se a águia tem idade suficiente para reproduzir.
     *
     * @return true se idade >= BREEDING_AGE.
     */
    @Override
    public boolean canBreed() { return getAge() >= BREEDING_AGE; }

    /**
     * Incrementa a fome; se chegar a zero, a águia morre de inanição.
     */
    private void incrementHunger() {
        setFoodLevel(getFoodLevel() - 1);
        if (getFoodLevel() <= 0) setDead();
    }

    /**
     * Procura por coelhos nas células adjacentes. Se encontrar um coelho vivo,
     * mata-o, restaura o nível de fome e retorna a localização do coelho (para mover-se).
     *
     * @return localização da comida encontrada ou null se não houver.
     */
    private Location findFood() {
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

    /**
     * Processa o nascimento de novas águias, posicionando-as nas adjacências livres.
     *
     * @param newAnimals lista onde novas águias são adicionadas.
     */
    private void giveBirth(List<Animal> newAnimals) {
        List<Location> free = getField().getFreeAdjacent(getLocation());
        int births = breed();
        for (int b = 0; b < births && !free.isEmpty(); b++) {
            Location loc = free.remove(0);
            newAnimals.add(new Aguia(false, getField(), loc));
        }
    }
}