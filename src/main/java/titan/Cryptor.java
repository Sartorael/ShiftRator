package titan;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

public class Cryptor {
    public void cryptFromFileToFile(String cryptKey) throws Exception {
        System.out.println("Подготовка к шифрованию");
        File file = new File("D:\\Program Files\\Java\\trueProjects\\ShiftRator\\src\\main\\resources\\input.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = br.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        String strToCrypt = stringBuilder.toString();
        System.out.println(strToCrypt);
        Cipher cipher = Cipher.getInstance("AES");
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(128);
        SecretKey key = keygen.generateKey();
        if (!cryptKey.equals("no")){
            key = new SecretKeySpec(cryptKey.getBytes(),"AES");
        }
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte [] bytes = cipher.doFinal(strToCrypt.getBytes());
        FileOutputStream fos = new FileOutputStream("D:\\Program Files\\Java\\trueProjects\\ShiftRator\\src\\main\\resources\\output.txt"); // указываем путь к файлу
        fos.write(bytes);
        fos.close();
        byte[] encodedKey = key.getEncoded();
        FileOutputStream kos = new FileOutputStream("D:\\Program Files\\Java\\trueProjects\\ShiftRator\\src\\main\\resources\\key.txt");
        kos.write(encodedKey);
    }

    public void decryptFromFile (String cryptKey) throws Exception {
        File file = new File("D:\\Program Files\\Java\\trueProjects\\ShiftRator\\src\\main\\resources\\output.txt");
        byte [] bytesOfFile = Files.readAllBytes(file.toPath());
            Cipher decryptCipher = Cipher.getInstance("AES");
            File file1 = new File("D:\\Program Files\\Java\\trueProjects\\ShiftRator\\src\\main\\resources\\key.txt");
            byte[] bytesOfKey = Files.readAllBytes(file1.toPath());
            if (!cryptKey.equals("no")){
                bytesOfKey = cryptKey.getBytes();
            }
            SecretKey key = new SecretKeySpec(bytesOfKey, "AES");
            decryptCipher.init(Cipher.DECRYPT_MODE,key);
            byte [] decodedFile = decryptCipher.doFinal(bytesOfFile);
        FileOutputStream fos = new FileOutputStream("D:\\Program Files\\Java\\trueProjects\\ShiftRator\\src\\main\\resources\\decodeFile.txt"); // указываем путь к файлу
        fos.write(decodedFile);
        fos.close();
    }
    public void cryptString(String cryptKey ,String stringToCrypt)throws Exception{
        SecretKey secretKey;
        if ("no".equals(cryptKey)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            secretKey = keyGenerator.generateKey();
            System.out.println("Ваш ключ шифрования: " +
                    Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        } else {
            secretKey = new SecretKeySpec(Base64.getDecoder().decode(cryptKey), "AES");
            System.out.println("Ваш ключ шифрования: " + cryptKey);
        }

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(stringToCrypt.getBytes(StandardCharsets.UTF_8));
        String encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);
        System.out.println("Ваш зашифрованый текст: " + encryptedString);
    }
    public void decryptString(String cryptKey, String stringToDecrypt)throws Exception{
        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(cryptKey), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(stringToDecrypt));
        String decryptedString = new String(decryptedBytes, StandardCharsets.UTF_8);
        System.out.println("Ваш расшифрованый текст: " + decryptedString);
    }
    public void directoryCrypt(String cryptKey, String filePath) throws Exception {
        SecretKey secretKey;
        if ("no".equals(cryptKey)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            secretKey = keyGenerator.generateKey();
        } else {
            secretKey = new SecretKeySpec(Base64.getDecoder().decode(cryptKey), "AES");
        }
        File file = new File(filePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    directoryCrypt(cryptKey, f.getAbsolutePath());
                }
            }
        } else {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            String newFilePath = file.getParent() + "/" + file.getName() + ".crypt";
            try (FileInputStream in = new FileInputStream(file);
                 FileOutputStream out = new FileOutputStream(newFilePath)) {
                byte[] buffer = new byte[2048];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    byte[] encryptedBytes = cipher.update(buffer, 0, bytesRead);
                    if (encryptedBytes != null) {
                        out.write(encryptedBytes);
                    }
                }
                byte[] finalBytes = cipher.doFinal();
                if (finalBytes != null) {
                    out.write(finalBytes);
                }
                System.out.println("Шифрование прошло успешно: " + file.getAbsolutePath() +
                        " => " + newFilePath);
                System.out.println("Generated key (base64-encoded): " +
                        Base64.getEncoder().encodeToString(secretKey.getEncoded()));
            }
        }
    }

    public void directoryDecrypt(String cryptKey, String directoryPath) throws Exception {
        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(cryptKey), "AES");
        File directory = new File(directoryPath);
        decryptDirectory(secretKey, directory);
    }

    private void decryptDirectory(SecretKey secretKey, File directory) throws Exception {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".crypt")) {
                        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                        cipher.init(Cipher.DECRYPT_MODE, secretKey);
                        String newFilePath = file.getParent() + "/" + file.getName().replace(".crypt", "");
                        try (FileInputStream in = new FileInputStream(file);
                             FileOutputStream out = new FileOutputStream(newFilePath)) {
                            byte[] buffer = new byte[2048];
                            int bytesRead;
                            while ((bytesRead = in.read(buffer)) != -1) {
                                byte[] decryptedBytes = cipher.update(buffer, 0, bytesRead);
                                if (decryptedBytes != null) {
                                    out.write(decryptedBytes);
                                }
                            }
                            byte[] finalBytes = cipher.doFinal();
                            if (finalBytes != null) {
                                out.write(finalBytes);
                            }
                            System.out.println("Ваша расшифровка: " + file.getAbsolutePath() +
                                    " => " + newFilePath);
                            file.delete();
                        }
                    } else if (file.isDirectory()) {
                        decryptDirectory(secretKey, file);
                    }
                }
            }
        }
    }
}


