import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Item {
    private String name;
    private int value;
    private int weight;

    public Item(String name, int value, int weight) {
        this.name = name;
        this.value = value;
        this.weight = weight;
    }

    // getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    // static method to read the csv file and return a list of items
    public static List<Item> readCSV(String csvFile) throws IOException {
        List<Item> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            reader.readLine(); // Skip the header
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                items.add(new Item(fields[0], Integer.parseInt(fields[1].trim()), Integer.parseInt(fields[2].trim())));
            }
        }
        return items;
    }
}
