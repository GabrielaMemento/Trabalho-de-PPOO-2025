import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Representa um caçador no ecossistema.
 */
public class Cacador extends Animal implements Actor {
    /** Idade mínima para reprodução. */
    private static final int BREEDING_AGE = 20;
    /** Idade máxima. */
    private static final int MAX_AGE = 300;
    /** Probabilidade de reprodução a cada passo. */
    private static final double BREEDING_PROBABILITY = 0.03;
    /** Tamanho máximo da ninhada. */
    private static final int MAX_LITTER_SIZE = 1;
    /** Valor nutricional da caça (restaura fome). */
    private static final int HUNT_FOOD_VALUE = 5;
    /** RNG local. */
    private static final Random RAND = new Random();

    public Cacador(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location);
        setFoodLevel(HUNT_FOOD_VALUE);
        
    }

    @Override
    public void act(List<Animal> newAnimals) {
        incrementAge();
        incrementHunger();
        if (!isAlive()) return;

        giveBirth(newAnimals);

        Location newLocation = findFood();
        if (newLocation == null) {
            newLocation = getField().freeAdjacentLocation(getLocation());
        }

        if (newLocation != null) {
            Terreno terrain = getField().getTerrainAt(newLocation);
            if (terrain != Terreno.MOUNTAIN && terrain != Terreno.RIVER) {
                setLocation(newLocation);
            }
        } else {
            setDead();
        }
    }

    

    public Location findFood() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findFood'");
    }

    @Override
    public int getBreedingAge() { 
        return BREEDING_AGE; 
    }

    @Override
    public int getMaxAge() { 
        return MAX_AGE; 
    }

    @Override
    public double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }

    @Override
    public int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }




}