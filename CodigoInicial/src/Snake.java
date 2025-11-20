import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * Modelo simples de uma cobra.
 * Cobras caçam coelhos e se movem bem em vegetação densa.
 * 
 * 
 * 
 */
public class Snake extends Animal {

    // Characteristics shared by all snakes (static fields).

    //Os atributos 'BREEDING_AGE', 'MAX_AGE', 'BREEDING_PROBABILITY', 'foodLevel', 'MAX_LITTER_SIZE', 'FOOD_VALUE' e 'rand' foram movidos para a classe Animal (comum entre todos os animais).
    // BREEDING_AGE = 8;
    // foodLevel = ??;
    // MAX_AGE = 40;
    // BREEDING_PROBABILITY = 0.112;
    // MAX_LITTER_SIZE = 2;
    // FOOD_VALUE = ??;
    // rand = new Random();
    
    // Individual characteristics (instance fields).
    // O atributo 'age', 'alive' e 'location' foram movidos para a classe Animal.
    private static final int RABBIT_FOOD_VALUE = 3; // EM COMUM PARA OS PREDADORES


    private int foodLevel;

    public Snake(boolean randomAge) {
        super();
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        } else {
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }

    @Override
    public void act(Field currentField, Field updatedField, List<Animal> newSnakes) {
        incrementAge(MAX_AGE);
        incrementHunger();
        if(isAlive()) {
            // Reprodução
            int births = breed();
            for(int b = 0; b < births; b++) {
                Snake newSnake = new Snake(false);
                newSnakes.add(newSnake);
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                newSnake.setLocation(loc);
                updatedField.place(newSnake, loc);
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

    private int breed() {
        int births = 0;
        if(getAge() >= BREEDING_AGE && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }
}