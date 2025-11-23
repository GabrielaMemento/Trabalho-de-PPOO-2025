import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public class Fox extends Animal
{
    private static final int BREEDING_AGE = 10;
    private static final int MAX_AGE = 150;
    private static final double BREEDING_PROBABILITY = 0.09;
    private static final int MAX_LITTER_SIZE = 3;
    private static final int RABBIT_FOOD_VALUE = 7;
    private static final Random rand = new Random();
    private int foodLevel;
    
    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with random age.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param BREEDING_AGE 
     */
    public Fox(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location);

        foodLevel = RABBIT_FOOD_VALUE;

        if(randomAge) {
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE) + 1;
        }
    }
    
    @Override
    public void act(List<Animal> newAnimals)
    {
        incrementAge();
        incrementHunger();
        if (!isAlive()) return;

        giveBirth(newAnimals);

        Location newLocation = findFood();
        if (newLocation == null) {
            newLocation = getField().freeAdjacentLocation(getLocation());
        }
        if (newLocation != null) {
            setLocation(newLocation);
        } else {
            setDead(); 
        }
    }

    private int breed() {
        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            return rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return 0;
    }

    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) setDead();
    }
    
    
    private Location findFood() {
        Field field = getField();
        Iterator<Location> it = field.adjacentLocations(getLocation());
        while (it.hasNext()) {
            Location where = it.next();
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

        private void giveBirth(List<Animal> newAnimals) {
        Field field = getField();
        List<Location> free = field.getFreeAdjacent(getLocation());
        int births = breed();
        for (int b = 0; b < births && !free.isEmpty(); b++) {
            Location loc = free.remove(0);
            newAnimals.add(new Fox(false, field, loc));
        }
    }

    @Override
    public int getBreedingAge() 
    { 
        return BREEDING_AGE; 
    }

    @Override
    public int getMaxAge() 
    { 
        return MAX_AGE; 
    }
        

}
