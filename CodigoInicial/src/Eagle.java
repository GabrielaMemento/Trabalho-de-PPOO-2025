import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * Modelo simples de uma águia.
 * Águias caçam coelhos e cobras, sobrevoam obstáculos e pousam em áreas abertas para caçar.
 * 
 * 
 * 
 */
public class Eagle extends Animal {
    private static final int BREEDING_AGE = 12;
    private static final int MAX_AGE = 80;
    private static final double BREEDING_PROBABILITY = 0.08;
    private static final int MAX_LITTER_SIZE = 2;
    private static final int FOOD_VALUE = 5;
    private static final Random rand = new Random();

    private int foodLevel;

    public Eagle(boolean randomAge) {
        super();
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            foodLevel = rand.nextInt(FOOD_VALUE);
        } else {
            foodLevel = FOOD_VALUE;
        }
    }

    @Override
    public void act(Field currentField, Field updatedField, List<Animal> newEagles) {
        incrementAge(MAX_AGE);
        incrementHunger();
        if(isAlive()) {
            // Reprodução
            int births = breed();
            for(int b = 0; b < births; b++) {
                Eagle newEagle = new Eagle(false);
                newEagles.add(newEagle);
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                newEagle.setLocation(loc);
                updatedField.place(newEagle, loc);
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

    private void incrementHunger() {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    private Location findFood(Field field, Location location) {
        Iterator adjacent = field.adjacentLocations(location);
        while(adjacent.hasNext()) {
            Location where = (Location) adjacent.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit || animal instanceof Snake) {
                Animal prey = (Animal) animal;
                if(prey.isAlive()) {
                    prey.setDead();
                    foodLevel = FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }

    private int breed() {
        int births = 0;
        if(getAge() >= BREEDING_AGE && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }
}