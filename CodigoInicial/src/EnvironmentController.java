/**
 * Controlador do ambiente da simulação.
 * Gerencia o clima e o ciclo dia/noite com base no passo atual.
 * 
 * 
 * 
 */
public class EnvironmentController {
    private Weather currentWeather;
    private TimeOfDay timeOfDay;
    private int step;

    public EnvironmentController() {
        step = 0;
        currentWeather = Weather.SUNNY;
        timeOfDay = TimeOfDay.DAY;
    }

    /**
     * Atualiza o ambiente com base no passo atual.
     */
    public void updateEnvironment() {
        step++;

        // Alterna entre dia e noite a cada 5 passos
        if (step % 5 == 0) {
            timeOfDay = (timeOfDay == TimeOfDay.DAY) ? TimeOfDay.NIGHT : TimeOfDay.DAY;
        }

        // Alterna clima a cada 10 passos
        if (step % 10 == 0) {
            switch (currentWeather) {
                case SUNNY -> currentWeather = Weather.RAINY;
                case RAINY -> currentWeather = Weather.SUMMER;
                case SUMMER -> currentWeather = Weather.WINTER;
                case WINTER -> currentWeather = Weather.SUNNY;
            }
        }
    }

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    public int getStep() {
        return step;
    }
}