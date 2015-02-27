/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganoapp.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import steganoapp.ui.SteganoAppUI;

/**
 *
 * @author calvin-pc
 */
public class vigenereDriver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        File messageFile = new File("D:\\Kuliah\\image\\data store.txt");
        File encryptedMessage = new File("D:\\Kuliah\\image\\enc.txt");
        File decryptedMessage = new File("D:\\Kuliah\\image\\dec.txt");
        String key = "Soda";
            try {
                encryptedMessage.delete();
                byte[] encrypted = Encrypt(Files.readAllBytes(messageFile.toPath()),key);
                FileOutputStream output = new FileOutputStream(encryptedMessage);
                output.write(encrypted);
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(SteganoAppUI.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
                return;
            }
            try {
                decryptedMessage.delete();
                byte[] decrypted = Decrypt(Files.readAllBytes(encryptedMessage.toPath()),key);
                FileOutputStream output = new FileOutputStream(decryptedMessage);
                output.write(decrypted);
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(SteganoAppUI.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
                return;
            }
    }
    
    public static byte[] Encrypt (byte[] plaintext, String key) {  
        StringBuilder sb = new StringBuilder();
        for (byte b : plaintext) {
            sb.append((char)b);
        }

        vigenereChiperExtended chpr = new vigenereChiperExtended();
        chpr.isAutoKey = false;
        chpr.key = key;

        String plainTextString = sb.toString();
        String chiperTextString = chpr.encrypt(plainTextString);
        sb = new StringBuilder(chiperTextString);
        byte r[] = new byte [sb.length()];
        for (int i = 0; i < sb.length(); i++) {
            r[i] = (byte) ((int)sb.charAt(i));
        }
        return r;
    }
    
    public static byte[] Decrypt (byte[] chipertext, String key) {  
        StringBuilder sb = new StringBuilder();
        for (byte b : chipertext) {
            sb.append((char)b);
        }

        vigenereChiperExtended chpr = new vigenereChiperExtended();
        chpr.isAutoKey = false;
        chpr.key = key;

        String chiperTextString = sb.toString();
        String plainTextString = chpr.decrypt(chiperTextString);
        sb = new StringBuilder(plainTextString);
        byte r[] = new byte [sb.length()];
        for (int i = 0; i < sb.length(); i++) {
            r[i] = (byte) ((int)sb.charAt(i));
        }
        return r;
    }
}
