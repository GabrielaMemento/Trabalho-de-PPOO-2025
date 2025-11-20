import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public class Rabbit extends Animal
{
    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     */
    public Rabbit(Random rand, boolean randomAge, int BREEDING_AGE, int MAX_AGE, double BREEDING_PROBABILITY, int MAX_LITTER_SIZE, int age, boolean alive, Location location, int foodLevel)
    {
        BREEDING_AGE = 5;
        MAX_AGE = 50;
        BREEDING_PROBABILITY = 0.15;
        MAX_LITTER_SIZE = 5;
        age = 0;
        alive = true;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     */
    public void run(Field updatedField, List newRabbits)
    {
        incrementAge();
        if(isAlive()) {
            int births = breed();
            for(int b = 0; b < births; b++) {
                Rabbit newRabbit = new Rabbit(null, false, b, b, b, b, b, false, null, b);
                newRabbits.add(newRabbit);
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                newRabbit.setLocation(loc);
                updatedField.place(newRabbit, loc);
            }
            Location newLocation = updatedField.freeAdjacentLocation(getLocation());
            // Only transfer to the updated field if there was a free location
            if(newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            }
            else {
                // can neither move nor stay - overcrowding - all locations taken
                setDead();
            }
        }
    }


}
