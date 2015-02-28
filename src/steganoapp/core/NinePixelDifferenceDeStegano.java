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
    
    @Override
    public void setCoverObject(File cover) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setKey(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMsgSize(int size) {
        this.size = size;
    }

    @Override
    public File deSteganoObject() {
        msgTemp = new StringBuffer();
        int iter = 0;
        int koorX=0;
        boolean lanjut = true;
        
            while(koorX<X.length && lanjut) {
                int koorY=0;
                while(koorY<X[0].length && lanjut) {
                    if(koorX!=koorX+3 || koorY!=koorY+3) {
                        int color=0;
                        while(color<3 && lanjut) {
                            int group = getGroup(koorX, koorY, color);
                            getBitMsg(koorX, koorY, color, group);
                            //get
                            color++;
                            iter++;
                            if(iter>size) lanjut = false;
                        }
                    }
                    koorY++;
                }
                koorX++;
            }
            
        msgInto8Bit();
        
        File output = new File("D:\\output.txt");
        try {
            output.delete();
            FileOutputStream outputStream = new FileOutputStream(output);
            outputStream.write(msg.toString().getBytes());
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
        
        for(int k=0; k<3; k++) {
            d = X[k][x+3][y+3] & 3;
        }
        return d;
    }
     
    public void getBitMsg(int x, int y, int color, int group) {
        int nLSB = nSubsitutionLSB(group); 
        for(int i=x; i <x+3; i++) {
            for(int j=y; j<y+3; j++) {
                if(i==x+3 && j==y+3) {
                    //TODO ganti kalau stegano diperbaik untuk kasus pixel 9
                }
                else {
                    int bitmask = 0;
                    for (int i1 = 0; i1 < nLSB; i1++) bitmask = (bitmask << 1) + 1;
                    int message = X[color][i][j] & bitmask;
                    for (int i1 = nLSB - 1; i1 >= 0; i1--)
                        if (((message >> i1) & 1) == 0) msgTemp.append('1');
                        else msgTemp.append('0');
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
        if(msgTemp.length()%8 != 0) {
            int iter = 8-(msgTemp.length()%8);
            for(int i=0; i<iter; i++) {
                msgTemp.insert(0, "0");
            }
        }
        while(msgTemp.length() > 8) {
            String karakterString = msgTemp.substring(0, 7);
            int karakterInt = Integer.parseInt(karakterString, 2);
            char karakter = (char) karakterInt;
            msg.append(karakter);
            msgTemp.delete(0, 7);
        }
        // msgTemp.length < 8
    } 
}
