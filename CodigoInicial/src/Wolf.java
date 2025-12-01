import java.util.Iterator;
import java.util.List;

/**
 * Representa um lobo no ecossistema.
 *
 * @author Grupo 1
 * @version 2025
 */
public class Wolf extends Animal {
    
    // Constantes da Espécie
    private static final int BREEDING_AGE = 12;
    private static final int MAX_AGE = 180;
    private static final double BREEDING_PROBABILITY = 0.37; 
    private static final int MAX_LITTER_SIZE = 5;
    private static final int FOOD_VALUE = 35; 

    /**
     * Constrói um lobo.
     */
    public Wolf() {
        super();
    }
    
    /**
     * Define se o lobo pode comer o objeto fornecido.
     */
    @Override
    public boolean canEat(Object obj) {
        return (obj instanceof Rabbit) || (obj instanceof Fox);
    }

    /**
     * Procura por comida (coelho ou raposa) nas células adjacentes.
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
     * Executa as ações do lobo em um passo de simulação.
     */
    @Override
    public void act(Field currentField, Field updatedField, List<Actor> newActors) {
        incrementAge();
        incrementHunger();
        if (!isAlive()) return;

        giveBirth(currentField, updatedField, newActors);

        Location nextLocation = findFood(currentField);
        if (nextLocation == null) {
            nextLocation = currentField.freeAdjacentLocation(getLocation());
        }

        attemptMove(currentField, updatedField, nextLocation);
    }
    
    /**
     * Tenta mover o lobo para a próxima localização.
     */
    private void attemptMove(Field currentField, Field updatedField, Location nextLocation) {
        if (nextLocation != null) {
            Terrain terrain = currentField.getTerrainAt(nextLocation);
            String actorName = this.getClass().getSimpleName();
            
            if (!Barriers.isForbidden(actorName, terrain)) {
                updatedField.clear(getLocation()); 
                setLocation(nextLocation);
                updatedField.place(this, nextLocation);
            } else {
                updatedField.place(this, getLocation());
            }
        } else {
            updatedField.place(this, getLocation());
        }
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
            
            Wolf newAnimal = new Wolf();
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