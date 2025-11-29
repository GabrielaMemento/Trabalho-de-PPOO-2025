import java.util.Iterator;
import java.util.List;


public class Rabbit extends Animal {
    private static final int BREEDING_AGE = 5;
    private static final int MAX_AGE = 40;
    private static final double BREEDING_PROBABILITY = 0.15;
    private static final int MAX_LITTER_SIZE = 4;


    public Rabbit(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location);
    }

    
    @Override
    public void act(List<Animal> newAnimals) {
        incrementAge();
        incrementHunger();
        if (!isAlive()) return;

        giveBirth(newAnimals);

        // Procura comida (plantas) nas adjacências
        Location newLocation = findFood();
        if (newLocation == null) {
            // Caso não encontre comida, tenta mover para um espaço livre
            newLocation = getField().freeAdjacentLocation(getLocation());
        }

        Location next = getField().freeAdjacentLocation(getLocation());
        if (next != null) {
            Terreno terrain = getField().getTerrainAt(next);
            if (terrain == Terreno.BURROW || terrain == Terreno.PLAIN || terrain == Terreno.DENSE_VEGETATION) {
                setLocation(next);
            }
        } else {
            
            setDead();
        }
    }

    public Location findFood() {
        Iterator<Location> adjacent = getField().adjacentLocations(getLocation()).iterator();

        while (adjacent.hasNext()) {
            Planta plant = null;
            if (plant == Planta.SALVIA) {
                    setFoodLevel(5); 
                }
            if (plant == Planta.ALECRIM) {
                    setFoodLevel(3);
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
