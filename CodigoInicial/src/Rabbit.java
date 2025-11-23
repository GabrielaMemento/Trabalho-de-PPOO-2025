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
    private static final int BREEDING_AGE = 5;
    private static final int MAX_AGE = 40;
    private static final double BREEDING_PROBABILITY = 0.15;
    private static final int MAX_LITTER_SIZE = 4;
    private static final Random RAND = new Random();

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     */
    public Rabbit(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location);
    }
    

    @Override
    public void act(List<Animal> newAnimals) {
        incrementAge();
        if (!isAlive()) return;

        giveBirth(newAnimals);

        Field field = getField();
        Location next = field.freeAdjacentLocation(getLocation());
        if (next != null) {
            setLocation(next);
        } else {
            
            setDead();
        }
    }

    private int breed() {
        if (canBreed() && RAND.nextDouble() <= BREEDING_PROBABILITY) {
            return RAND.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return 0;
    }

    @Override
    public int getBreedingAge() { 
        return BREEDING_AGE; 
    }

    @Override
    public int getMaxAge() { 
        return MAX_AGE; 
    }
    private void giveBirth(List<Animal> newAnimals) {
        Field field = getField();
        List<Location> free = field.getFreeAdjacent(getLocation());
        int births = breed();
        for (int b = 0; b < births && !free.isEmpty(); b++) {
            Location loc = free.remove(0);
            newAnimals.add(new Rabbit(false, field, loc));
        }
    }
    
}
