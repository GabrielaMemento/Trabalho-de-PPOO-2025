import java.util.Iterator;
import java.util.List;

/**
 * Representa uma águia no ecossistema.
 *
 * @author Grupo 1
 * @version 2025
 */
public class Eagle extends Animal {

    // Constantes da Espécie
    private static final int BREEDING_AGE = 15;
    private static final int MAX_AGE = 200;
    private static final double BREEDING_PROBABILITY = 0.04; 
    private static final int MAX_LITTER_SIZE = 2;
    private static final int FOOD_VALUE = 10; 
    
    /**
     * Constrói uma águia.
     */
    public Eagle() {
        super();
    }
    
    /**
     * Define se a águia pode comer o objeto fornecido.
     */
    @Override
    public boolean canEat(Object obj) {
        return (obj instanceof Rabbit) || (obj instanceof Snake);
    }

    /**
     * Procura por comida (coelho ou cobra) nas células adjacentes.
     */
    @Override
    public Location findFood(Field currentField) {
        Iterator<Location> adjacent = currentField.adjacentLocations(getLocation()).iterator();
        while (adjacent.hasNext()) {
            Location where = adjacent.next();
            Object obj = currentField.getObjectAt(where);

            if (canEat(obj)) {
                Animal prey = (Animal) obj;
                if (prey.isAlive()) {
                    prey.setDead();
                    currentField.clear(where); 
                    setFoodLevel(FOOD_VALUE);
                    return where;
                }
            }
        }
        return null;
    }


    /**
     * Gera novos animais em locais adjacentes livres.
     */
    @Override
    public void giveBirth(Field currentField, Field updatedField, List<Actor> newActors) {
        List<Location> freeLocations = currentField.getFreeAdjacent(getLocation());
        int births = breed();
        
        for (int i = 0; i < births && !freeLocations.isEmpty(); i++) {
            Location newLoc = freeLocations.remove(0);
            
            Eagle newAnimal = new Eagle();
            newAnimal.setLocation(newLoc);
            
            updatedField.place(newAnimal, newLoc);
            newActors.add(newAnimal);
        }
    }
    
    // Getters para as propriedades da espécie
    @Override 
    public boolean canBreed() { 
        return getAge() >= BREEDING_AGE; 
    }
    @Override 
    public int getBreedingAge() { 
        return BREEDING_AGE; 
    }
    @Override 
    public double getBreedingProbability() { 
        return BREEDING_PROBABILITY; 
    }
    @Override 
    public int getMaxLitterSize() { 
        return MAX_LITTER_SIZE; 
    }
    @Override 
    public int getMaxAge() { 
        return MAX_AGE; 
    }
    public int getFoodValue() { 
        return FOOD_VALUE; 
    }
}