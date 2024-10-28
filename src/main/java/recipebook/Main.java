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

        System.out.println("\n\n\nWrite \"all\" to see all the recipes\nOr write \"find [name of recipe]\" to find excactly one)");
        System.out.println("Input _help_ for list of commands\n");

        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print(">>> ");
            input = scanner.nextLine();
            if (input.isEmpty()) continue;
            System.out.println();
        } while (!client.processCommand(Objects.requireNonNull(input)));
    }
}