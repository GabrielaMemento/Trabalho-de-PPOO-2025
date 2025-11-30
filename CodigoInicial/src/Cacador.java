import java.util.Iterator;
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
    

    public Cacador(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location);
        setFoodLevel(HUNT_FOOD_VALUE);
        
    }
    
    
    public Location findFood() {
        Iterator<Location> adjacent = getField().adjacentLocations(getLocation()).iterator();
        
        while (adjacent.hasNext()) {
            Location where = adjacent.next();
            Object obj = getField().getObjectAt(where);

        // O caçador pode caçar qualquer animal
            if (obj instanceof Animal) {
                Animal alvo = (Animal) obj;
                if (alvo.isAlive()) {
                    // Mata o animal
                    alvo.setDead();
                    // Recupera energia ao caçar
                    setFoodLevel(HUNT_FOOD_VALUE);
                    // Retorna a posição do animal caçado (para mover-se até lá)
                    return where;
                }
            }
        }
    return null;

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
