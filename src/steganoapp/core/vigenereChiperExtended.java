/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganoapp.core;

/**
 *
 * @author calvin-pc
 */
public class vigenereChiperExtended{
    public boolean isAutoKey;
    public String key;
    
    public String encrypt(String plaintext) {
        if (!isAutoKey && 
                (key == null || key.isEmpty())
                ) {
            throw new RuntimeException("without AutoKey,Key must filled"); //To change body of generated methods, choose Tools | Templates.
        }
        
        if (isAutoKey) {
            return encryptAutoKey(plaintext); 
        }
        
        else {
            return encryptNotAutoKey(plaintext);
        }    
    }

    public String decrypt(String chipertext) {
        if (!isAutoKey && 
                (key == null || key.isEmpty())
                ) {
            throw new RuntimeException("without AutoKey,Key must filled"); //To change body of generated methods, choose Tools | Templates.
        }
        
        if (isAutoKey) {
            return decryptAutoKey(chipertext); 
        }
        
        else {
            return decryptNotAutoKey(chipertext);
        }    
    }

    private String encryptNotAutoKey(String plaintext) {
        int posPlain ,posKey;
        posKey = posPlain = 0;
        
        StringBuffer chipertext = new StringBuffer();
        
        while (posPlain < plaintext.length()) {
            if (posKey >= key.length())
                posKey = posKey - key.length();
            
            int ch = ((int)plaintext.charAt(posPlain) + (int)key.charAt(posKey)) % 256;
            
            chipertext.append((char) ch);
            posPlain++;
            posKey++;
        }
        
        return new String(chipertext);
    }

    private String encryptAutoKey(String plaintext) {
        int posPlain ,posKey;
        posKey = posPlain = 0;
        
        StringBuffer chipertext = new StringBuffer();
        
        while (posPlain < plaintext.length()) {
            
            int ch = (int)plaintext.charAt(posPlain);
            
            // Getting the key (character)
            if (posKey < key.length())
                ch = (ch + (int)key.charAt(posKey)) % 256;
            else 
                ch = (ch + (int)plaintext.charAt(posKey - key.length())) % 256;
                
            chipertext.append((char) ch);
            posPlain++;
            posKey++;
        }
        
        return new String(chipertext);
    }

    private String decryptAutoKey(String chipertext) {
        int posChiper ,posKey;
        posKey = posChiper = 0;
        
        StringBuffer plaintext = new StringBuffer();
        
        while (posChiper < chipertext.length()) {
            
            int ch = (int)chipertext.charAt(posChiper);
            
            // Getting the key (character)
            if (posKey < key.length())
                ch = (ch - (int)key.charAt(posKey) + 256) % 256; // + 256 to ensure positive
            else 
                ch = (ch - (int)plaintext.charAt(posKey - key.length()) + 256) % 256;
                
            plaintext.append((char) ch);
            posChiper++;
            posKey++;
        }
        
        return new String(plaintext);
    }

    private String decryptNotAutoKey(String chipertext) {
        int posChiper ,posKey;
        posKey = posChiper = 0;
        
        StringBuffer plaintext = new StringBuffer();
        
        while (posChiper < chipertext.length()) {
            
            int ch = (int)chipertext.charAt(posChiper);
            
            // Getting the key (character)
            if (posKey >= key.length())
                posKey = posKey - key.length();
            
            ch = (ch - (int)key.charAt(posKey) + 256) % 256;
                
            plaintext.append((char) ch);
            posChiper++;
            posKey++;
        }
        
        return new String(plaintext);
    }
}
