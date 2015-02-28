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
import java.nio.file.Files;
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
    private String msg;
    private File coverFile;
    
    @Override
    public void setCoverObject(File image) {
        try {
            coverFile = image;
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
            Logger.getLogger(NinePixelDifferenceStegano.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setKey(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getMaxMsgSize() {
        int sum = 0;
        int sisaX = cover.getWidth() % 3;
        int sisaY = cover.getHeight() % 3;
        for(int i=0; i<cover.getWidth()-sisaX; i+=3) {
            for(int j=0; j<cover.getHeight()-sisaY; j+=3) {
                for(int k=0; k<3; k++) {
                    int d = PDV(k,i,j);
                    int nLSB = nSubsitutionLSB(group(d));
                    sum += (9 * nLSB - 2 );
                }
            }
        }
        return sum;
    }

    @Override
    public File getSteganoObject() {
        File new_f = new File("D://lenaS.bmp");
        String binaryMsg = stringToBinary(msg);
//        
        int iterX = 0;
        int[] d = new int[3];
        boolean lanjut = true;
           
        int sisaPixX = X.length % 3;
        int sisaPixY = X[0].length % 3;
        while((iterX<X.length-sisaPixX) && lanjut) {
            int iterY = 0;
            while((iterY<X[0].length-sisaPixY) && lanjut) {
                int color = 0;
                while(color < 3) {
                //hitung PVD
            
            d[color] = PDV(color, iterX,iterY);
            
//            //pengelompokkan subsitusi bit
            int[] bit = new int[3];
            bit[color] = nSubsitutionLSB(group(d[color]));
//
//            //menyisipikan kelompok di bit 7th dan 8th
            X[color][iterX+3][iterY+3] >>= 2;
            X[color][iterX+3][iterY+3] <<= 2;
            X[color][iterX+3][iterY+3] += group(d[color]);
//
            msg = msgSubsitutionN(0, 0, binaryMsg, bit[color]);
//            if(msg.length() < 5) {
//                lanjut = false;
//            }
            
            if(msg.length() < 2) lanjut = false;
            color++;
                }
                iterY+=3;
            }
            iterX+=3;
        }
  
            BufferedImage new_img = cover;
                for(int i=0; i<cover.getWidth(); i++) {
                    for(int j=0; j<cover.getHeight(); j++) {
                        int r = X[0][i][j];
                        int g = X[1][i][j];
                        int b = X[2][i][j];
                        int col = (r << 16) | (g << 8) | b;
                        new_img.setRGB(i, j, col);
                    }
                }
                
//            }
        try {
            ImageIO.write(new_img,"bmp",new_f);
        } catch (IOException ex) {
            Logger.getLogger(NinePixelDifferenceStegano.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public String msgSubsitutionN(int koorX, int koorY, String msgBinary, int nLSB) {
        String sisa = msgBinary;
        int i = koorX;
        boolean habis = false;
        while((i<=koorX+3) && !habis) {
            int j = koorY;
            while((j<=koorY+3) && !habis) {
                if(koorX!=koorX+3 || koorY!=koorY+3) {
                    String sub = msgBinary.substring(0, nLSB);
                    int msgBit = Integer.parseInt(sub, 2); 
                    int color = 0;
                    while(color < 3) {
                        if(nLSB == 2) {
                            X[color][koorX][koorY] &= 0xFC; 
                            X[color][koorX][koorY] += msgBit;
                        } else if(nLSB == 3) {
                            X[color][koorX][koorY] &= 0xF8;  
                            X[color][koorX][koorY] += msgBit;
                        } else if(nLSB == 4) {
                            X[color][koorX][koorY] &= 0xF0;  
                            X[color][koorX][koorY] += msgBit;
                        } else {
                            X[color][koorX][koorY] &= 0xE0;  
                            X[color][koorX][koorY] += msgBit;
                        }
                        color++;
                        if(sisa.length() < nLSB)
                            habis = true;
                    }
                }
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

    @Override
    public void setMessage(File messageFile) {
        try {
           byte[] messages = Files.readAllBytes(messageFile.toPath());
            StringBuilder sb = new StringBuilder();
            for(byte b : messages) {
                sb.append((char) b);
            } 
            msg = sb.toString();
        } catch (IOException ex) {
            Logger.getLogger(NinePixelDifferenceStegano.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
