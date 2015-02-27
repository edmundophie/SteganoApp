/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganoapp.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import sun.misc.IOUtils;

/**
 *
 * @author calvin-pc
 */
public class FourPixelDiffrenceDeStegano implements DeStegano{

    public int treshold = 6; 
    public int kl = 1; //Number of bit subsitution when 'smooth'
    public int kh = 3; //Number of bit subsitution when 'rough'
    public int msgSize;
    
    private BufferedImage cover;
    private Random numberGen;
    
    @Override
    public void setCoverObject(File cover) {
        throw  new UnsupportedOperationException();
    }

    @Override
    public void setKey(String key) {
        int hash=7;
        for (int i=0; i < key.length(); i++) {
            hash = hash*31+key.charAt(i) % 10000;
        }
        numberGen = new Random();
        numberGen.setSeed(hash);
    }

    @Override
    public void setMsgSize(int size) {
        msgSize = size;
    }

    @Override
    public void setStegoObject(File stegoObj) {
        try {
            this.cover = ImageIO.read(stegoObj);
        } catch (IOException ex) {
            Logger.getLogger(FourPixelDiffrenceStegano.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    @Override
    public File deSteganoObject() {
        int[] randomList; int rowSize, columnSize;
        byte[][][] RGBDimension;
        byte[] messageByte;
        int messageByteOffset = 0, messageBitOffset = 0;
        
        rowSize = (cover.getWidth())/2;
        columnSize = (cover.getHeight())/2;
        
        //Buat list random unik
        randomList = new int [rowSize * columnSize];
        for (int i = 0; i < columnSize; i++) {
            for (int j = 0; j < rowSize; j++) {
                randomList[i*rowSize + j] = i*rowSize + j;
            }
        }
        for (int i = columnSize - 1; i >= 0; i--) {
            for (int j = rowSize - 1; j >=0; j--) {
                if (i == 0 && j == 0) continue;
                int n = Math.abs(numberGen.nextInt()) % (i*rowSize + j);
                int temp = randomList[i*rowSize + j];
                randomList[i*rowSize + j] = randomList[n];
                randomList[n] = temp;
            }
        }
        
        RGBDimension = new byte[3][cover.getHeight()][cover.getWidth()];
        for (int i = 0; i < cover.getHeight(); i++) {
            for (int j = 0; j < cover.getWidth(); j++) {
                int rgb = cover.getRGB(j, i);
                for (int bitOffset = 0; bitOffset != 24; bitOffset += 8) {
                    byte value = (byte)((rgb >> bitOffset) % 256); //get spesific r/g/b value
                    RGBDimension[bitOffset/8][i][j] = value;
                }
            }
        }
        
        messageByte = new byte [msgSize];
        
        for (int i = 0; i < randomList.length 
                && messageByteOffset < messageByte.length; i++) { //until all message extracted
            for (int dim = 0; dim < 3; dim++) {
                int x = randomList[i] / rowSize * 2;
                int y = randomList[i] % rowSize * 2;
                int value1 = byteToUnsignedInt(RGBDimension[dim][x][y]);
                int value2 = byteToUnsignedInt(RGBDimension[dim][x+1][y]);
                int value3 = byteToUnsignedInt(RGBDimension[dim][x][y+1]);
                int value4 = byteToUnsignedInt(RGBDimension[dim][x+1][y+1]);
                int k = getK(value1, value2, value3, value4);
                if (k == 0) continue; //Error block
                
                //Step 3
                int mask = 1;
                for (int i1 = 1; i1 < k; i1++) mask = (mask << 1) + 1;
                if (messageByteOffset < messageByte.length) { // still message to embeded
                    int pesan = value1 & mask;
                    if ((8 - messageBitOffset) >= k) {
                        messageByte[messageByteOffset] = (byte)(byteToUnsignedInt(messageByte[messageByteOffset])
                                                        | (pesan << (8-messageBitOffset-k)));
                        messageBitOffset += k;
                    }
                    else {
                        messageByte[messageByteOffset] = (byte)(byteToUnsignedInt(messageByte[messageByteOffset])
                                                        | (pesan >> (k- (8-messageBitOffset))));
                        messageByteOffset++;
                        if (messageByteOffset < messageByte.length) {
                            messageByte[messageByteOffset] = (byte)(byteToUnsignedInt(messageByte[messageByteOffset])
                                                        | (pesan << (8- (k- (8-messageBitOffset)))));
                        }
                        messageBitOffset = k- (8-messageBitOffset);
                    }
                    if (messageBitOffset == 8) {
                        messageBitOffset = 0;
                        messageByteOffset ++;
                    }
                }
                
                if (messageByteOffset < messageByte.length) { // still message to embeded
                    int pesan = value2 & mask;
                    if ((8 - messageBitOffset) >= k) {
                        messageByte[messageByteOffset] = (byte)(byteToUnsignedInt(messageByte[messageByteOffset])
                                                        | (pesan << (8-messageBitOffset-k)));
                        messageBitOffset += k;
                    }
                    else {
                        messageByte[messageByteOffset] = (byte)(byteToUnsignedInt(messageByte[messageByteOffset])
                                                        | (pesan >> (k- (8-messageBitOffset))));
                        messageByteOffset++;
                        if (messageByteOffset < messageByte.length) {
                            messageByte[messageByteOffset] = (byte)(byteToUnsignedInt(messageByte[messageByteOffset])
                                                        | (pesan << (8- (k- (8-messageBitOffset)))));
                        }
                        messageBitOffset = k- (8-messageBitOffset);
                    }
                    if (messageBitOffset == 8) {
                        messageBitOffset = 0;
                        messageByteOffset ++;
                    }
                }
                
                if (messageByteOffset < messageByte.length) { // still message to embeded
                    int pesan = value3 & mask;
                    if ((8 - messageBitOffset) >= k) {
                        messageByte[messageByteOffset] = (byte)(byteToUnsignedInt(messageByte[messageByteOffset])
                                                        | (pesan << (8-messageBitOffset-k)));
                        messageBitOffset += k;
                    }
                    else {
                        messageByte[messageByteOffset] = (byte)(byteToUnsignedInt(messageByte[messageByteOffset])
                                                        | (pesan >> (k- (8-messageBitOffset))));
                        messageByteOffset++;
                        if (messageByteOffset < messageByte.length) {
                            messageByte[messageByteOffset] = (byte)(byteToUnsignedInt(messageByte[messageByteOffset])
                                                        | (pesan << (8- (k- (8-messageBitOffset)))));
                        }
                        messageBitOffset = k- (8-messageBitOffset);
                    }
                    if (messageBitOffset == 8) {
                        messageBitOffset = 0;
                        messageByteOffset ++;
                    }
                }
                
                if (messageByteOffset < messageByte.length) { // still message to embeded
                    int pesan = value4 & mask;
                    if ((8 - messageBitOffset) >= k) {
                        messageByte[messageByteOffset] = (byte)(byteToUnsignedInt(messageByte[messageByteOffset])
                                                        | (pesan << (8-messageBitOffset-k)));
                        messageBitOffset += k;
                    }
                    else {
                        messageByte[messageByteOffset] = (byte)(byteToUnsignedInt(messageByte[messageByteOffset])
                                                        | (pesan >> (k- (8-messageBitOffset))));
                        messageByteOffset++;
                        if (messageByteOffset < messageByte.length) {
                            messageByte[messageByteOffset] = (byte)(byteToUnsignedInt(messageByte[messageByteOffset])
                                                        | (pesan << (8- (k- (8-messageBitOffset)))));
                        }
                        messageBitOffset = k- (8-messageBitOffset);
                    }
                    if (messageBitOffset == 8) {
                        messageBitOffset = 0;
                        messageByteOffset ++;
                    }
                }
            }
        }
        
        File stegoFile = new File("deStegoFile");//D:\\Kuliah\\image\\a.txt");
        try {
            stegoFile.delete();
            FileOutputStream output = new FileOutputStream(stegoFile);
            output.write(messageByte);
            output.close();
        } catch (IOException ex) {
            System.out.println("Error on creating image file. " + ex);
        }
        
        return stegoFile;
        
    }

    double CalculateD (int y1, int y2, int y3, int y4) {
        int ymin = Math.min(y4, Math.min(y3, Math.min(y1, y2)));
        double d;
        int sum = y1 + y2 + y3 + y4 - 4*ymin;
        d = sum / 3;
        return d;
    }
    
    //get the k-bit value for LSB subsitution, if 0 then no subsitution
    int getK (int v1,int v2,int v3,int v4) {
        double d = CalculateD(v1, v2, v3, v4);
        if (d <= treshold) { // smooth block
            int vmax = Math.max(v1, v2);
            vmax = Math.max(v3, vmax);
            vmax = Math.max(v4, vmax);

            int vmin = Math.min(v1, v2);
            vmin = Math.min(v3, vmin);
            vmin = Math.min(v4, vmin);
            if((vmax - vmin) > (2*treshold+2)) {// error block {
                return 0;
            }
            else {
                return kl;
            }
        } //edgy block
        else {
            return kh;
        }
    }
    public int byteToUnsignedInt (byte n) {
        int a = n & 0xFF;
        return a;
    }
}
