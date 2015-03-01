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
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author asus
 */
public class NinePixelDifferenceStegano implements Stegano {
    private int X[][][];
    private int Xori[][][];
    private BufferedImage cover;
    private String msg;
    private File coverFile;
    private String key;
    
    @Override
    public void setCoverObject(File image) {
        try {
            coverFile = image;
            cover = ImageIO.read(image);
             X = new int[3][cover.getWidth()][cover.getHeight()];
             Xori = new int[3][cover.getWidth()][cover.getHeight()];
        for(int i=0; i<X[0].length; i++) {
            for(int j=0; j<X[0][0].length; j++) {
                Color c = new Color(cover.getRGB(i, j));
                X[0][i][j] = c.getRed();
                Xori[0][i][j] = c.getRed();
                X[1][i][j] = c.getGreen();
                Xori[1][i][j] = c.getGreen();
                X[2][i][j] = c.getBlue();
                Xori[2][i][j] = c.getBlue();
            }
        }
        } catch (IOException ex) {
            Logger.getLogger(NinePixelDifferenceStegano.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setKey(String key) {
        this.key = key;
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
        File new_f = new File("stegObj");
        StringBuffer binaryMsg = new StringBuffer(stringToBinary(msg));
//        
        int iterX = 0;
        int[] d = new int[3];
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
        while(lanjut) {
        int rand = randomStack.pop();
        iterX = (rand / (X[0][0].length/3)) * 3;
        int iterY = (rand % (X[0][0].length/3)) * 3;
                int color = 0;
                while(color < 3 && lanjut) {
                //hitung PVD
            
            d[color] = PDV(color, iterX,iterY);
            
//            //pengelompokkan subsitusi bit
            int[] bit = new int[3];
            bit[color] = nSubsitutionLSB(group(d[color]));
//
//            //menyisipikan kelompok di bit 7th dan 8th
            X[color][iterX+2][iterY+2] >>= 2;
            X[color][iterX+2][iterY+2] <<= 2;
            X[color][iterX+2][iterY+2] += group(d[color]);
//
            String blockMsg = null;
            if (binaryMsg.length() < bit[color]*8) {
                blockMsg = binaryMsg.substring(0, binaryMsg.length());
                binaryMsg.delete(0, binaryMsg.length());
            }
            else {
                blockMsg = binaryMsg.substring(0, bit[color] * 8);
                binaryMsg.delete(0, bit[color] * 8);
            }
            msgSubsitutionN(iterX, iterY, color, blockMsg, bit[color]);
            adjustment(color, iterX, iterY, bit[color]);
            
            if(binaryMsg.length() == 0) lanjut = false;
            color++;
                }
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
            new_f.delete();
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
    
    public void msgSubsitutionN(int koorX, int koorY, int color, String msgBinary, int nLSB) {
        StringBuffer sisa = new StringBuffer(msgBinary);
        int i = koorX;
        boolean habis = false;
        while((i<koorX+3) && !habis) {
            int j = koorY;
            while((j<koorY+3) && !habis) {
                if(i!=koorX+2 || j!=koorY+2) {
                    String sub = null;
                    if (nLSB > sisa.length()) {
                        sub = sisa.substring(0, sisa.length());
                        for (int k = sub.length(); k < nLSB; k++)
                            sub = sub + "0";
                        sisa.delete(0, sisa.length());
                    }
                    else {
                        sub = sisa.substring(0, nLSB);
                        sisa.delete(0, nLSB);
                    }
                    if (sisa.length() == 0) {
                        habis = true;
                    }
                    
                    int msgBit = Integer.parseInt(sub, 2); 
                    if(nLSB == 1) {
                        X[color][i][j] &= 0xFE; 
                        X[color][i][j] += msgBit;
                    } else if(nLSB == 2) {
                        X[color][i][j] &= 0xFC; 
                        X[color][i][j] += msgBit;
                    } else if(nLSB == 3) {
                        X[color][i][j] &= 0xF8;  
                        X[color][i][j] += msgBit;
                    } else if(nLSB == 4) {
                        X[color][i][j] &= 0xF0;  
                        X[color][i][j] += msgBit;
                    } else {
                        X[color][i][j] &= 0xE0;  
                        X[color][i][j] += msgBit;
                    }
                    
                }
                j++;
            }
            i++;
        }
    }
    
    public static String stringToBinary(String str) {
        StringBuilder binary = new StringBuilder();
        for (char b : str.toCharArray())
        {
            binary.append(String.format("%8s", Integer.toBinaryString((byte)b & 0xFF)).replace(' ', '0'));
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
    
    public void adjustment (int color, int x, int y, int nLSB) {
        int i = x;
        while(i < x+3) {
            int j = y;
            while(j < y+3) {
                if(X[color][i][j] >= (Xori[color][i][j] + (int) Math.pow(2, (nLSB-1)) + 1)) {
                    X[color][i][j] -= (int) Math.pow(2, nLSB);
                } else if(X[color][i][j] <= (Xori[color][i][j] - (int) Math.pow(2, (nLSB-1)) + 1)) {
                    X[color][i][j] += (int) Math.pow(2, nLSB);
                } else {
                    // nilai x[color][x][y] tidak perlu di adjustment
                }
                
                // cek boundary {0, 255}
                if(X[color][i][j] < 0) {
                    X[color][i][j] += (int) Math.pow(2, nLSB);
                } else if(X[color][i][j] > 255) {
                    X[color][i][j] -= (int) Math.pow(2, nLSB);
                }
                
                j++;
            }
            i++;
        }
    }
    
}
