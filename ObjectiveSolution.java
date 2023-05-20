import java.util.List;

public class ObjectiveSolution {
    private List<Integer> solution;
    private int objectiveValue;

    public ObjectiveSolution(List<Integer> solution, int objectiveValue) {
        this.solution = solution;
        this.objectiveValue = objectiveValue;
    }

    // getters and setters

    public List<Integer> getSolution() {
        return solution;
    }

    public void setSolution(List<Integer> solution) {
        this.solution = solution;
    }

    public int getObjectiveValue() {
        return objectiveValue;
    }

    public void setObjectiveValue(int objectiveValue) {
        this.objectiveValue = objectiveValue;
    }
}
