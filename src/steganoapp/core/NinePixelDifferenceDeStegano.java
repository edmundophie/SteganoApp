/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package steganoapp.core;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author asus
 */
public class NinePixelDifferenceDeStegano implements DeStegano {
    int size;
    int X[][][];
    BufferedImage cover;
    StringBuffer msgTemp;
    StringBuffer msg;
    String key;
    
    @Override
    public void setCoverObject(File cover) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void setMsgSize(int size) {
        this.size = size;
    }

    @Override
    public File deSteganoObject() {
        msgTemp = new StringBuffer();
        int koorX=0;
        boolean lanjut = true;
        Stack<Integer> randomStack = null;
        
        {
            int seed = 0;
            for(int i=0;i<key.length();++i)
                seed += (int)key.charAt(i);
            Random rand = new Random(seed);
            
            int randomList[] = new int [(X[0].length/3) * (X[0][0].length/3)];
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
        }
        /*while(lanjut) {
        int rand = randomStack.pop();
        koorX = (rand / (X[0][0].length/3)) * 3;
        int koorY = (rand % (X[0][0].length/3)) * 3;*/
        for (koorX = 0; lanjut; koorX+=3)
            for (int koorY = 0; koorY+2 < X[0][0].length && lanjut; koorY+=3) {
                        int color=0;
                        while(color<3 && lanjut) {
                            int group = getGroup(koorX, koorY, color);
                            getBitMsg(koorX, koorY, color, group);
                            //get
                            color++;
                            if(msgTemp.length()/8 >= size) lanjut = false;
                        }
            }
        msgInto8Bit();
        
        File output = new File("deStegoMessage");
        try {
            byte[] data = new byte [msg.toString().toCharArray().length];
            for (int i = 0 ; i < data.length; i++) data[i] = (byte)msg.toString().toCharArray()[i]; 
            output.delete();
            FileOutputStream outputStream = new FileOutputStream(output);
            outputStream.write(data);
            outputStream.close();
        } catch (IOException ex) {
            System.out.println("Error on creating image file. " + ex);
        }
        return output;
    }

    @Override
    public void setStegoObject(File image) {
        try {
            cover = ImageIO.read(image);
            
            X = new int[3][cover.getWidth()][cover.getHeight()];
           for(int i=0; i<X[0].length; i++) {
               for(int j=0; j<X[0][0].length; j++) {
                   Color c = new Color(cover.getRGB(i, j));
                   X[0][i][j] = c.getRed();
                   X[1][i][j] = c.getGreen();
                   X[2][i][j] = c.getBlue();
               }
           }
           
        } catch (IOException ex) {
            Logger.getLogger(NinePixelDifferenceDeStegano.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getGroup(int x, int y, int color) {
        int d = 0;
        
        d = X[color][x+2][y+2] & 3;
        
        return d;
    }
     
    public void getBitMsg(int x, int y, int color, int group) {
        int nLSB = nSubsitutionLSB(group); 
        for(int i=x; i <x+3; i++) {
            for(int j=y; j<y+3; j++) {
                if(i==x+2 && j==y+2) {
                    //TODO ganti kalau stegano diperbaik untuk kasus pixel 9
                }
                else {
                    int bitmask = 0;
                    if (nLSB == 1) bitmask = 0x01;
                    else if (nLSB == 2) bitmask = 3;
                    else if (nLSB == 3) bitmask = 7;
                    else if (nLSB == 4) bitmask = 15;
                    else if (nLSB == 5) bitmask = 31;
                    int message = X[color][i][j] & bitmask;
                    for (int i1 = nLSB - 1; i1 >= 0; i1--)
                        if (((message >> i1) & 1) == 0) msgTemp.append('0');
                        else msgTemp.append('1');
                }
            }
        }
    }
    
    public int nSubsitutionLSB(int group) {
        int bit;
        if(group == 0)
            bit = 2;
        else if(group == 1)
            bit = 3;
        else if(group == 2)
            bit = 4;
        else
            bit = 5;
        return bit;
    }
    
    public void msgInto8Bit() {
        msg = new StringBuffer();
        int msgLeft = size;
        while(msgLeft > 0) {
            String karakterString = msgTemp.substring(0, 8);
            int karakterInt = Integer.parseInt(karakterString, 2);
            char karakter = (char) karakterInt;
            msg.append(karakter);
            msgTemp.delete(0, 8);
            msgLeft--;
        }
        // msgTemp.length < 8
    } 
}
