import java.awt.*;
import javax.swing.*;
import java.util.HashMap;

/**
 * Visualização gráfica da simulação.
 * Exibe um retângulo colorido para cada posição do campo, representando seu conteúdo.
 * As cores para cada tipo de ator podem ser definidas com o método setColor.
 * 
 * @author David J. Barnes, Michael Kolling
 * @version 2002-04-23 (adaptado por Gabriela)
 */
public class SimulatorView extends JFrame {
    private static final Color EMPTY_COLOR = Color.white;
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Passo: ";
    private final String POPULATION_PREFIX = "População: ";

    private JLabel stepLabel, population;
    private FieldView fieldView;
    private HashMap<Class<?>, Color> colors;
    private FieldStats stats;

    /**
     * Cria a visualização com altura e largura especificadas.
     */
    public SimulatorView(int height, int width) {
        stats = new FieldStats();
        colors = new HashMap<>();

        setTitle("Simulação de Raposas e Coelhos");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);

        setLocation(100, 50);
        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        contents.add(stepLabel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    /**
     * Define a cor para uma determinada classe de ator.
     */
    public void setColor(Class<?> actorClass, Color color) {
        colors.put(actorClass, color);
    }

    /**
     * Retorna a cor associada à classe do ator.
     */
    private Color getColor(Class<?> actorClass) {
        return colors.getOrDefault(actorClass, UNKNOWN_COLOR);
    }

    /**
     * Atualiza a visualização com o estado atual do campo.
     */
    public void showStatus(int step, Field field) {
        if (!isVisible()) setVisible(true);

        stepLabel.setText(STEP_PREFIX + step);
        stats.reset();
        fieldView.preparePaint();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Object actor = field.getObjectAt(row, col);
                if (actor != null) {
                    stats.incrementCount(actor.getClass());
                    fieldView.drawMark(col, row, getColor(actor.getClass()));
                } else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }

        stats.countFinished();
        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Verifica se a simulação ainda é viável (mais de uma espécie viva).
     */
    public boolean isViable(Field field) {
        return stats.isViable(field);
    }

    /**
     * Componente gráfico interno que representa o campo.
     */
    private class FieldView extends JPanel {
        private final int GRID_VIEW_SCALING_FACTOR = 6;
        private int gridWidth, gridHeight;
        private int xScale, yScale;
        private Dimension size;
        private Graphics g;
        private Image fieldImage;

        public FieldView(int height, int width) {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        public void preparePaint() {
            if (!size.equals(getSize())) {
                size = getSize();
                fieldImage = createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = Math.max(size.width / gridWidth, GRID_VIEW_SCALING_FACTOR);
                yScale = Math.max(size.height / gridHeight, GRID_VIEW_SCALING_FACTOR);
            }
        }

        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
        }

        public void paintComponent(Graphics g) {
            if (fieldImage != null) {
                g.drawImage(fieldImage, 0, 0, null);
            }
        }
    }
}