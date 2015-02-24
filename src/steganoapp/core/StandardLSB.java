/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganoapp.core;

import java.io.File;

/**
 *
 * @author edmundophie
 */
public class StandardLSB implements Stegano{
    private byte[] message;
    private int messageSize;
    private File coverObject;
    private String stegoKey;
    
    @Override
    public void setCoverObject(File image) {
        
    }

    @Override
    public void setKey(String key) {
        
    }

    @Override
    public void setMessage(byte[] message) {
        
    }

    @Override
    public int getMaxMsgSize() {
        
        return 0;
    }

    @Override
    public void steganoObject(String filename, String filepath) {
        
    }
    
}
