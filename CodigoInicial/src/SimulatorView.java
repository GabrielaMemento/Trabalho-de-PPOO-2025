import java.awt.*;
import javax.swing.*;
import java.util.HashMap;


/**
 * Visualização gráfica da grade da simulação com controles interativos.
 *
 * @author Grupo 1
 * @version 2025
 */
public class SimulatorView extends JFrame {
    
    // Constantes de Cores e Interface (mantidas)
    private static final Color UNKNOWN_COLOR = Color.gray;
    private static final Color MOUNTAIN_COLOR = new Color(100, 100, 100);
    private static final Color RIVER_COLOR = new Color(100, 150, 255);
    private static final Color BURROW_COLOR = new Color(160, 120, 80);
    private static final Color CAVE_COLOR = new Color(50, 50, 50);
    private static final Color VEGETATION_COLOR = new Color(34, 139, 34);
    private static final Color PLAIN_COLOR = new Color(220, 220, 200);

    private final String STEP_PREFIX = "Passo: ";
    private final String POPULATION_PREFIX = "População: ";
    
    // Atributos de Interface
    private final JLabel stepLabel;
    private final JLabel populationLabel;
    private final FieldView fieldView;
    private final HashMap<Class<?>, Color> colors;
    private final FieldStats stats;
    
    // Controles de simulação
    private JButton playPauseButton;
    private JButton stepButton;
    private JButton resetButton;
    private JSlider speedSlider;
    private Timer simulationTimer;
    private Simulator simulator;
    private boolean isRunning = false;
    
    // VARIÁVEIS PARA CONTROLE DE EXECUÇÃO EXTERNA (Não usadas no Reset, mas mantidas para o Main.simulate(X))
    private int targetSteps = -1; 
    
    // Constantes de Velocidade
    private static final int MAX_DELAY_MS = 500; 
    private static final int MIN_DELAY_MS = 50;  
    private static final int SLIDER_RANGE = 10;
    private static final int INITIAL_SPEED = 5;


    /**
     * Cria a janela de visualização com dimensões específicas.
     */
    public SimulatorView(int height, int width) {
        stats = new FieldStats();
        colors = new HashMap<>();

        setTitle("Simulação de Ecossistema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        stepLabel = new JLabel(STEP_PREFIX + "0", JLabel.CENTER);
        stepLabel.setFont(new Font("Arial", Font.BOLD, 14));
        stepLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        populationLabel = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        populationLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        populationLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel controlPanel = createControlPanel();
        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        contents.add(stepLabel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(populationLabel, BorderLayout.NORTH);
        bottomPanel.add(controlPanel, BorderLayout.CENTER);
        contents.add(bottomPanel, BorderLayout.SOUTH);
        
        setLocation(100, 50);
        pack();
        setVisible(true);
        
        // Timer para simulação automática (Controle principal de tempo)
        simulationTimer = new Timer(getDelayForSpeed(INITIAL_SPEED), e -> {
            if (simulator != null && isRunning) {
                
                // Lógica de PARADA AUTOMÁTICA
                if (targetSteps != -1 && simulator.step >= targetSteps) {
                    stop(); 
                    targetSteps = -1; 
                    checkButtons(); 
                    return; 
                }
                
                simulator.simulateOneStep();
            }
        });
        simulationTimer.setInitialDelay(0);
    }
    
    /**
     * Define a meta de passos para a simulação rodar automaticamente.
     */
    public void setTargetSteps(int numSteps) {
        this.targetSteps = this.simulator.step + numSteps;
    }

    /**
     * Define o simulador que será controlado por esta view.
     */
    public void setSimulator(Simulator sim) {
        this.simulator = sim;
    }

    /**
     * Calcula o delay (ms) com base no valor do slider.
     */
    private int getDelayForSpeed(int speed) {
        double adjustment = (double)(MAX_DELAY_MS - MIN_DELAY_MS) / (SLIDER_RANGE - 1);
        return MAX_DELAY_MS - (int)(adjustment * (speed - 1));
    }
    
    /**
     * Habilita ou desabilita botões se o limite for atingido ou se a simulação não for viável.
     */
    private void checkButtons() {
        if (simulator == null) return;
        
        boolean isViable = stats.isViable(simulator.field);
        
        // O limite de passos é baseado no valor que Main chamou (simulator.getInitialRunSteps())
        boolean reachedLimit = simulator.step >= simulator.getInitialRunSteps() && simulator.getInitialRunSteps() > 0;
        
        if (reachedLimit || !isViable) {
            stop(); 
            playPauseButton.setEnabled(false);
            stepButton.setEnabled(false);
            resetButton.setEnabled(true); 
            
        } else if (!isRunning) {
            playPauseButton.setEnabled(true);
            stepButton.setEnabled(true);
        }
    }


    /**
     * Cria o painel de controles.
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Botão Play/Pause
        playPauseButton = new JButton("▶ Play");
        playPauseButton.addActionListener(e -> togglePlayPause());
        
        // Botão Step (executar um passo)
        stepButton = new JButton("➜ Passo");
        stepButton.addActionListener(e -> {
            if (simulator != null && !isRunning) {
                // Se o limite foi atingido, não faça nada
                if (simulator.step >= simulator.getInitialRunSteps() && simulator.getInitialRunSteps() > 0) return;
                
                simulator.simulateOneStep();
            }
        });
        
        // Botão Reset (CORREÇÃO: Volta ao Passo 0 e permite avanço manual)
        resetButton = new JButton("↻ Reset");
        resetButton.addActionListener(e -> {
            if (simulator != null) {
                // 1. Para o Timer
                stop();
                // 2. Garante que qualquer meta de auto-execução seja cancelada
                this.targetSteps = -1; 
                // 3. Reseta o estado do simulador para Passo 0
                simulator.reset();
                
                // checkButtons() será chamado pelo showStatus após o reset e habilitará Play/Passo.
            }
        });
        
        // Slider de velocidade
        JLabel speedLabel = new JLabel("Velocidade:");
        speedSlider = new JSlider(JSlider.HORIZONTAL, 1, SLIDER_RANGE, INITIAL_SPEED);
        speedSlider.addChangeListener(e -> updateSpeed());
        
        // Adiciona componentes
        panel.add(playPauseButton);
        panel.add(stepButton);
        panel.add(resetButton);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(new JLabel("Velocidade:"));
        panel.add(speedSlider);
        
        return panel;
    }

    /**
     * Alterna entre Play e Pause.
     */
    private void togglePlayPause() {
        if (isRunning) {
            stop();
        } else {
            play();
        }
    }

    /**
     * Inicia a simulação automática.
     */
    public void play() {
        // Se já atingiu o limite, não faça nada
        if (simulator != null && simulator.step >= simulator.getInitialRunSteps() && simulator.getInitialRunSteps() > 0) {
             checkButtons(); 
             return;
        }
        
        isRunning = true;
        playPauseButton.setText("⏸ Pause");
        stepButton.setEnabled(false);
        simulationTimer.setDelay(getDelayForSpeed(speedSlider.getValue())); 
        simulationTimer.start();
    }

    /**
     * Pausa a simulação automática.
     */
    private void stop() {
        isRunning = false;
        playPauseButton.setText("▶ Play");
        stepButton.setEnabled(true);
        simulationTimer.stop();
    }

    /**
     * Atualiza a velocidade da simulação com base no slider.
     */
    private void updateSpeed() {
        int speed = speedSlider.getValue();
        int delay = getDelayForSpeed(speed);
        
        simulationTimer.setDelay(delay);
    }

    /**
     * Define uma cor para uma classe de entidade.
     */
    public void setColor(Class<?> entityClass, Color color) {
        colors.put(entityClass, color);
    }

    /**
     * Recupera a cor associada a uma classe.
     */
    private Color getColor(Class<?> entityClass) {
        return colors.getOrDefault(entityClass, UNKNOWN_COLOR);
    }

    /**
     * Exibe o estado atual do campo.
     */
    public void showStatus(int step, Field field) {
        if(!isVisible())
            setVisible(true);

        stepLabel.setText(STEP_PREFIX + step);
        stats.reset();
        fieldView.preparePaint();

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Location loc = new Location(row, col);
                Object obj = field.getObjectAt(loc);

                if(obj != null) {
                    stats.incrementCount(obj.getClass());
                    fieldView.drawMark(col, row, getColor(obj.getClass()));
                } else {
                    // Célula vazia: desenha o terreno
                    Terrain terrain = field.getTerrainAt(loc);
                    fieldView.drawMark(col, row, getTerrainColor(terrain));
                }
            }
        }

        populationLabel.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
        
        checkButtons(); 
    }

    /**
     * Mapeia o tipo de terreno para sua cor.
     */
    private Color getTerrainColor(Terrain terrain) {
        return switch (terrain) {
            case MOUNTAIN -> MOUNTAIN_COLOR;
            case RIVER -> RIVER_COLOR;
            case BURROW -> BURROW_COLOR;
            case CAVE -> CAVE_COLOR;
            case DENSE_VEGETATION -> VEGETATION_COLOR;
            case PLAIN -> PLAIN_COLOR;
        };
    }

    /**
     * Verifica se a simulação é viável.
     */
    public boolean isViable(Field field) {
        return stats.isViable(field);
    }

    /**
     * Painel interno responsável por desenhar a grade.
     */
    private class FieldView extends JPanel {
        private static final int GRID_VIEW_SCALING_FACTOR = 8;

        private final int gridWidth, gridHeight;
        private int xScale, yScale;
        private Dimension size;
        private Graphics g;
        private Image fieldImage;

        public FieldView(int height, int width) {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
            setBackground(Color.WHITE);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        public void preparePaint() {
            if(!size.equals(getSize())) {
                size = getSize();
                fieldImage = createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = Math.max(1, size.width / gridWidth);
                yScale = Math.max(1, size.height / gridHeight);
            }
        }

        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(fieldImage != null) {
                g.drawImage(fieldImage, 0, 0, null);
            }
        }
    }
}