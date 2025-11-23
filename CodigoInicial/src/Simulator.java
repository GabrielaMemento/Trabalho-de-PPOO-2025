import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a field containing
 * rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-09
 */
public class Simulator
{
    // The private static final variables represent 
    // configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 50;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 50;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;    

    // The list of animals in the field
    private final ArrayList<Animal> animals;
    // The current state of the field.
    private Field field;
    // A second field, used to build the next stage of the simulation.
    private Field updatedField;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;

    private final Random rand = new Random();
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        animals = new ArrayList<>();
        field = new Field(depth, width);

        view = new SimulatorView(depth, width);
        view.setColor(Fox.class, Color.blue);
        view.setColor(Rabbit.class, Color.orange);

        updatedField = new Field(depth, width);

        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * e.g. 500 steps.
     */
    public void runLongSimulation()
    {
        simulate(500);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;
        List<Animal> newAnimals = new ArrayList<>();

        // let all animals act
        for(Iterator<Animal> iter = animals.iterator(); iter.hasNext(); ) {
            Animal animal = iter.next();
            animal.act(newAnimals);
            if (!animal.isAlive()) {
                iter.remove();
            }
        }

        // add new born animals to the list of animals
        animals.addAll(newAnimals);

        // Swap the field and updatedField at the end of the step.
        Field temp = field;
        field = updatedField;
        updatedField = temp;
        updatedField.clear();

        // display the new field on screen
        view.showStatus(step, field);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        animals.clear();
        field.clear();
        updatedField.clear();
        populate(field);
        
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
        /**
         * Populate the field with foxes and rabbits.
         */
        private void populate(Field field)
        {
            field.clear();
            for(int row = 0; row < field.getDepth(); row++) {
                for(int col = 0; col < field.getWidth(); col++) {
                    double random = rand.nextDouble();
                    Location location = new Location (row,col);

                    if(random <= FOX_CREATION_PROBABILITY) {
                        animals.add(new Fox(true,field,location));
                    }
                    else if(random <= RABBIT_CREATION_PROBABILITY) {
                        animals.add(new Rabbit(true,field,location));
                    }
                }
            }
        }
    }

