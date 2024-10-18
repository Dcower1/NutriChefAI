package com.example.nutrichefai.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utilidades {


    public static String hashPassword(String password) {
        try {
            // Obtener instancia de SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convertir la contraseña en bytes
            byte[] hash = digest.digest(password.getBytes());

            // Convertir el hash en formato hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            // Devolver el hash como string
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= 6 && password.matches(".*[A-Z].*"); // Al menos 6 caracteres y 1 mayúscula
    }

}
