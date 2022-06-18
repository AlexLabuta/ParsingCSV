import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final String pathToAccountStatement = "data/movementList.csv";
    private static final HashMap<String, Double> expenseItem = new HashMap<>();
    private static double arrival = 0;
    private static double expenses = 0;

    public static void main(String[] args) {
        Path path = Paths.get(pathToAccountStatement);
        parsingAccountStatement();
        System.out.println("Общий доход: " + arrival);
        System.out.println("Общий расход: " + expenses);
        printExpenseCategory();
    }

    private static void parsingAccountStatement() {
        double expense;
        String category;
        try {
            List<String> lines = Files.readAllLines(Paths.get(pathToAccountStatement));
            lines.remove(0);

            for (String line : lines) {
                String[] fragments = line.split(",", 8);
                if (fragments.length != 8) {
                    System.out.println("Wrong line " + line);
                    continue;
                }

                arrival += Double.parseDouble(fragments[6].replace("\"", "").replace(",", "."));

                expense = Double.parseDouble(fragments[7].replace("\"", "").replace(",", "."));

                expenses += expense;

                if (expense > 0) {
                    String[] columnFragments = fragments[5].split("\\s{3,}");
                    category = columnFragments[1].substring(columnFragments[1].lastIndexOf("\\") + 1).trim().toUpperCase();
                    putExpenseCategoriesInMap(category, expense);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void putExpenseCategoriesInMap(String category, double expense) {
        if (expenseItem.containsKey(category)) {
            expenseItem.put(category, expenseItem.get(category) + expense);
        } else {
            expenseItem.put(category, expense);
        }

    }

    private static void printExpenseCategory() {
        expenseItem.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .forEach(System.out::println);

    }
}
