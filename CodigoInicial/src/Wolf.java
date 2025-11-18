import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Modelo simples de um lobo.
 * Lobos caçam coelhos, competem com raposas e vivem em cavernas.
 * 
 *
 *
 */
public class Wolf extends Animal {
    // Idade mínima para reprodução
    private static final int BREEDING_AGE = 12;
    // Idade máxima
    private static final int MAX_AGE = 100;
    // Probabilidade de reprodução
    private static final double BREEDING_PROBABILITY = 0.07;
    // Tamanho máximo da ninhada
    private static final int MAX_LITTER_SIZE = 2;
    // Valor nutricional de um coelho
    private static final int RABBIT_FOOD_VALUE = 5;
    // Gerador de números aleatórios
    private static final Random rand = new Random();

    // Nível de fome
    private int foodLevel;

    /**
     * Cria um lobo com idade aleatória ou recém-nascido.
     */
    public Wolf(boolean randomAge) {
        super();
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        } else {
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }

    /**
     * Define o comportamento do lobo a cada passo da simulação.
     */
    @Override
    public void act(Field currentField, Field updatedField, List<Animal> newWolves) {
        incrementAge(MAX_AGE);
        incrementHunger();
        if(isAlive()) {
            // Reprodução
            int births = breed();
            for(int b = 0; b < births; b++) {
                Wolf newWolf = new Wolf(false);
                newWolves.add(newWolf);
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                newWolf.setLocation(loc);
                updatedField.place(newWolf, loc);
            }

            // Caça
            Location newLocation = findFood(currentField, getLocation());
            if(newLocation == null) {
                newLocation = updatedField.freeAdjacentLocation(getLocation());
            }

            if(newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                setDead(); // superpopulação
            }
        }
    }

    /**
     * Aumenta a fome do lobo.
     */
    private void incrementHunger() {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Procura por coelhos nas proximidades para se alimentar.
     */
    private Location findFood(Field field, Location location) {
        Iterator adjacent = field.adjacentLocations(location);
        while(adjacent.hasNext()) {
            Location where = (Location) adjacent.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) {
                    rabbit.setEaten();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Define o número de filhotes gerados.
     */
    private int breed() {
        int births = 0;
        if(getAge() >= BREEDING_AGE && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }
}