import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class SimulatedAnnealing {

    private static final double INITIAL_TEMPERATURE = 10000;
    private static final double COOLING_RATE = 0.03;
    private static final double STOP_TEMPERATURE = 0.01; // given by me

    private ObjectiveSolution currentSolution;
    private ObjectiveSolution bestSolution;
    private List<Item> items;
    private int knapsackCapacity;
    private Random random;
    private BufferedWriter writer;

    public SimulatedAnnealing(List<Item> items, int knapsackCapacity, String outputFile) throws IOException {
        this.items = items;
        this.knapsackCapacity = knapsackCapacity;
        this.random = new Random();
        this.writer = new BufferedWriter(new FileWriter(outputFile));

        // Generate an initial feasible solution by greedy heuristic
        List<Integer> initialSolution = new ArrayList<>();
        int totalWeight = 0;
        for (Item item : items) {
            if (item.getWeight() + totalWeight <= knapsackCapacity) {
                initialSolution.add(1); // item is included
                totalWeight += item.getWeight();
            } else {
                initialSolution.add(0); // item is excluded
            }
        }
        this.currentSolution = new ObjectiveSolution(initialSolution, calculateValue(initialSolution));
        this.bestSolution = new ObjectiveSolution(new ArrayList<>(initialSolution),
                currentSolution.getObjectiveValue());
    }

    public ObjectiveSolution run() throws IOException {
        double t = INITIAL_TEMPERATURE;
        while (t > STOP_TEMPERATURE) {
            ObjectiveSolution neighbor = generateNeighbor();

            if (neighbor != null) {
                double delta = neighbor.getObjectiveValue() - currentSolution.getObjectiveValue(); // for accepting the
                                                                                                   // better solutions
                if (delta > 0
                        || random.nextDouble() < calculateAcceptanceProbability(currentSolution.getObjectiveValue(),
                                neighbor.getObjectiveValue(), t)) {
                    currentSolution = neighbor;
                    if (currentSolution.getObjectiveValue() > bestSolution.getObjectiveValue()) {
                        bestSolution = new ObjectiveSolution(new ArrayList<>(currentSolution.getSolution()),
                                currentSolution.getObjectiveValue());
                    }
                }
                t *= 1 - COOLING_RATE;
                writer.write(currentSolution.getObjectiveValue() + "\n");
            }
            // if we write t *= 1 - COOLING_RATE; here, it will run some non iterations
            // because of the structure of the while loop when we get null we don't update
            // "t" because actually it doesn't make an iteration so we write it inside the
            // neighbor != null if.

            // Write the current objective value to the file
        }

        writer.close();

        return bestSolution;
    }

    private ObjectiveSolution generateNeighbor() {
        List<Integer> currentSolutionList = currentSolution.getSolution();
        int[] includedIndices = IntStream.range(0, currentSolutionList.size())
                .filter(i -> currentSolutionList.get(i) == 1).toArray();
        int[] excludedIndices = IntStream.range(0, currentSolutionList.size())
                .filter(i -> currentSolutionList.get(i) == 0).toArray();

        // Create a copy of the current solution that we'll modify to generate the
        // neighbor
        List<Integer> neighborSolution = new ArrayList<>(currentSolutionList);

        // Randomly decide whether to add an item, remove an item, or swap an item
        int operation = random.nextInt(3);
        if (operation == 0 && excludedIndices.length > 0) { //// Add an item
            for (int i = 0; i < excludedIndices.length; i++) {
                int indexToInclude = excludedIndices[random.nextInt(excludedIndices.length)]; // for reducing complexity
                                                                                              // could solve with if
                                                                                              // statement
                neighborSolution.set(indexToInclude, 1); // Include item
                if (calculateWeight(neighborSolution) <= knapsackCapacity) {
                    return new ObjectiveSolution(neighborSolution, calculateValue(neighborSolution));
                } else {
                    neighborSolution.set(indexToInclude, 0); // Exclude item
                }
            }
            return null; // If we got here, no item could be added without exceeding the weight limit
        } else if (operation == 1 && includedIndices.length > 0) { //// Remove an item
            int indexToRemove = includedIndices[random.nextInt(includedIndices.length)];
            neighborSolution.set(indexToRemove, 0); // Exclude item
            return new ObjectiveSolution(neighborSolution, calculateValue(neighborSolution));
        } else if (operation == 2 && includedIndices.length > 0 && excludedIndices.length > 0) { //// Swap an item
            for (int i = 0; i < excludedIndices.length; i++) {
                int indexToRemove = includedIndices[random.nextInt(includedIndices.length)];
                int indexToInclude = excludedIndices[random.nextInt(excludedIndices.length)];
                neighborSolution.set(indexToRemove, 0); // Exclude item
                neighborSolution.set(indexToInclude, 1); // Include item
                if (calculateWeight(neighborSolution) <= knapsackCapacity) {
                    return new ObjectiveSolution(neighborSolution, calculateValue(neighborSolution));
                } else {
                    // Undo the swap
                    neighborSolution.set(indexToRemove, 1);
                    neighborSolution.set(indexToInclude, 0);
                }
            }
            return null; // If we got here, no swap could be made without exceeding the weight limit
        } else {
            // If we got here, the operation was not possible (e.g., trying to remove an
            // item from an empty knapsack)
            return null;
        }
    }

    private int calculateValue(List<Integer> solution) {
        return IntStream.range(0, solution.size()).filter(i -> solution.get(i) == 1).map(i -> items.get(i).getValue())
                .sum();
    }

    private int calculateWeight(List<Integer> solution) {
        return IntStream.range(0, solution.size()).filter(i -> solution.get(i) == 1).map(i -> items.get(i).getWeight())
                .sum();
    }

    private double calculateAcceptanceProbability(int currentValue, int neighborValue, double temperature) {
        if (neighborValue > currentValue) {
            return 1.0;
        } else {
            return Math.exp((neighborValue - currentValue) / temperature);
        }
    }
}
