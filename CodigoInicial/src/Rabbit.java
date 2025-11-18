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
    // Characteristics shared by all rabbits (static fields).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 50;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.15;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();
    
    // Individual characteristics (instance fields).
    // O atributo 'age', 'alive' e 'location' foram movidos para a classe Animal.
    
    // The rabbit's age.
    
    // Whether the rabbit is alive or not.
    
    // The rabbit's position
    

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     */
    public Rabbit(boolean randomAge)
    {
        super();

        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
        }
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     */
    /**
     * Este é o que o coelho faz na maior parte do tempo - ele corre.
     * Às vezes ele se reproduz ou morre de velhice.
     */
    @Override
    public void act(Field currentField, Field updatedField, List newRabbits)
    {
        incrementAge();
        if(isAlive()) {
            int births = breed();
            for(int b = 0; b < births; b++) {
                Rabbit newRabbit = new Rabbit(false);
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
    
    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    /**
     * Aumenta a idade.
     * Isso pode resultar na morte do coelho.
     */
    private void incrementAge()
    {
        incrementAge(MAX_AGE);
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A rabbit can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return getAge() >= BREEDING_AGE;
    }
    
    /**
     * Check whether the rabbit is alive or not.
     * @return True if the rabbit is still alive.
     */


    /**
     * Tell the rabbit that it's dead now :(
     */
    /**
     * Diz ao coelho que ele morreu :(
     */
    public void setEaten()
    {
        setDead();
    }
    
    /**
     * Set the animal's location.
     * @param row The vertical coordinate of the location.
     * @param col The horizontal coordinate of the location.
     */


    /**
     * Set the rabbit's location.
     * @param location The rabbit's location.
     */
    // O método setLocation(Location) foi movido para a classe Animal.
    // O método setLocation(int, int) foi removido, pois a versão com Location é suficiente.
    @Override
    public void setLocation(Location location)
    {
        super.setLocation(location);
    }
}
