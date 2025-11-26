import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Representa um lobo no ecossistema.
 * 
 * - Lobos são predadores de topo, caçam raposas e coelhos
 * - São mais fortes que raposas mas se reproduzem mais lentamente
 * - Movimentação é restrita: não atravessam rios, mas podem passar por montanhas
 * - Têm maior longevidade e necessitam de mais comida
 * 
 * @author Leonardo Elias Rodrigues
 * @version 2025-11
 */
public class Lobo extends Animal {
    /** Idade mínima para reprodução. */
    private static final int BREEDING_AGE = 15;
    /** Idade máxima. */
    private static final int MAX_AGE = 180;
    /** Probabilidade de reprodução a cada passo. */
    private static final double BREEDING_PROBABILITY = 0.06;
    /** Tamanho máximo da ninhada. */
    private static final int MAX_LITTER_SIZE = 2;
    /** Valor nutricional de uma raposa. */
    private static final int FOX_FOOD_VALUE = 10;
    /** Valor nutricional de um coelho. */
    private static final int RABBIT_FOOD_VALUE = 7;
    /** RNG local. */
    private static final Random RAND = new Random();

    /**
     * Construtor compatível (3 parâmetros)
     */
    public Lobo(boolean randomAge, Field field, Location location) {
        this(randomAge, field, location, null);
    }

    /**
     * Construtor com ControladorClima (4 parâmetros)
     */
    public Lobo(boolean randomAge, Field field, Location location, ControladorClima controladorClima) {
        super(field, location, controladorClima);
        foodLevel = FOX_FOOD_VALUE;
        if (randomAge) {
            this.age = RAND.nextInt(MAX_AGE);
            this.foodLevel = RAND.nextInt(FOX_FOOD_VALUE) + 1;
        }
    }

    /**
     * Executa as ações do lobo em um passo de simulação:
     * - Envelhece e aumenta a fome
     * - Tenta reproduzir
     * - Procura comida (raposas e coelhos) nas adjacências
     * - Move-se respeitando restrições de terreno
     * - Morre se não encontrar comida ou espaço
     */
    @Override
    public void act(List<Animal> newAnimals) {
        incrementAge();
        incrementHunger();
        if (!isAlive()) return;

        giveBirth(newAnimals);

        // Procura comida (raposas e coelhos) nas adjacências
        Location newLocation = findFood();
        if (newLocation == null) {
            // Caso não encontre comida, tenta mover para um espaço livre
            newLocation = encontrarProximaLocalizacao();
        }

        // Movimento respeitando restrições de terreno
        if (newLocation != null) {
            setLocation(newLocation);
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
        age++;
        if (age > MAX_AGE) setDead();
    }

    /**
     * Calcula o número de filhotes gerados neste passo.
     */
    @Override
    public int breed() {
        double probabilidadeBase = BREEDING_PROBABILITY;
        if (controladorClima != null) {
            probabilidadeBase *= controladorClima.getMultiplicadorReproducao();
        }
        
        if (canBreed() && RAND.nextDouble() <= probabilidadeBase) {
            return RAND.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return 0;
    }

    /**
     * Verifica se o lobo tem idade suficiente para reproduzir.
     */
    @Override
    public boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    /**
     * Implementação da interface Movel - define restrições de terreno.
     * Lobos podem atravessar montanhas mas não rios.
     */
    @Override
    public boolean podeMoverPara(Terreno terreno) {
        return terreno != Terreno.RIVER;
        // Lobos são fortes e podem atravessar montanhas, mas não nadam bem
    }

    /**
     * Incrementa a fome; se chegar a zero, o lobo morre de inanição.
     */
    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) setDead();
    }

    /**
     * Procura por raposas e coelhos nas células adjacentes.
     * Prefere raposas (mais nutritivas) mas come coelhos se necessário.
     * 
     * @return localização da comida encontrada ou null se não houver.
     */
    private Location findFood() {
        Iterator<Location> adjacent = field.adjacentLocations(location).iterator();
        
        // Primeiro procura por raposas (mais nutritivas)
        while (adjacent.hasNext()) {
            Location where = adjacent.next();
            Object obj = field.getObjectAt(where);
            if (obj instanceof Fox) {
                Fox fox = (Fox) obj;
                if (fox.isAlive()) {
                    fox.setDead();
                    foodLevel = FOX_FOOD_VALUE;
                    return where;
                }
            }
        }
        
        // Se não encontrou raposas, procura por coelhos
        adjacent = field.adjacentLocations(location).iterator();
        while (adjacent.hasNext()) {
            Location where = adjacent.next();
            Object obj = field.getObjectAt(where);
            if (obj instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) obj;
                if (rabbit.isAlive()) {
                    rabbit.setDead();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
        }
        
        return null;
    }

    /**
     * Processa o nascimento de novos lobos, posicionando-os nas adjacências livres.
     */
    private void giveBirth(List<Animal> newAnimals) {
        List<Location> free = field.getFreeAdjacent(location);
        int births = breed();
        for (int b = 0; b < births && !free.isEmpty(); b++) {
            Location loc = free.remove(0);
            newAnimals.add(new Lobo(false, field, loc, controladorClima));
        }
    }
}
