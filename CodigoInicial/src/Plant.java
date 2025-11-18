/**
 * Representa uma planta no campo de simulação.
 * Plantas podem crescer, morrer e servir de alimento para coelhos.
 * 
 * 
 */
public abstract class Plant {
    private boolean alive;
    private Location location;

    public Plant() {
        alive = true;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setDead() {
        alive = false;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Define como a planta cresce, dependendo do clima.
     */
    public abstract void grow(Weather weather);
}