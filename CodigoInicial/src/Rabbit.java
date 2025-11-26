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
    public Rabbit(boolean randomAge, Field field, Location location, ControladorClima controladorClima) {
        super(field, location, controladorClima); // ← PASSAR controladorClima para super
        if (randomAge) {
            this.age = RAND.nextInt(MAX_AGE);
        }
        this.foodLevel = 5;
    }
    /**
     * Ações do coelho considerando efeitos do clima atualizado.@author Leonardo Elias Rodrigues
     */
    @Override
    public void act(List<Animal> newAnimals) {
        incrementAge();
        if (!isAlive()) return;

        // Consome planta (com efeito do clima na disponibilidade)
        if (RAND.nextDouble() < controladorClima.getMultiplicadorComida()) {
            Object obj = field.getObjectAt(location);
            if (obj instanceof Planta) {
                Planta plant = (Planta) obj;
                if (plant.podeSerComido()) {
                    plant.foiComido();
                    foodLevel += plant.getValorNutricional();
                }
            }
        }

        // Reprodução com efeito do clima
        giveBirth(newAnimals);

        // Movimento usando a interface Movel
        Location next = encontrarProximaLocalizacao();
        if (next != null) {
            setLocation(next);
        } else {
            // Sem espaço livre: morre por superlotação
            setDead();
        }
    }

    // Implementação do método abstrato da interface Movel
    @Override
    public boolean podeMoverPara(Terreno terreno) {
        // Coelhos podem se mover em tocas, campos abertos e vegetação densa
        return  terreno == Terreno.BURROW ||
                terreno == Terreno.PLAIN ||
                terreno == Terreno.DENSE_VEGETATION;
    }

    // Mantidos da versão anterior (com ajustes para clima)
    @Override
    public void incrementAge() {
        age++;
        if (age > MAX_AGE) setDead();
    }

    @Override
    public int breed() {
        double probabilidadeComClima = BREEDING_PROBABILITY * controladorClima.getMultiplicadorReproducao();
        if (canBreed() && RAND.nextDouble() <= probabilidadeComClima) {
            return RAND.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return 0;
    }

    @Override
    public boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    private void giveBirth(List<Animal> newAnimals) {
        List<Location> free = field.getFreeAdjacent(location);
        int births = breed();
        for (int b = 0; b < births && !free.isEmpty(); b++) {
            Location loc = free.remove(0);
            // USAR O CONSTRUTOR CORRETO COM ControladorClima
            newAnimals.add(new Rabbit(false, field, loc, this.controladorClima));
        }
    }
}