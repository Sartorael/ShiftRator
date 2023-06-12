package titan;

import java.util.Scanner;

public class Chooser {
    public void chooseJob() throws Exception{
        Scanner scanner = new Scanner(System.in);
        Cryptor cryptor = new Cryptor();
        while (true) {
            System.out.println("Введите команду:");
            System.out.println("1. Шивровании информации в вайле");
            System.out.println("2. Расширфовка информации в файле");
            System.out.println("3. Шифрование информации в предложении");
            System.out.println("4. Расшифровка информации в предложении");
            System.out.println("5. Запуск шифрования файла или папки");
            System.out.println("6. Запуск расшифрования файла или папки");
            System.out.println("7. Выйти");

            int choice = scanner.nextInt();
            String encryptionKey = "";
            switch (choice) {
                case 1:
                    System.out.println("Вы выбрали команду 1. Шивровании информации в вайле");
                    System.out.println("Введите ключ шифрования, если его нет введите no:");
                    encryptionKey = scanner.next();
                    cryptor.cryptFromFileToFile(encryptionKey);
                    break;
                case 2:
                    System.out.println("Вы выбрали команду 2. Расширфовка информации в файле");
                    System.out.println("Введите ключ шифрования, если его нет введите no:");
                    encryptionKey = scanner.next();
                    cryptor.decryptFromFile(encryptionKey);
                    break;
                case 3:
                    System.out.println("Вы выбрали команду 3. Шифрование информации в предложении");
                    System.out.println("Введите ключ шифрования, если его нет, введите no:");
                    String consumeN = scanner.nextLine();
                    encryptionKey = scanner.nextLine();
                    System.out.println("Введите предложение для шифрования:");
                    String stringToCrypt = scanner.nextLine();
                    cryptor.cryptString(encryptionKey, stringToCrypt);
                    break;
                case 4:
                    System.out.println("Вы выбрали команду 4. Расшифровка информации в предложении");
                    System.out.println("Введите ключ шифрования, если его нет введите no:");
                    String consumeN1 = scanner.nextLine();
                    encryptionKey = scanner.nextLine();
                    System.out.println("Введите предложение для кодирования");
                    String stringToDecrypt = scanner.nextLine();
                    try {
                        cryptor.decryptString(encryptionKey, stringToDecrypt);
                    }catch (Exception e) {
                        System.out.println("Вы ввели неверный ключ, повторите попытку");
                    }
                    break;
                case 5:
                    System.out.println("Вы выбрали команду 5. Запуск шифрования файла или папки");
                    System.out.println("Введите ключ шифрования, если его нет введите no:");
                    String consumeN2 = scanner.nextLine();
                    encryptionKey = scanner.nextLine();
                    System.out.println("Введите путь до папки ли файла:");
                    String directory = scanner.nextLine();
                    cryptor.directoryCrypt(encryptionKey,directory);
                    break;
                case 6:
                    System.out.println("Вы выбрали команду 6. Запуск расшифрования файла или папки");
                    System.out.println("Введите ключ шифрования:");
                    String consumeN3 = scanner.nextLine();
                    encryptionKey = scanner.nextLine();
                    System.out.println("Введите путь до папки ли файла:");
                    String directory1 = scanner.nextLine();
                    cryptor.directoryDecrypt(encryptionKey,directory1);
                    break;
                case 7:
                    System.out.println("Вы выбрали команду 6. Выход");
                    System.exit(0);
                default:
                    System.out.println("Выберите корректную команду");
            }
        }
    }
}
