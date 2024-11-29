package com.example;

import com.example.animal.AnimalDAO;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AnimalDAO animalDAO = new AnimalDAO();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Меню:");
            System.out.println("1. Додати новий");
            System.out.println("2. Показати всіх тварин");
            System.out.println("3. Оновити тварину");
            System.out.println("4. Видалити тварину");
            System.out.println("5. Вийти");
            System.out.print("Виберіть опцію: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Введіть ім'я тварини: ");
                    String name = scanner.nextLine();
                    System.out.print("Введіть вид тварини: ");
                    String species = scanner.nextLine();
                    animalDAO.addAnimal(name, species);
                    break;
                case 2:
                    List<String> animals = animalDAO.getAllAnimals();
                    for (String animal : animals) {
                        System.out.println(animal);
                    }
                    break;
                case 3:
                    System.out.print("Введіть ID тварини для оновлення: ");
                    int idToUpdate = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Введіть нове ім'я: ");
                    String newName = scanner.nextLine();
                    System.out.print("Введіть новий вид: ");
                    String newSpecies = scanner.nextLine();
                    animalDAO.updateAnimal(idToUpdate, newName, newSpecies);
                    break;
                case 4:
                    System.out.print("Введіть ID тварини для видалення: ");
                    int idToDelete = scanner.nextInt();
                    animalDAO.deleteAnimal(idToDelete);
                    break;
                case 5:
                    System.out.println("Вихід з програми.");
                    break;
                default:
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        } while (choice != 5);

        scanner.close();
    }
}