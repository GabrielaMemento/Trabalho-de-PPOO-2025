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
    private static final int RABBIT_FOOD_VALUE = 4;
    private static final int MAX_FOOD_VALUE = 20; 
    
    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with random age.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param BREEDING_AGE 
     */
    public Fox(Random rand, boolean randomAge, int BREEDING_AGE, int MAX_AGE, double BREEDING_PROBABILITY, int MAX_LITTER_SIZE, int age, boolean alive, Location location, int foodLevel)
    {
        super();
        BREEDING_AGE = 10;
        MAX_AGE = 150;
        BREEDING_PROBABILITY = 0.09;
        MAX_LITTER_SIZE = 3;
        age = 0;
        alive = true;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        }
        else {
            // leave age at 0
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     */
    public void hunt(Field currentField, Field updatedField, List newFoxes)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            // New foxes are born into adjacent locations.
            int births = breed();
            for(int b = 0; b < births; b++) {
                Fox newFox = new Fox(null, false, b, b, b, b, b, false, null, b);
                newFoxes.add(newFox);
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                newFox.setLocation(loc);
                updatedField.place(newFox, loc);
            }
            // Move towards the source of food if found.
            Location newLocation = findFood(currentField, getLocation());
            if(newLocation == null) {  // no food found - move randomly
                newLocation = updatedField.freeAdjacentLocation(getLocation());
            }
            if(newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            }
            else {
                setDead();
            }
        }
    }
    
    
    /**
     * Tell the fox to look for rabbits adjacent to its current location.
     * @param field The field in which it must look.
     * @param location Where in the field it is located.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood(Location location)
    {
    Iterator<Location> it = field.adjacentLocations(location);
    Location rabbitLocation = null;
    while(it.hasNext()) {
        Location where = it.next();
        Object animal = field.getObjectAt(where);
        if(animal instanceof Rabbit) {
            Rabbit rabbit = (Rabbit) animal;
            if(rabbit.isAlive()) {
                rabbit.setDead();
                foodLevel += RABBIT_FOOD_VALUE;
                if(foodLevel > MAX_FOOD_VALUE) {
                    foodLevel = MAX_FOOD_VALUE;
                }
                rabbitLocation = where;
            }      
        }
    }
    return rabbitLocation;
} 
        

}
