/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganoapp.core;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 *
 * @author edmundophie
 */
public class StandardLSB implements Stegano{
    private File messageFile;
    private int messageSize;
    private File coverObject;
    private String stegoKey;
    
    @Override
    public void setCoverObject(File imageFile) {
        coverObject = imageFile;
    }

    @Override
    public void setKey(String key) {
        stegoKey = key;
    }

    @Override
    public int getMaxMsgSize() {
        return messageSize;
    }

    @Override
    public void setMessage(File messageFile) {
        this.messageFile = messageFile;
        this.messageSize = (int) messageFile.length();
    }

    public static byte[] convertFile2Bytes(File file) throws IOException {
        byte[] data;
        data = Files.readAllBytes(file.toPath());
        return data;
    }
    
    @Override
    public File getSteganoObject() {
        File stegoObj = null;
        return stegoObj;
    }
}
