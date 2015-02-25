/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganoapp.core;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author edmundophie
 */
public class StandardLSB implements Stegano{
    private File messageFile;
    private int messageSize;
    private int maxMessageSize;
    private File coverObject;
    private String stegoKey;
    private int seed=0;
    
    @Override
    public void setCoverObject(File imageFile) {
        coverObject = imageFile;
        maxMessageSize = (int) (imageFile.length()/8);
    }

    @Override
    public void setKey(String key) {
        stegoKey = key;
        for(int i=0;i<key.length();++i)
            seed += (int)key.charAt(i);
    }

    @Override
    public int getMaxMsgSize() {
        return maxMessageSize;
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
    
    private String getBits(Byte b) {
        String result = "";
        for(int i=7;i>=0;--i)
            result += (b&(1<<i))==0 ? "0" : "1";
        return result;
    }
    
    private String getBits(byte b[]) {
        String result = "";
        for(int i=0;i<b.length;++i)
            result += getBits(b[i]);
        return result;
    }
    
    @Override
    public File getSteganoObject() {
        // Dapatkan array of byte dari messageFile dan coverObjectFile
        byte[] messageData = null;
        byte[] coverObjData = null;
        try {
            messageData = convertFile2Bytes(messageFile);
            coverObjData = convertFile2Bytes(coverObject);
        } catch (IOException ex) {
            System.out.println("Error: convertFile2Bytes fail. " + ex);
        }
        
        String msgBits = getBits(messageData);
        String coverBits = getBits(coverObjData);
        StringBuilder stegoBits = new StringBuilder(coverBits);
        ArrayList<Integer> usedIdx = new ArrayList<Integer>();
        
        // Sisipkan bit pesan ke bit cover
        for(int i=0;i<msgBits.length();++i) {
            // Generate random index
            Random rand = new Random(seed);
            int idx;
            do {
                idx = rand.nextInt(maxMessageSize+1)+1; // 1 <= idx <= maxMessageSize
            } while(usedIdx.contains(idx));
            usedIdx.add(idx);
            
            stegoBits.setCharAt(idx*8-1, msgBits.charAt(i));
        }
        
        // Convert stegoBits ke bytes
        short a = Short.parseShort(stegoBits.toString(), 2);
        ByteBuffer b = ByteBuffer.allocate(2).putShort(a);
        byte[] stegoData = b.array();
        
        // Convert stegoBits ke file
        File stegoFile = new File("tempStego", ".tmp");
        FileOutputStream out;
        try {
            out = new FileOutputStream(stegoFile);
            out.write(stegoData);
            out.close();
        } catch (Exception ex) {
            System.out.println("Error: problem on writing stegoFile.");
        }
        
        return stegoFile;
    }
}
