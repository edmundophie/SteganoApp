/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganoapp.core;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author edmundophie
 */
public class StandardLSB implements Stegano{
    private File messageFile;
    private File coverObject;
    private String stegoKey;
    private int seed=0;
    
    @Override
    public void setCoverObject(File imageFile) {
        coverObject = imageFile;
    }

    @Override
    public void setKey(String key) {
        stegoKey = key;
        for(int i=0;i<key.length();++i)
            seed += (int)key.charAt(i);
    }

    @Override
    public int getMaxMsgSize() {
        return (int)messageFile.length()/8;
    }

    @Override
    public void setMessage(File messageFile) {
        this.messageFile = messageFile;
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
        BufferedImage coverImage;
        BufferedImage stegoImage = null;
        try {
            coverImage = ImageIO.read(coverObject);
            
            // Configure stegoImage attributes
            stegoImage = new BufferedImage(coverImage.getWidth(), coverImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D graphics = stegoImage.createGraphics();
            graphics.drawRenderedImage(coverImage, null);
            graphics.dispose();
        } catch (IOException ex) {
            System.out.println("Error on processing cover file. " + ex);
        }
        
        // Get image byte[]
        WritableRaster raster = stegoImage.getRaster();
        DataBufferByte buffer= (DataBufferByte) raster.getDataBuffer();
        byte stegoData[] = buffer.getData();
        
        // Get message byte[]
        byte msgData[] = null;
        try {
             msgData = Files.readAllBytes(messageFile.toPath());
        } catch (IOException ex) {
            System.out.println("Error on processing message file. " + ex);
        }
        
        // Get message length in byte[]
        byte msgLen[] = new byte[]{0,0,0, (byte)(msgData.length & 0x000000FF)};
        
        // Insert msgLen[] into stegoData[]
        insertMessage(stegoData, msgLen, 0);
        
        // Insert msgData[] into stegoData[]
        insertMessage(stegoData, msgData, 32);
        
        // Convert image to file
        File stegoFile = new File("stegoTemp");
        try {
            stegoFile.delete();
            ImageIO.write(stegoImage, "jpg", stegoFile);
        } catch (IOException ex) {
            System.out.println("Error on creating image file. " + ex);
        }
        
        return stegoFile;
    }
    
    private byte[] insertMessage(byte[] imgData, byte[] msgData, int offset) {
        for(int i=0;i<msgData.length;++i) {
            int msg = msgData[i];
            for(int j=7;j>=0;--j, ++offset) {
                int b = (msg >>> j) & 1;
                imgData[offset] = (byte) ((imgData[offset] & 0xFE) | b);
            }
        }
        return imgData;
    }
}
