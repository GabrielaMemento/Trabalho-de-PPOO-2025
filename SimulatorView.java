import java.awt.*;
import javax.swing.*;
import java.util.HashMap;

/**
 * Visualização gráfica da grade da simulação com controles interativos.
 *
 * - Mostra cada célula do campo como um retângulo colorido.
 * - Usa cores diferentes para cada espécie/planta (definidas com setColor).
 * - Mostra o passo atual e estatísticas populacionais agregadas.
 * - Também desenha tipos de terreno (rios, montanhas, cavernas, tocas, vegetação, campo aberto).
 * - Inclui botões de controle: Play/Pause, Step, Reset e controle de velocidade.
 *
 * @author David J. Barnes
 * @author Michael Kolling
 * @version 2002-04-23 (revisado 2025-11)
 */
public class SimulatorView extends JFrame {
    private static final Color UNKNOWN_COLOR = Color.gray;

    // Cores para terrenos
    private static final Color MOUNTAIN_COLOR = new Color(100, 100, 100);
    private static final Color RIVER_COLOR = new Color(100, 150, 255);
    private static final Color BURROW_COLOR = new Color(160, 120, 80);
    private static final Color CAVE_COLOR = new Color(50, 50, 50);
    private static final Color VEGETATION_COLOR = new Color(34, 139, 34);
    private static final Color PLAIN_COLOR = new Color(220, 220, 200);

    private final String STEP_PREFIX = "Passo: ";
    private final String POPULATION_PREFIX = "População: ";
    
    private JLabel stepLabel, population;
    private FieldView fieldView;
    private HashMap<Class<?>, Color> colors;
    private FieldStats stats;
    
    // Controles de simulação
    private JButton playPauseButton;
    private JButton stepButton;
    private JButton resetButton;
    private JSlider speedSlider;
    private Timer simulationTimer;
    private Simulator simulator;
    private boolean isRunning = false;

    /**
     * Cria a janela de visualização com dimensões específicas.
     */
    public SimulatorView(int height, int width) {
        stats = new FieldStats();
        colors = new HashMap<>();

        setTitle("Simulação de Ecossistema ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Labels de informação
        stepLabel = new JLabel(STEP_PREFIX + "0", JLabel.CENTER);
        stepLabel.setFont(new Font("Arial", Font.BOLD, 14));
        stepLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        population.setFont(new Font("Arial", Font.PLAIN, 12));
        population.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Painel de controles
        JPanel controlPanel = createControlPanel();
        
        // Campo de visualização
        fieldView = new FieldView(height, width);

        // Layout da janela
        Container contents = getContentPane();
        contents.add(stepLabel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(population, BorderLayout.NORTH);
        bottomPanel.add(controlPanel, BorderLayout.CENTER);
        contents.add(bottomPanel, BorderLayout.SOUTH);
        
        setLocation(100, 50);
        pack();
        setVisible(true);
        
        // Timer para simulação automática
        simulationTimer = new Timer(100, e -> {
            if (simulator != null && isRunning) {
                simulator.simulateOneStep();
            }
        });
    }

    /**
     * Define o simulador que será controlado por esta view.
     */
    public void setSimulator(Simulator sim) {
        this.simulator = sim;
    }

    /**
     * Cria o painel de controles com botões e slider.
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Botão Play/Pause
        playPauseButton = new JButton("▶ Play");
        playPauseButton.setFont(new Font("Arial", Font.BOLD, 12));
        playPauseButton.setPreferredSize(new Dimension(100, 30));
        playPauseButton.addActionListener(e -> togglePlayPause());
        
        // Botão Step (executar um passo)
        stepButton = new JButton("➜ Passo");
        stepButton.setFont(new Font("Arial", Font.PLAIN, 12));
        stepButton.setPreferredSize(new Dimension(100, 30));
        stepButton.addActionListener(e -> {
            if (simulator != null && !isRunning) {
                simulator.simulateOneStep();
            }
        });
        
        // Botão Reset
        resetButton = new JButton("↻ Reset");
        resetButton.setFont(new Font("Arial", Font.PLAIN, 12));
        resetButton.setPreferredSize(new Dimension(100, 30));
        resetButton.addActionListener(e -> {
            if (simulator != null) {
                stop();
                simulator.reset();
            }
        });
        
        // Slider de velocidade
        JLabel speedLabel = new JLabel("Velocidade:");
        speedLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        
        speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 5);
        speedSlider.setPreferredSize(new Dimension(150, 30));
        speedSlider.setMajorTickSpacing(3);
        speedSlider.setPaintTicks(true);
        speedSlider.addChangeListener(e -> updateSpeed());
        
        // Adiciona componentes ao painel
        panel.add(playPauseButton);
        panel.add(stepButton);
        panel.add(resetButton);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(speedLabel);
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
    private void play() {
        isRunning = true;
        playPauseButton.setText("⏸ Pause");
        stepButton.setEnabled(false);
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
        // Quanto maior o valor, menor o delay (mais rápido)
        int delay = 500 / speed; // De 500ms (lento) a 50ms (rápido)
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
        Color col = colors.get(entityClass);
        return (col == null) ? UNKNOWN_COLOR : col;
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
                    Terreno terrain = field.getTerrainAt(loc);
                    fieldView.drawMark(col, row, getTerrainColor(terrain));
                }
            }
        }

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Mapeia o tipo de terreno para sua cor. TIRA
     */
    private Color getTerrainColor(Terreno terrain) {
        switch(terrain) {
            case MOUNTAIN: return MOUNTAIN_COLOR;
            case RIVER: return RIVER_COLOR;
            case BURROW: return BURROW_COLOR;
            case CAVE: return CAVE_COLOR;
            case DENSE_VEGETATION: return VEGETATION_COLOR;
            case PLAIN:
            default: return PLAIN_COLOR;
        }
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
        private final int GRID_VIEW_SCALING_FACTOR = 8;

        private int gridWidth, gridHeight;
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

        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        public void preparePaint() {
            if(!size.equals(getSize())) {
                size = getSize();
                fieldImage = createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) xScale = GRID_VIEW_SCALING_FACTOR;
                
                yScale = size.height / gridHeight;
                if(yScale < 1) yScale = GRID_VIEW_SCALING_FACTOR;
            }
        }

        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(fieldImage != null) {
                g.drawImage(fieldImage, 0, 0, null);
            }
        }
    }
}