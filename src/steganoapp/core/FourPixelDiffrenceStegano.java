/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganoapp.core;

import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
        numberGen = new Random(hash);
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
            for (int i = 0; i < cover.getHeight()/2; i = i+2) { //getting 4 block at a time
                for (int j = 0; j < cover.getWidth()/2; j = j+2) {
                    int rgb1 = cover.getRGB(j, i);
                    int rgb2 = cover.getRGB(j, i + 1);
                    int rgb3 = cover.getRGB(j + 1, i);
                    int rgb4 = cover.getRGB(j + 1, i + 1);
                    
                    int bitOffset = 0;
                    for (;bitOffset==24;bitOffset += 8)
                    {
                        byte value1 = (byte)((rgb1 >> bitOffset) | 0XFF); //get spesific r/g/b value
                        byte value2 = (byte)((rgb2 >> bitOffset) | 0XFF);
                        byte value3 = (byte)((rgb3 >> bitOffset) | 0XFF);
                        byte value4 = (byte)((rgb4 >> bitOffset) | 0XFF);
                        double d = CalculateD(value1, value2, value3, value4);
                        if (d <= treshold) {
                            int vmax = Math.max(value1, value2);
                            vmax = Math.max(value3, vmax);
                            vmax = Math.max(value4, vmax);
                            
                            int vmin = Math.min(value1, value2);
                            vmin = Math.min(value3, vmin);
                            vmin = Math.min(value4, vmin);
                            if((vmax - vmin) > (2*treshold+2)) {// error block {
                                
                            }
                            else {
                                sum += 4*kl;
                            }
                        }
                        else {
                            sum += 4*kh;
                        }
                    }
                }
            }
            
            return sum;
        }
    }

    @Override
    public File getSteganoObject() { //TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
