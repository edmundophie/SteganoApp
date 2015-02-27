/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganoapp.core;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author edmundophie
 */
public class StandardLSBDeStegano implements DeStegano{
    private File coverFile;
    private String stegoKey;
    private int msgSize;
    private File stegoFile;
    private Random rand;
    private Stack<Integer> randomStack;

    @Override
    public void setCoverObject(File cover) {
        throw new RuntimeException("Deprecated.");
    }

    @Override
    public void setKey(String key) {
        stegoKey = key;
        int seed=0;
        for(int i=0;i<key.length();++i)
            seed += (int)key.charAt(i);
        rand = new Random(seed);
    }

    @Override
    public void setMsgSize(int size) {
        msgSize = size;
    }
    
    private int getMaxMessageSize() {
        return (int)stegoFile.length()/8;
    }

    @Override
    public void setStegoObject(File stegoObj) {
       this.stegoFile = stegoObj;
    }

    
    @Override
    public File deSteganoObject() {
        // Get stegano data
        BufferedImage stegoImage;
        byte[] stegoData = null;
        try {
            stegoImage = ImageIO.read(stegoFile);
            WritableRaster raster = stegoImage.getRaster();
            DataBufferByte buffer= (DataBufferByte) raster.getDataBuffer();
            stegoData = buffer.getData();
        } catch (IOException ex) {
            Logger.getLogger(StandardLSBDeStegano.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Make random list
        int randomList[] = new int [stegoData.length];
        for (int i = 0; i < randomList.length; i++)
            randomList[i] = i;
        for (int i = randomList.length - 1; i > 0; i--) {
                int n = Math.abs(rand.nextInt()) % i;
                int temp = randomList[i];
                randomList[i] = randomList[n];
                randomList[n] = temp;            
        }
        randomStack = new Stack<Integer>();
        for(int i=0; i<randomList.length; ++i)
            randomStack.push(randomList[i]);
        
        // Get message bit
        int length=0;
        int offset;
        for(int i=0;i<32;++i) {
            offset = randomStack.pop();
            length = (length<<1) | (stegoData[offset]&1);
        }
        byte[] result = new byte[length];
        for(int idxByte=0; idxByte<length;++idxByte){
            for(int bit=0;bit<8;++bit) {
                offset = randomStack.pop();
                result[idxByte] = (byte)((result[idxByte]<<1) | (stegoData[offset]&1));
            }
        }
        
        File messageFile = new File("tempMessage");
        try {
            FileOutputStream out = new FileOutputStream(messageFile);
            out.write(result);
            out.close();
        } catch (Exception ex) {
            System.out.println("Error on getting message file. " + ex);
        }
        return messageFile;
    }
    
}
