/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package steganoapp.core;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author asus
 */
public class NinePixelDifferenceStegano implements Stegano {
    private int X[][][];
    private BufferedImage cover;
    
    @Override
    public void setCoverObject(File image) {
        try {
            cover = ImageIO.read(image);
        } catch (IOException ex) {
            Logger.getLogger(NinePixelDifferenceStegano.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setKey(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMessage(byte[] message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getMaxMsgSize() {
        int sum = 0;
        for(int i=0; i<cover.getWidth(); i+=3) {
            for(int j=0; j<cover.getHeight(); j+=3) {
                int d = PDV(0,i,j);
                int nLSB = nSubsitutionLSB(group(d));
                sum += (9 * nLSB -2 );
            }
        }
        return sum;
    }

    @Override
    public File steganoObject() {
        File new_f = null;
        File f = new File("D:\\SteganoApp\\src\\steganoapp\\ui\\lena.png");
        setCoverObject(f);
        X = new int[3][cover.getWidth()][cover.getHeight()];
        for(int i=0; i<X[0].length; i++) {
            for(int j=0; j<X[0][0].length; j++) {
                Color c = new Color(cover.getRGB(i, j));
                X[0][i][j] = c.getRed();
                X[1][i][j] = c.getGreen();
                X[2][i][j] = c.getBlue();
            }
        }
        
        //hitung PVD
        int[] d = new int[3];
        d[0] = PDV(0, 0,0);
        d[1] = PDV(1, 0,0);
        d[2] = PDV(2, 0,0);
        
        //pengelompokkan subsitusi bit
        int[] bit = new int[3];
        bit[0] = nSubsitutionLSB(group(d[0]));
        bit[1] = nSubsitutionLSB(group(d[1]));
        bit[2] = nSubsitutionLSB(group(d[2]));
        
        //menyisipikan kelompok di bit 7th dan 8th
        X[0][0+3][0+3] = X[0][0+3][0+3] >> 2;
        X[0][0+3][0+3] = X[0][0+3][0+3] << 2;
        X[0][0+3][0+3] = X[0][0+3][0+3] + group(d[0]);
        
        //tes pesan
        String msg = "Haii";
        String binaryMsg = stringToBinary(msg);
        
        for(int i=0; i<3; i++) {
            msg = msgSubsitutionN(i, 0, 0, binaryMsg, bit[i]);
        }
        return new_f;
    }
    
    public BufferedImage getImage(String f) throws IOException {
        BufferedImage image;
        File file = new File(f);
        image = ImageIO.read(file);
        return image;
    }
    
    public int PDV (int color, int koorX, int koorY) {
        int result;
        int sum = 0;
        int Xmin = XMin(color, koorX, koorY);
        boolean found = false;
        for(int j=koorY; j < koorY+3; j++) {
            for (int i=koorX; i < koorX+3; i++) {
                if(X[color][i][j] == Xmin && !found)
                    found = true;
                else 
                    sum += (X[color][i][j] - Xmin);
            }
        }
        result = sum/8;
        return result;
    }
    
    public int XMin (int color, int koorX, int koorY) {
        int Xmin = X[color][koorX][koorY];
        for(int i=koorX; i < koorX+3; i++) {
            for(int j=koorY; j < koorY+3; j++) {
                if(X[color][i][j] < Xmin)
                    Xmin = X[color][i][j];
            }
        }
        return Xmin;
    }
    
    public int group(int d) {
        int kelompok;
        if(d <= 7)
            kelompok = 0;   //lower
        else if(d>=8 && d<=15)
            kelompok = 1;   //lower-middle
        else if(d>=16 && d<=31)
            kelompok = 2;   //higher-middle
        else
            kelompok = 3;   //higher
        return kelompok;
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
    
    public String msgSubsitutionN(int color, int koorX, int koorY, String msgBinary, int nLSB) {
        String sisa = msgBinary;
        int i = koorX;
        boolean habis = false;
        while((i<=koorX+3) && !habis) {
            int j = koorY;
            while((j<=koorY+3) && !habis) {
                String sub = msgBinary.substring(0, nLSB);
                int msgBit = Integer.parseInt(sub, 2);
                msgBit = msgBit << 2;
                String subX = Integer.toBinaryString(X[color][i][j]);
                if(nLSB == 2) {
                    subX = subX.substring(subX.length()-2, subX.length());
                    subX = subX + "00";
                } else if(nLSB == 3) {
                    subX = subX.substring(subX.length()-3, subX.length());
                    subX = subX + "00";
                } else if(nLSB == 4) {
                    subX = subX.substring(subX.length()-4, subX.length());
                    subX = subX + "00";
                } else {
                    subX = subX.substring(subX.length()-5, subX.length());
                    subX = subX + "00";
                }
                
                //insert bit
                int subXInt = Integer.parseInt(subX, 2);
                X[color][i][j] -= subXInt;
                X[color][i][j] += msgBit;
                
                sisa = msgBinary.substring(nLSB);
                if(sisa.length() < nLSB)
                    habis = true;
                j++;
            }
            i++;
        }
        return sisa;
    }
    
    public static String stringToBinary(String str) {
        byte[] bytes = str.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes)
        {
           if((int)b < 127)
                binary.append("0");
           
            binary.append(Integer.toBinaryString((int) b));
//            binary.append(" ");
        }
        return binary.toString();        
    }
    
}
