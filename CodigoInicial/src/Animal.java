import java.util.List;
import java.util.Random;

/**
 * Uma classe que representa características compartilhadas de animais.
 * Estende de {@link Actor} para se adequar à estrutura geral de simulação.
 * @author Grupo 1
 * @version 2025
 */
public abstract class Animal implements Actor {
    
    // Atributos
    /** Se o animal está vivo ou não */
    private boolean alive;
    /** A localização do animal */
    private Location location;
    /** A idade do animal */
    private int age;
    /** Nível de energia/fome do animal */
    private int foodLevel; 
    
    /** Um gerador de números aleatórios compartilhado para controlar a reprodução. */
    protected static final Random rand = new Random();

    /**
     * Construtor para um animal.
     * Inicializa com idade 0 e nível de comida padrão.
     */
    public Animal() {
        this.age = 0;
        this.alive = true;
    }

        /**
     * Executa as ações da águia em um passo de simulação.
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
     * Tenta mover a águia para a próxima localização.
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

    // Métodos Abstratos de Espécie
    
    /** @return A idade máxima permitida para esta espécie. */
    public abstract int getMaxAge();

    /** @return A probabilidade de reprodução (entre 0 e 1). */
    public abstract double getBreedingProbability();

    /** @return O número máximo de filhotes que podem nascer de uma vez. */
    public abstract int getMaxLitterSize();

    /** @return True se o animal tiver idade suficiente para procriar. */
    public abstract boolean canBreed();


    /** @return A idade mínima para ser apto para reprodução. */
    public abstract int getBreedingAge();

    /**
     * Tenta localizar e consumir comida no campo atual.
     * A comida encontrada é marcada para remoção no campo atual.
     * @param currentField O campo atual, usado para verificar locais adjacentes.
     * @return A localização da comida encontrada, ou null se não houver.
     */
    public abstract Location findFood(Field currentField);

    /**
     * Gera novos animais em locais adjacentes livres, com base na probabilidade de reprodução.
     * Os novos animais são colocados no campo de destino.
     * @param currentField O campo atual.
     * @param updatedField O campo de destino onde os novos atores serão colocados.
     * @param newActors Lista onde os novos atores nascidos são adicionados.
     * @param isDay Indica se o ciclo atual é dia (true) ou noite (false).
     */
    public abstract void giveBirth(Field currentField, Field updatedField, List<Actor> newActors);
    
    /** * Define se este animal pode comer outro objeto.
     * @param obj O objeto potencial a ser comido.
     * @return true se o animal puder comer o objeto.
     */
    public abstract boolean canEat(Object obj);

    // Métodos de Estado

    /** @return True se o animal ainda estiver vivo. */
    public boolean isAlive() {
        return alive;
    }

    /** Indica que o animal não está mais vivo. */
    protected void setDead() {
        this.alive = false;
    }

    /** @return A localização atual do animal. */
    public Location getLocation() {
        return location;
    }

    /**
     * Define a localização do animal (objeto Location).
     * @param location A nova localização.
     */
    @Override
    public void setLocation(Location location) {
        this.location = location;
    }


    /** @return A idade do animal. */
    protected int getAge() {
        return age;
    }

    /** @param age A nova idade do animal. */
    protected void setAge(int age) {
        this.age = age;
    }
    
    /** @param foodLevel O novo nível de comida. */
    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    /** @return O nível de comida atual. */
    public int getFoodLevel() {
        return foodLevel;
    }

    /**
     * Aumenta a idade em uma unidade e verifica a idade máxima.
     */
    protected void incrementAge() {
        age++;
        if (age > getMaxAge()) {
            setDead();
        }
    }
    
    /**
     * Diminui o nível de fome em uma unidade e verifica se o animal morreu de fome.
     */
    protected void incrementHunger() {
        this.foodLevel--;
        if (foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Gera um número de nascimentos, se o animal puder procriar.
     * @return O número de nascimentos (pode ser zero).
     */
    protected int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }
}