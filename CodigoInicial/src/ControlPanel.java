import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Painel de controle da simulação.
 * Permite iniciar, pausar e reiniciar a simulação, além de exibir informações ambientais.
 * 
 * 
 * 
 */
public class ControlPanel extends JPanel {
    private JButton startButton, pauseButton, resetButton;
    private JLabel weatherLabel, timeLabel, seasonLabel;
    private JLabel populationLabel;

    private EnvironmentController environment;

    public ControlPanel(EnvironmentController environment) {
        this.environment = environment;
        setLayout(new GridLayout(2, 1));

        // Painel de botões
        JPanel buttonPanel = new JPanel();
        startButton = new JButton("Iniciar");
        pauseButton = new JButton("Pausar");
        resetButton = new JButton("Reiniciar");

        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(resetButton);

        // Painel de informações
        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        weatherLabel = new JLabel("Clima: " + environment.getCurrentWeather());
        timeLabel = new JLabel("Tempo: " + environment.getTimeOfDay());
        seasonLabel = new JLabel("Estação: " + getSeasonFromWeather(environment.getCurrentWeather()));
        populationLabel = new JLabel("População: ");

        infoPanel.add(weatherLabel);
        infoPanel.add(timeLabel);
        infoPanel.add(seasonLabel);
        infoPanel.add(populationLabel);

        add(buttonPanel);
        add(infoPanel);

        // Eventos dos botões (exemplo básico)
        startButton.addActionListener(e -> System.out.println("Simulação iniciada"));
        pauseButton.addActionListener(e -> System.out.println("Simulação pausada"));
        resetButton.addActionListener(e -> System.out.println("Simulação reiniciada"));
    }

    /**
     * Atualiza os rótulos com os dados atuais do ambiente.
     */
    public void updateLabels(int totalPopulation) {
        weatherLabel.setText("Clima: " + environment.getCurrentWeather());
        timeLabel.setText("Tempo: " + environment.getTimeOfDay());
        seasonLabel.setText("Estação: " + getSeasonFromWeather(environment.getCurrentWeather()));
        populationLabel.setText("População: " + totalPopulation);
    }

    /**
     * Traduz o clima para estação do ano.
     */
    private String getSeasonFromWeather(Weather weather) {
        return switch (weather) {
            case SUMMER -> "Verão";
            case WINTER -> "Inverno";
            default -> "Transição";
        };
    }
}