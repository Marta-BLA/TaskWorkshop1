package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {


    static final String FILE_NAME = "tasks.csv";  // stala - nazwę pliku, w którym znajdować się będą informacje o zadaniach
    static final String[] OPTIONS = {"add", "remove", "list", "exit"};  // stala - listę dostępnych w programie opcji.
    static String[][] tasks;              // odamy również pole reprezentujące tablicę, w której będziemy przechowywać wczytane z pliku informacje:


    public static void printOptions(String[] tab) {

        System.out.println(ConsoleColors.BLUE + "Please select an option: ");
        System.out.println(ConsoleColors.RESET);
        for (String option : tab) {                 // ?
            System.out.println(option);
        }
    }

    public static void main(String[] args) {
        printOptions(OPTIONS);
        tasks = loadDataToTab(FILE_NAME);  //Wywołujemy metodę w pierwszej lini metody main, tak by dane były dostępne od początku działania programu. Jednocześnie zwracany wynik przypisujemy do zdefiniowanej w klasie zmiennej tasks;

        //Dodamy teraz odrobinę dynamiki do naszego programu, w tym celu do metody main dodamy pobieranie danych
        // od użytkownika. Będą to opcje, które użytkownik ma do wyboru. W tym celu wykorzystamy pętlę while oraz
        // znany z zajęć Scanner.
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {

            String input = scanner.nextLine();
            switch (input) {
                case "exit":
                    saveTabToFile(FILE_NAME, tasks);
                        System.out.println(ConsoleColors.RED + "Bye, bye.");
                       System.exit(0);  //program sie zatrzymuje
                    break;
                case "add":
                    addTask();
                    break;
                case "remove":
                    removeTask(tasks, getTheNumber());
                    System.out.println("Value was successfully deleted.");
                    break;
                case "list":
                    printTab(tasks);
                    break;
                default:
                    System.out.println("Please select a correct option.");
            }
            printOptions(OPTIONS);
        }

//Dodajemy również instrukcje switch, która posłuży nam do wywołania poszczególnych opcji, w kolejnych etapach
// dodamy jeszcze dodatkową opcję do wyjścia z programu.
//
//Dla opcji list wywołujemy wcześniej utworzoną metodę.


    }

    public static String[][] loadDataToTab(String fileName) {
        Path dir = Paths.get(fileName);        //utworzymy zmienną dir typu Path i sprawdzimy czy zadany plik istnieje
        if (!Files.exists(dir)) {
            System.out.println("File not exist.");
            System.exit(0);
        }

        String[][] tab = null;   //Skoro metoda ma zwracać tablicę musimy ją utworzyć:
        try {
            List<String> strings = Files.readAllLines(dir);    //Korzystając z metody readAllLines klasy Files wczytamy zawartość pliku.
            tab = new String[strings.size()][strings.get(0).split(",").length];  //Do zmiennej tab przypisujemy nową tablicę dwuwymiarową, której rozmiar ustalamy dzięki:

            //strings.size() - jest to ilość wczytanych wierszy.
            //strings.get(0).split(",").length - jest to ilość elementów pojedynczego wiersza

            for (int i = 0; i < strings.size(); i++) {        //Wczytane z pliku dane umieścimy teraz w tablicy:
                String[] split = strings.get(i).split(",");
                for (int j = 0; j < split.length; j++) {
                    tab[i][j] = split[j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();       //a to składa się w całości "printStackTrace" w języku Java.
            // W efekcie końcowym mamy tłumaczenie: "wydrukuj śledzenie stosu". Jakkolwiek by to nie brzmiało
            // komicznie, chodzi o wypisanie wszystkich metod umieszczonych na stosie, od momentu uruchomienia
            // programu do momentu zgłoszenia wyjątku
        }
        return tab;

    }

    // dodamy możliwość wyświetlania listy zadań znajdującej się w zmiennej tasks.
    public static void printTab(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }      //Metoda ta wyświetla elementy tablicy dwuwymiarowej poprzedzone dodatkowo licznikiem z pętli.

    private static void addTask() {

//W pierwszej kolejności utworzymy obiekt typu Scanner i za jego pomocą pobierzemy 3 wartości:

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add task description");
        String description = scanner.nextLine();
        System.out.println("Please add task due date");
        String dueDate = scanner.nextLine();
        System.out.println("Is your task important: true/false");
        String isImportant = scanner.nextLine();


//Następnie tworzymy kopie tablicy powiększoną o jeden i uzupełniamy elementy nowo pobranymi informacjami.

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = dueDate;
        tasks[tasks.length - 1][2] = isImportant;
    }

    //skorzystamy z klasy ArrayUtils oraz NumberUtils z Apache Commons Lang.
    // Zamiast umieszczać cały kod w ramach jednej metody rozbijemy go na kilka mniejszych.
    // Dzięki temu będziemy mogli je w przyszłości ponownie wykorzystać, lub np. przenieść do innej klasy, czy projektu

    //Pierwsza z metod będzie odpowiedzialna za sprawdzanie czy wartość da się poprawnie przekształcić do
    // postaci numerycznej oraz czy jest większa lub równa zero.
    public static boolean isNumberGreaterEqualZero(String input) {
        if (NumberUtils.isParsable(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }
    //Odpowiedzialność następnej metody to, pobieranie wartości od użytkownika za pomocą klasy Scanner z
    // jednoczesnym sprawdzaniem czy jest ona poprawna.
    public static int getTheNumber() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please select number to remove.");

            String n = scanner.nextLine();
            while (!isNumberGreaterEqualZero(n)) {
                    System.out.println("Incorrect argument passed. Please give number greater or equal 0");
                    scanner.nextLine();
                }
           return Integer.parseInt(n);
        }

        //Dodatkowo w tej metodzie sprawdzimy warunek czy podany index nie jest większy niż rozmiar tablicy.
        private static void removeTask(String[][] tab, int index) {
                try {
                        if (index < tab.length) {
                                tasks = ArrayUtils.remove(tab, index);
                            }
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        System.out.println("Element not exist in tab");
                    }
            }

            //Zamykanie programu i zapis do pliku

//Następnie w pętli przekształcamy tablicę dwuwymiarową na jednowymiarową - tak by możliwy był zapis
// poszczególnych lini. Wykorzystujemy w tym celu metodę join klasy `String:
    public static void saveTabToFile(String fileName, String[][] tab) {
            Path dir = Paths.get(fileName);

            String[] lines = new String[tasks.length]; //Przyjmujemy że nadpisujemy aktualną jego zawartość
           for (int i = 0; i < tab.length; i++) {
                   lines[i] = String.join(",", tab[i]);
                }

            try {
                   Files.write(dir, Arrays.asList(lines));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        }
}

// Michala warsztat
//package pl.coderslab.workshops.taskmanager;


//        import org.apache.commons.lang3.ArrayUtils;
//        import org.apache.commons.lang3.StringUtils;
//
//        import java.io.File;
//        import java.io.FileNotFoundException;
//        import java.io.IOException;
//        import java.io.PrintWriter;
//        import java.util.Scanner;
//
//        import static pl.coderslab.workshops.taskmanager.ConsoleColors.*;
//
//public class Application {
//
//    private static final String[] OPTIONS = {"add", "remove", "list", "exit"};
//    private static final String EXIT_OPTION = "exit";
//    private static final String TASKS_FILE_NAME = "tasks.csv";
//
//    private static String[][] tasks = new String[0][];
//
//    public static void main(String[] args) {
//        run();
//    }
//
//    public static void run() {
//        printWelcomeMessage();
//        loadTasks();
//        while (true) {
//            printMenu();
//            String option = selectOption();
//            if (!validOption(option)) {
//                printErrorMessage(option);
//                continue;
//            }
//            executeOption(option);
//            if (isExitOption(option)) {
//                break;
//            }
//        }
//        saveTasks();
//        printExitMessage();
//    }
//
//    private static void saveTasks() {
//        try (PrintWriter writer = new PrintWriter(new File(TASKS_FILE_NAME))) {
//            for (String[] task : tasks) {
//                String taskLine = StringUtils.join(task, ",");
//                writer.println(taskLine);
//            }
//            System.out.println(PURPLE + tasks.length + " tasks have been written" + RESET);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void loadTasks() {
//        StringBuilder dataBuilder = new StringBuilder();
//        try (Scanner scanner = new Scanner(new File(TASKS_FILE_NAME))) {
//            while (scanner.hasNextLine()) {
//                dataBuilder.append(scanner.nextLine()).append(";");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String[] taskLines = dataBuilder.toString().split(";");
//        for (String taskLine : taskLines) {
//            String[] task = taskLine.split(",");
//            tasks = ArrayUtils.add(tasks, task);
//        }
//        System.out.println(PURPLE + tasks.length + " tasks have bean read" + RESET);
//    }
//
//    private static void printExitMessage() {
//        System.out.println(BLUE + "Bye, bye" + RESET);
//    }
//
//    private static void executeOption(String option) {
//        switch (option) {
//            case "add": {
//                addTask();
//                break;
//            }
//            case "remove": {
//                removeTask();
//                break;
//            }
//            case "list": {
//                listTasks();
//                break;
//            }
//            default: {
//                break;
//            }
//        }
//    }
//
//    private static void listTasks() {
//        System.out.printf("%2s | %-40s | %-10s | %-5s |%n", "Lp", "Opis", "Data", "Pr");
//        System.out.println("-".repeat(68));
//        for (int i = 0; i < tasks.length; i++) {
//            String[] task = tasks[i];
//            System.out.printf("%2d | %40s | %10s | %5s |%n", i, task[0].substring(0, Math.min(task[0].length(), 40)), task[1], task[2]);
//        }
//        System.out.println("-".repeat(68));
//    }
//
//    private static void removeTask() {
//        if (tasks.length == 0) {
//            System.out.println(RED + "No tasks to remove" + RESET);
//            return;
//        }
//        System.out.print(BLUE + "Please select index to remove > " + RESET);
//        Scanner scanner = new Scanner(System.in);
//        int index;
//        while (true) {
//            while (!scanner.hasNextInt()) {
//                scanner.nextLine();
//                System.out.print(RED + "Invalid argument passed. Please give number between 0 and " + (tasks.length - 1) + ": " + RESET);
//            }
//            index = scanner.nextInt();
//            if (index >= 0 && index < tasks.length) {
//                break;
//            } else {
//                System.out.print(RED + "Invalid argument passed. Please give number between 0 and " + (tasks.length - 1) + ": " + RESET);
//            }
//        }
//        String[] task = tasks[index];
//        System.out.print(BLUE + "Please confirm (Y/y) to remove task '" + task[0] + "' > " + RESET);
//        scanner = new Scanner(System.in);
//        String confirmed = scanner.nextLine();
//        if ("y".equalsIgnoreCase(confirmed)) {
//            tasks = ArrayUtils.remove(tasks, index);
//            System.out.println("Task was successfully deleted");
//        }
//    }
//
//    private static void addTask() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Please add task description > ");
//        String description = scanner.nextLine().trim();
//        System.out.print("Please add task due date > ");
//        String dueDate = scanner.nextLine().trim();
//        String important = null;
//        do {
//            System.out.print("Is your task important (true/false)? > ");
//            important = scanner.nextLine().trim();
//        } while (!("false".equals(important) || "true".equals(important)));
//        tasks = ArrayUtils.add(tasks, new String[]{description, dueDate, important});
//        System.out.println("Task was successfully added");
//    }
//
//    private static boolean isExitOption(String option) {
//        return EXIT_OPTION.equalsIgnoreCase(option);
//    }
//
//    private static void printErrorMessage(String option) {
//        System.out.println(RED + "Invalid menu option: '" + option + "'" + RESET);
//    }
//
//    private static boolean validOption(String option) {
//        return ArrayUtils.contains(OPTIONS, option);
//    }
//
//    private static String selectOption() {
//        Scanner scanner = new Scanner(System.in);
//        return scanner.nextLine().trim().toLowerCase();
//    }
//
//    private static void printMenu() {
//        System.out.println("\nAvailable options:");
//        for (String option : OPTIONS) {
//            System.out.println(" " + option);
//        }
//        System.out.print(BLUE + "Please select an option > " + RESET);
//    }
//
//    private static void printWelcomeMessage() {
     //   System.getProperties().list(System.out);
//        String userName = System.getProperty("user.name");
//        System.out.println("Hello " + userName);
//    }
//}



//
//        wersja dostepna na stronie jako rozwiaznie    !!!!!!!!!!!!!!!!!!!!!!!!

//public class TaskManager {
//
//    static final String FILE_NAME = "tasks.csv";
//    static final String[] OPTIONS = {"add", "remove", "list", "exit"};
//    static String[][] tasks;
//
//    public static void printOptions(String[] tab) {
//        System.out.println(ConsoleColors.BLUE);
//        System.out.println("Please select an option: " + ConsoleColors.RESET);
//        for (String option : tab) {
//            System.out.println(option);
//        }
//    }
//
//    public static void main(String[] args) {
//        tasks = loadDataToTab(FILE_NAME);
//        printOptions(OPTIONS);
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNextLine()) {
//            String input = scanner.nextLine();
//
//            switch (input) {
//                case "exit":
//                    saveTabToFile(FILE_NAME, tasks);
//                    System.out.println(ConsoleColors.RED + "Bye, bye.");
//                    System.exit(0);
//                    break;
//                case "add":
//                    addTask();
//                    break;
//                case "remove":
//                    removeTask(tasks, getTheNumber());
//                    System.out.println("Value was successfully deleted.");
//                    break;
//                case "list":
//                    printTab(tasks);
//                    break;
//                default:
//                    System.out.println("Please select a correct option.");
//            }
//
//            printOptions(OPTIONS);
//        }
//
//
//    }
//
//    public static int getTheNumber() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please select number to remove.");
//
//        String n = scanner.nextLine();
//        while (!isNumberGreaterEqualZero(n)) {
//            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
//            scanner.nextLine();
//        }
//        return Integer.parseInt(n);
//    }
//
//    private static void removeTask(String[][] tab, int index) {
//        try {
//            if (index < tab.length) {
//                tasks = ArrayUtils.remove(tab, index);
//            }
//        } catch (ArrayIndexOutOfBoundsException ex) {
//            System.out.println("Element not exist in tab");
//        }
//    }
//
//    public static boolean isNumberGreaterEqualZero(String input) {
//        if (NumberUtils.isParsable(input)) {
//            return Integer.parseInt(input) >= 0;
//        }
//        return false;
//    }
//
//    public static void printTab(String[][] tab) {
//        for (int i = 0; i < tab.length; i++) {
//            System.out.print(i + " : ");
//            for (int j = 0; j < tab[i].length; j++) {
//                System.out.print(tab[i][j] + " ");
//            }
//            System.out.println();
//        }
//    }
//
//    private static void addTask() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please add task description");
//        String description = scanner.nextLine();
//        System.out.println("Please add task due date");
//        String dueDate = scanner.nextLine();
//        System.out.println("Is your task important: true/false");
//        String isImportant = scanner.nextLine();
//        tasks = Arrays.copyOf(tasks, tasks.length + 1);
//        tasks[tasks.length - 1] = new String[3];
//        tasks[tasks.length - 1][0] = description;
//        tasks[tasks.length - 1][1] = dueDate;
//        tasks[tasks.length - 1][2] = isImportant;
//    }
//
//
//    public static String[][] loadDataToTab(String fileName) {
//        Path dir = Paths.get(fileName);
//        if (!Files.exists(dir)) {
//            System.out.println("File not exist.");
//            System.exit(0);
//        }
//
//        String[][] tab = null;
//        try {
//            List<String> strings = Files.readAllLines(dir);
//            tab = new String[strings.size()][strings.get(0).split(",").length];
//
//            for (int i = 0; i < strings.size(); i++) {
//                String[] split = strings.get(i).split(",");
//                for (int j = 0; j < split.length; j++) {
//                    tab[i][j] = split[j];
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return tab;
//    }
//
//
//    public static void saveTabToFile(String fileName, String[][] tab) {
//        Path dir = Paths.get(fileName);
//
//        String[] lines = new String[tasks.length];
//        for (int i = 0; i < tab.length; i++) {
//            lines[i] = String.join(",", tab[i]);
//        }
//
//        try {
//            Files.write(dir, Arrays.asList(lines));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }