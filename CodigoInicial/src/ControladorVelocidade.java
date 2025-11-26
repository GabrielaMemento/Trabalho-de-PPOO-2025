import javax.swing.Timer;
import java.awt.event.ActionListener;

/**
 * Controla a velocidade e temporização da simulação
 */
public class ControladorVelocidade {
    private Timer timer;
    private int velocidade; // 1-10
    private final int delayBase = 1000; // 1 segundo no mais lento
    
    public ControladorVelocidade(ActionListener action) {
        this.velocidade = 5; // Velocidade média padrão
        this.timer = new Timer(calcularDelay(), action);
    }
    
    /**
     * Calcula o delay baseado na velocidade (1-10)
     * Velocidade 1 = mais lento, 10 = mais rápido
     */
    private int calcularDelay() {
        // Fórmula: delay = delayBase / (velocidade * 0.5)
        // Exemplo: vel=1 → 2000ms, vel=5 → 400ms, vel=10 → 200ms
        return (int) (delayBase / (velocidade * 0.2));
    }
    
    public void setVelocidade(int velocidade) {
        this.velocidade = Math.max(1, Math.min(10, velocidade));
        timer.setDelay(calcularDelay());
    }
    
    public int getVelocidade() {
        return velocidade;
    }
    
    public void iniciar() {
        timer.start();
    }
    
    public void parar() {
        timer.stop();
    }
    
    public boolean isRunning() {
        return timer.isRunning();
    }
}
