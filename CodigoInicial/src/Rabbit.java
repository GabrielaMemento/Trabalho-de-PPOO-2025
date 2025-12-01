import java.util.Collections;
import java.util.List;

/**
 * Representa um coelho (herbívoro) no ecossistema.
 *
 * @author Grupo 1
 * @version 2025
 */
public class Rabbit extends Animal {
    
    // Constantes da Espécie
    private static final int BREEDING_AGE = 5;
    private static final int MAX_AGE = 50; 
    private static final double BREEDING_PROBABILITY = 0.4; 
    private static final int MAX_LITTER_SIZE = 5;

    /**
     * Constrói um coelho.
     */
    public Rabbit() {
        super();
    }
    
    /**
     * Define se o coelho pode comer o objeto fornecido.
     */
    @Override
    public boolean canEat(Object obj) {
        return obj instanceof Plant;
    }

    /**
    * Procura por uma planta comestível nas localizações adjacentes.
    */
    @Override
    public Location findFood(Field currentField) {
        
        List<Location> adjacentList = currentField.adjacentLocations(getLocation()); 
        Collections.shuffle(adjacentList, rand);
        
        for (Location where : adjacentList) {
            Object obj = currentField.getObjectAt(where);
            
            if (canEat(obj)) {
                Plant plantFound = (Plant) obj;
                
                setFoodLevel(getFoodLevel() + plantFound.getFoodValue());
                currentField.clear(where);
                
                return where;
            }
        }
        return null;
    }

    /**
     * Executa as ações do coelho em um passo de simulação.
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
     * Tenta mover o coelho para a próxima localização.
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
            
            Rabbit newAnimal = new Rabbit();
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
    public int getMaxAge() { 
        return MAX_AGE; 
    }
    @Override 
    public double getBreedingProbability() { 
        return BREEDING_PROBABILITY; 
    }
    public int getMaxLitterSize() { 
        return MAX_LITTER_SIZE; 
    }
}