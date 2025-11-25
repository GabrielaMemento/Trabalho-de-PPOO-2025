import java.util.List;
import java.util.Random;

/**
 * Representa um coelho no ecossistema.
 *
 * - Coelhos são presas principais, com reprodução rápida.
 * - Alimentam-se de plantas (Alecrim, Sálvia), recuperando energia ao consumir.
 * - Movimentação é restrita por tipos de terreno: BURROW, PLAIN, DENSE_VEGETATION.
 *
 * Esta classe foi expandida para consumir plantas e respeitar restrições de terreno,
 * conforme a proposta do grupo.
 *
 * @author Base: Barnes & Kolling
 * @version 2002-04-23 (revisado 2025-11 para plantas e terrenos)
 */
public class Rabbit extends Animal {
    /** Idade mínima para reprodução. */
    private static final int BREEDING_AGE = 5;
    /** Idade máxima (após isso morre de velhice). */
    private static final int MAX_AGE = 40;
    /** Probabilidade de reprodução a cada passo. */
    private static final double BREEDING_PROBABILITY = 0.15;
    /** Tamanho máximo da ninhada. */
    private static final int MAX_LITTER_SIZE = 4;
    /** RNG local para inicializações. */
    private static final Random RAND = new Random();

    /**
     * Cria um novo coelho nascido ou com idade aleatória.
     *
     * @param randomAge se true, atribui idade inicial aleatória.
     * @param field campo da simulação.
     * @param location localização inicial.
     */
    public Rabbit(boolean randomAge, Field field, Location location) {
        super(field, location);
        if (randomAge) {
            this.age = RAND.nextInt(MAX_AGE);
        }
        this.foodLevel = 5; // energia inicial
    }

    /**
     * Executa as ações do coelho em um passo de simulação:
     * - Envelhece e verifica morte por idade.
     * - Consome planta se houver na célula atual.
     * - Tenta reproduzir.
     * - Move para uma célula adjacente permitida pelo terreno.
     * - Pode morrer por superlotação (se não houver espaço).
     *
     * @param newAnimals lista onde novos animais nascidos são adicionados.
     */
    @Override
    public void act(List<Animal> newAnimals) {
        incrementAge();
        if (!isAlive()) return;

        // Consome planta se houver na célula atual
        Object obj = field.getObjectAt(location);
        if (obj instanceof Planta) {
            Planta plant = (Planta) obj;
            if (plant.isAlive()) {
                plant.setDead();
                foodLevel = plant.getValorAlimento(); // recupera energia de acordo com a planta
            }
        }

        // Reprodução
        giveBirth(newAnimals);

        // Movimento restrito por terreno
        Location next = field.freeAdjacentLocation(location);
        if (next != null) {
            Terreno terrain = field.getTerrainAt(next);
            if (terrain == Terreno.BURROW || terrain == Terreno.PLAIN || terrain == Terreno.DENSE_VEGETATION) {
                setLocation(next);
            }
        } else {
            // Sem espaço livre: morre por superlotação
            setDead();
        }
    }

    /**
     * Incrementa a idade e verifica se excedeu o limite.
     */
    @Override
    public void incrementAge() {
        age++;
        if (age > MAX_AGE) setDead();
    }

    /**
     * Retorna o número de filhotes gerados neste passo (se ocorrer reprodução).
     *
     * @return número de nascimentos (0 se não reproduziu).
     */
    @Override
    public int breed() {
        if (canBreed() && RAND.nextDouble() <= BREEDING_PROBABILITY) {
            return RAND.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return 0;
    }

    /**
     * Verifica se o coelho tem idade suficiente para reproduzir.
     *
     * @return true se idade >= BREEDING_AGE.
     */
    @Override
    public boolean canBreed() { return age >= BREEDING_AGE; }

    /**
     * Processa o nascimento de novos coelhos, alocando-os em células adjacentes livres.
     *
     * @param newAnimals lista onde novos coelhos são adicionados.
     */
    private void giveBirth(List<Animal> newAnimals) {
        List<Location> free = field.getFreeAdjacent(location);
        int births = breed();
        for (int b = 0; b < births && !free.isEmpty(); b++) {
            Location loc = free.remove(0);
            newAnimals.add(new Rabbit(false, field, loc));
        }
    }
}
