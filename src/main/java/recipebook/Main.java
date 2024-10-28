package recipebook;

import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Client client = new Client();
        String input;

        System.out.println();
        System.out.println("Welcome to your RecipeBook");
        System.out.println("===============");

        System.out.println("\n\n\nWrite \"_all_\" to see all the recipes");
        System.out.println("Write \"_find_ name of recipe\" to find exactly one)");
        System.out.println("Write \"_add_ recipeName\" to add recipe");
        System.out.println("Write \"_delete_ recipeName\" to delete exciting recipe");
        System.out.println("Write \"_quit_\" to exit the application");

        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print(">>> ");
            input = scanner.nextLine();
            if (input.isEmpty()) continue;
            System.out.println();
        } while (client.processCommand(Objects.requireNonNull(input)));
    }
}