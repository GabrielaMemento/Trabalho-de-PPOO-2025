/**
 * Planta Sálvia.
 * Cresce melhor com chuva e serve de alimento para coelhos.
 * 
 * 
 * 
 */
public class Sage extends Plant {
    private static final int MAX_LIFE = 25;
    private int age;

    public Sage() {
        super();
        age = 0;
    }

    @Override
    public void grow(Weather weather) {
        if(isAlive()) {
            if(weather == Weather.RAINY) {
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