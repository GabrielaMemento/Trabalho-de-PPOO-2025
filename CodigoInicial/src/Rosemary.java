/**
 * Planta Alecrim.
 * Cresce melhor no verão e serve de alimento para coelhos.
 * 
 * 
 *
 */
public class Rosemary extends Plant {
    private static final int MAX_LIFE = 30;
    private int age;

    public Rosemary() {
        super();
        age = 0;
    }

    @Override
    public void grow(Weather weather) {
        if(isAlive()) {
            if(weather == Weather.SUMMER) {
                age += 2;
            } else {
                age++;
            }
            if(age > MAX_LIFE) {
                setDead();
            }
        }
    }
}