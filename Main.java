import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static int knapsackCapacity = 300;
    private static List<Item> items;

    public static void main(String[] args) {
        try {
            items = Item.readCSV("Items.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Integer> initialSolution = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            initialSolution.add(0); // Initially no items are selected
        }
        ObjectiveSolution currentSolution = new ObjectiveSolution(initialSolution, calculateValue(initialSolution));
        ObjectiveSolution bestSolution = new ObjectiveSolution(new ArrayList<>(initialSolution),
                currentSolution.getObjectiveValue());

        SimulatedAnnealing sa;
        try {
            sa = new SimulatedAnnealing(items, knapsackCapacity, "output.csv");
            bestSolution = sa.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Best Solution: " + bestSolution.getSolution());
        System.out.println("Best Value: " + bestSolution.getObjectiveValue());
    }

    private static int calculateValue(List<Integer> solution) {
        int value = 0;
        int weight = 0;
        for (int i = 0; i < solution.size(); i++) {
            if (solution.get(i) == 1) {
                value += items.get(i).getValue();
                weight += items.get(i).getWeight();
            }
        }
        if (weight > knapsackCapacity) {
            return 0;
        } else {
            return value;
        }
    }
}
