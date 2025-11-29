import java.util.List;
import java.util.Random;


public abstract class Animal implements Actor {
    private int age;
    private boolean alive;
    private Location location;
    private int foodLevel;
    private Field field;
    private static final Random RAND = new Random();


    
    public abstract int getBreedingAge();

    public abstract double getBreedingProbability();

    public abstract int getMaxLitterSize();

    public abstract int getMaxAge();

    public abstract Location findFood();



    public Animal(boolean randomAge, Field field, Location location) {
        this.age = 0;
        this.alive = true;
        this.field = field;
        this.foodLevel = 0;
        setLocation(location);
        
        if (randomAge) {
            this.age = RAND.nextInt(getMaxAge()); 
        }
    }

    /**
     * Executa as ações da raposa em um passo de simulação:
     * - Envelhece e aumenta a fome.
     * - Tenta reproduzir.
     * - Procura comida (coelho) nas adjacências; se encontrar, move-se para lá.
     * - Caso não encontre, tenta mover-se para uma célula adjacente livre.
     * - Respeita restrições de terreno: não entra em RIVER ou MOUNTAIN.
     * - Morre de fome se foodLevel chegar a zero; pode morrer por superlotação.
     *
     * @param newAnimals lista onde novos animais nascidos são adicionados.
     */
    public void act(List<Animal> newAnimals) {
        incrementAge();
        incrementHunger();
        if (!isAlive()) return;

        giveBirth(newAnimals);

        // Procura comida  nas adjacências
        Location newLocation = findFood();
        if (newLocation == null) {
            // Caso não encontre comida, tenta mover para um espaço livre
            newLocation = getField().freeAdjacentLocation(getLocation());
        }

        // Movimento respeitando restrições de terreno
    if (newLocation != null) {
        Terreno terrain = getField().getTerrainAt(newLocation);
        
        
        //  Obtém o nome do ator  para consultar a regra no arquivo
        String nomeAtor = this.getClass().getSimpleName();
        
        //  Verifica se o terreno é proibido usando a classe Barreiras
        if (!Barreiras.isProibido(nomeAtor, terrain)) {
        setLocation(newLocation);
        }
    
        
    } else {
        // Sem espaço livre: morre por superlotação
        setDead();
    }
    }

    public void incrementAge() {
        age++;
        if (age > getMaxAge()) {
            setDead();
        }
    }

    
    public boolean canBreed() {
        return age >= getBreedingAge(); 
    }

    public int breed() {
        if (canBreed() && RAND.nextDouble() <= getBreedingProbability()) {
            return RAND.nextInt(getMaxLitterSize()) + 1;
        }
        return 0;
    }

    public void incrementHunger() {
        setFoodLevel(foodLevel - 1);
        if (foodLevel <= 0) setDead();
    }

    public void giveBirth(List<Animal> newAnimals) {
        List<Location> free = getField().getFreeAdjacent(getLocation());
        int births = breed();
        for (int b = 0; b < births && !free.isEmpty(); b++) {
            Location loc = free.remove(0);
            newAnimals.add(new Rabbit(false, getField(), loc));
        }
    }

    public void setDead() {
        alive = false;
        if (location != null && field != null) {
            field.clear(location);            
        }
        location = null;
        field = null;
    }


    public void setLocation(Location newLocation) {
        if (location != null && field != null) {
            field.clear(location);
        }
        location = newLocation;
        if (field != null && newLocation != null) {
            field.place(this, newLocation);
        }
    }

    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    public int getFoodLevel() {
        return foodLevel;
    }
    
    public boolean isAlive() {
        return alive; 
    }

    public Field getField() {
        return field;
    }

    public Location getLocation() { 
        return location; 
    }


}
