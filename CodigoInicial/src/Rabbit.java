import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;



public class Rabbit extends Animal {
    private static final int BREEDING_AGE = 5;
    private static final int MAX_AGE = 40;
    private static final double BREEDING_PROBABILITY = 0.15;
    private static final int MAX_LITTER_SIZE = 4;
    private static final Random RAND = new Random();



    public Rabbit(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location);
    }


    /**
    * Procura por uma planta comestível (Alecrim ou Sálvia) nas localizações adjacentes, 
    * escolhendo-a de forma aleatória se houver múltiplas opções.
    * * O método garante a aleatoriedade embaralhando a ordem das localizações adjacentes 
    * e consumindo a primeira planta encontrada.
    */
    public Location findFood() {
    
    List<Location> adjacentList = getField().adjacentLocations(getLocation()); 
    Collections.shuffle(adjacentList, RAND);
    Iterator<Location> shuffledIterator = adjacentList.iterator();
    
    while (shuffledIterator.hasNext()) {
        Location where = shuffledIterator.next();
        
        Object obj = getField().getObjectAt(where);
        
        if (obj instanceof Planta) {
            
            Planta plantaEncontrada = (Planta) obj;
            
            setFoodLevel(getFoodLevel() + plantaEncontrada.getValorAlimento());
            getField().place(null, where); 
            
            return where;
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
