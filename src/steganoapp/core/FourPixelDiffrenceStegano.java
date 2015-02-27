/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganoapp.core;

import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author calvin-pc
 */
public class FourPixelDiffrenceStegano implements Stegano{
    
    public int treshold = 6; 
    public int kl = 1; //Number of bit subsitution when 'smooth'
    public int kh = 3; //Number of bit subsitution when 'rough'
    
    private BufferedImage cover;
    private Random numberGen;
    private File message;
    
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

    @Override
    public void setCoverObject(File image) {        
        try {
            cover = ImageIO.read(image);
        } catch (IOException ex) {
            Logger.getLogger(FourPixelDiffrenceStegano.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
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
    public void setMessage(File messageFile) {
        message = messageFile;
    }

    @Override
    public int getMaxMsgSize() {
        if (cover == null) {return 0;}
        else {
            int sum = 0;
            for (int i = 0; i + 1< cover.getHeight(); i = i+2) { //getting 4 block at a time
                for (int j = 0; j + 1 < cover.getWidth(); j = j+2) {
                    int rgb1 = cover.getRGB(j, i);
                    int rgb2 = cover.getRGB(j, i + 1);
                    int rgb3 = cover.getRGB(j + 1, i);
                    int rgb4 = cover.getRGB(j + 1, i + 1);
                    for (int bitOffset = 0;bitOffset!=24;bitOffset += 8) {
                        int value1 = ((rgb1 >> bitOffset) % 256); //get spesific r/g/b value
                        int value2 = ((rgb2 >> bitOffset) % 256);
                        int value3 = ((rgb3 >> bitOffset) % 256);
                        int value4 = ((rgb4 >> bitOffset) % 256);
                        int k = getK(value1, value2, value3, value4);
                        sum += k*4;
                    }
                }
            }
            
            return sum/8;
        }
    }

    @Override
    public File getSteganoObject() { //TODO
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
        
        try {
            messageByte = Files.readAllBytes(message.toPath());
        } catch (IOException ex) {
            Logger.getLogger(FourPixelDiffrenceStegano.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
        for (int i = 0; i < randomList.length 
                && messageByteOffset < messageByte.length; i++) { //until all message embeded
            for (int dim = 0; dim < 3; dim++) {
                int x = randomList[i] / rowSize * 2;
                int y = randomList[i] % rowSize * 2;
                int value1 = byteToUnsignedInt(RGBDimension[dim][x][y]);
                int value2 = byteToUnsignedInt(RGBDimension[dim][x+1][y]);
                int value3 = byteToUnsignedInt(RGBDimension[dim][x][y+1]);
                int value4 = byteToUnsignedInt(RGBDimension[dim][x+1][y+1]);
                int k = getK(value1, value2, value3, value4);
                if (k == 0) continue; //Error block
                
                //Step 4&5
                if (messageByteOffset < messageByte.length) { // still message to embeded
                    if ((8 - messageBitOffset) >= k) {
                        int pesan = byteToUnsignedInt(messageByte[messageByteOffset]);
                        pesan = (pesan << messageBitOffset) >> (8 - k);
                        value1 = modifiedLSBSubsitution(k, value1, pesan);
                        messageBitOffset += k;
                    }
                    else {
                        int pesan = byteToUnsignedInt(messageByte[messageByteOffset]);
                        pesan = (pesan << messageBitOffset) >> (8 - k);
                        messageByteOffset++;
                        if (messageByteOffset < messageByte.length) {
                            int pesan2 = byteToUnsignedInt(messageByte[messageByteOffset]);
                            pesan += pesan2 >> (8 - (k - (8 - messageBitOffset)));
                        }
                        messageBitOffset = (k - (8 - messageBitOffset));
                        value1 = modifiedLSBSubsitution(k, value1, pesan);
                    }
                    if (messageBitOffset == 8) {
                        messageBitOffset = 0;
                        messageByteOffset ++;
                    }
                }
                
                if (messageByteOffset < messageByte.length) { // still message to embeded
                    if ((8 - messageBitOffset) >= k) {
                        int pesan = byteToUnsignedInt(messageByte[messageByteOffset]);
                        pesan = (pesan << messageBitOffset) >> (8 - k);
                        value2 = modifiedLSBSubsitution(k, value2, pesan);
                        messageBitOffset += k;
                    }
                    else {
                        int pesan = byteToUnsignedInt(messageByte[messageByteOffset]);
                        pesan = (pesan << messageBitOffset) >> (8 - k);
                        messageByteOffset++;
                        if (messageByteOffset < messageByte.length) {
                            int pesan2 = byteToUnsignedInt(messageByte[messageByteOffset]);
                            pesan += pesan2 >> (8 - (k - (8 - messageBitOffset)));
                        }
                        messageBitOffset = (k - (8 - messageBitOffset));
                        value2 = modifiedLSBSubsitution(k, value2, pesan);
                    }
                    if (messageBitOffset == 8) {
                        messageBitOffset = 0;
                        messageByteOffset ++;
                    }
                }
                
                if (messageByteOffset < messageByte.length) { // still message to embeded
                    if ((8 - messageBitOffset) >= k) {
                        int pesan = byteToUnsignedInt(messageByte[messageByteOffset]);
                        pesan = (pesan << messageBitOffset) >> (8 - k);
                        value3 = modifiedLSBSubsitution(k, value3, pesan);
                        messageBitOffset += k;
                    }
                    else {
                        int pesan = byteToUnsignedInt(messageByte[messageByteOffset]);
                        pesan = (pesan << messageBitOffset) >> (8 - k);
                        messageByteOffset++;
                        if (messageByteOffset < messageByte.length) {
                            int pesan2 = byteToUnsignedInt(messageByte[messageByteOffset]);
                            pesan += pesan2 >> (8 - (k - (8 - messageBitOffset)));
                        }
                        messageBitOffset = (k - (8 - messageBitOffset));
                        value3 = modifiedLSBSubsitution(k, value3, pesan);
                    }
                    if (messageBitOffset == 8) {
                        messageBitOffset = 0;
                        messageByteOffset ++;
                    }
                }
                
                if (messageByteOffset < messageByte.length) { // still message to embeded
                    if ((8 - messageBitOffset) >= k) {
                        int pesan = byteToUnsignedInt(messageByte[messageByteOffset]);
                        pesan = (pesan << messageBitOffset) >> (8 - k);
                        value4 = modifiedLSBSubsitution(k, value4, pesan);
                        messageBitOffset += k;
                    }
                    else {
                        int pesan = byteToUnsignedInt(messageByte[messageByteOffset]);
                        pesan = (pesan << messageBitOffset) >> (8 - k);
                        messageByteOffset++;
                        if (messageByteOffset < messageByte.length) {
                            int pesan2 = byteToUnsignedInt(messageByte[messageByteOffset]);
                            pesan += pesan2 >> (8 - (k - (8 - messageBitOffset)));
                        }
                        messageBitOffset = (k - (8 - messageBitOffset));
                        value4 = modifiedLSBSubsitution(k, value4, pesan);
                    }
                    if (messageBitOffset == 8) {
                        messageBitOffset = 0;
                        messageByteOffset ++;
                    }
                }
                
                //Step 6
                int step = 1 << k; //2 pangkat k
                int [] valuePossibiity = {
                    value1 - step, value1, value1 + step,
                    value2 - step, value2, value2 + step,
                    value3 - step, value3, value3 + step,
                    value4 - step, value4, value4 + step,
                };
                int [] bestValue = new int[4];
                int bestSquareMean = -1;
                
                value1 = byteToUnsignedInt(RGBDimension[dim][x][y]);
                value2 = byteToUnsignedInt(RGBDimension[dim][x+1][y]);
                value3 = byteToUnsignedInt(RGBDimension[dim][x][y+1]);
                value4 = byteToUnsignedInt(RGBDimension[dim][x+1][y+1]);
                
                for (int i1 = 0; i1 < 12; i1++){
                    valuePossibiity[i1] = byteToUnsignedInt((byte) valuePossibiity[i1]);
                }
                
                for (int i1 = 0; i1 < 3; i1++)
                for (int i2 = 3; i2 < 6; i2++)
                for (int i3 = 6; i3 < 9; i3++)
                for (int i4 = 9; i4 < 12; i4++) {
                    if (getK(valuePossibiity[i1],
                            valuePossibiity[i2],
                            valuePossibiity[i3],
                            valuePossibiity[i4]) 
                        == k){
                        int squareMean = 0;
                        squareMean += (valuePossibiity[i1] - value1) * (valuePossibiity[i1] - value1);
                        squareMean += (valuePossibiity[i2] - value2) * (valuePossibiity[i2] - value2);
                        squareMean += (valuePossibiity[i3] - value3) * (valuePossibiity[i3] - value3);
                        squareMean += (valuePossibiity[i4] - value4) * (valuePossibiity[i4] - value4);
                        if (squareMean < bestSquareMean || bestSquareMean == -1) {
                            bestValue[0] = valuePossibiity[i1];
                            bestValue[1] = valuePossibiity[i2];
                            bestValue[2] = valuePossibiity[i3];
                            bestValue[3] = valuePossibiity[i4];
                            bestSquareMean = squareMean;
                        }
                    }
                }
                
                RGBDimension[dim][x][y] = (byte)(bestValue[0]);
                RGBDimension[dim][x+1][y] = (byte)(bestValue[1]);
                RGBDimension[dim][x][y+1] = (byte)(bestValue[2]);
                RGBDimension[dim][x+1][y+1] = (byte)(bestValue[3]);
            }
        }
        
        for (int i = 0; i < cover.getHeight(); i++) {
            for (int j = 0; j < cover.getWidth(); j++) {
                int rgb = 0;
                for (int bitOffset = 0; bitOffset != 24; bitOffset += 8) {
                    byte value = RGBDimension[bitOffset/8][i][j];
                    rgb += byteToUnsignedInt(value) << bitOffset;
                }
                cover.setRGB(j, i, rgb);
            }
        }
        
        File stegoFile = new File("D:\\Kuliah\\image\\ac.bmp");
        try {
            stegoFile.delete();
            ImageIO.write(cover, "bmp", stegoFile);
        } catch (IOException ex) {
            System.out.println("Error on creating image file. " + ex);
        }
        
        return stegoFile;
    }
    
    private int commonLSBSubsitution (int offset, int size, int cover, int data) {
        int bitmask = 0;
        for (int i = 0; i < size; i++) bitmask = (bitmask << 1) | 1;
        
        data = data & bitmask;
        cover = cover & (~(bitmask << offset));
        cover = cover | (data << offset);
        return cover;
    }

    private int modifiedLSBSubsitution (int size, int cover, int data) {
        
        int step = 1<<(size);
        int triger = (1<<(size-1));
        int newCover = commonLSBSubsitution(0, size, cover, data);
        int newCover2;
        if (Math.abs(cover - newCover) >=  triger) {
            if (newCover>cover) newCover2 = newCover - step;
            else newCover2 = newCover + step;
            if (newCover2 >= 0 && newCover2 <= 255) newCover = newCover2;
        }
        return newCover;
    }
    
    public int byteToUnsignedInt (byte n) {
        int a = n & 0xFF;
        return a;
    }
}
