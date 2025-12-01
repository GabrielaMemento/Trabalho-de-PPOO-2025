import java.util.List;

/**
 * Define os tipos de plantas disponíveis no ecossistema e o valor de alimento que fornecem.
 * Implementa {@link Actor} para participar do ciclo da simulação.
 * @author Grupo 1
 * @version 2025
 */
public enum Plant implements Actor {
    
    // Define os valores de alimento em cada constante
    ROSEMARY(6), // Alecrim
    SAGE(4);     // Sálvia

    private final int foodValue;
    private Location location;

    /** Construtor do enum. */
    Plant(int foodValue) {
        this.foodValue = foodValue;
    }

    /** * Retorna o valor de alimento da planta. 
     * @return O valor de alimento.
     */
    public int getFoodValue() {
        return foodValue;
    }
    
    /**
     * Ação da planta em um passo de simulação.
     * A planta se copia para o updatedField (se sobreviver à lógica de morte no Simulator).
     */
    @Override
    public void act(Field currentField, Field updatedField, List<Actor> newActors) {
        // A planta não tem lógica de mover/morrer/reproduzir aqui.
        // Ela se replica para o updatedField, se o Simulator não a removeu.
        if (location != null) {
            updatedField.place(this, location);
        }
    }
    
    /** @return A localização atual da planta. */
    @Override
    public Location getLocation() {
        return location;
    }
    
    /** * Define a localização da planta. 
     * @param location A nova localização.
     */
    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}