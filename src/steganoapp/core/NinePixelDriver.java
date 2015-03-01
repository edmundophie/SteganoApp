/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package steganoapp.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author asus
 */
public class NinePixelDriver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        {
            File test = new File("coba");
            byte[] msgLen = new byte[4];
            for (int i = 0; i < 4; i++){
            msgLen[i] = (byte)((93000 >> (i*8)) & (0xFF));
                System.out.println(msgLen[i]);
            }
            FileOutputStream output = new FileOutputStream(test);
            output.write(msgLen);
            output.close();
            File image = new File("D:\\Kuliah\\image\\coba.bmp");
            NinePixelDifferenceStegano st = new NinePixelDifferenceStegano();
            st.setCoverObject(image);
            st.setKey("a");
            st.setMessage(test);
            st.getSteganoObject();
        }
        {
            File image = new File("stegObj");
            NinePixelDifferenceDeStegano st = new NinePixelDifferenceDeStegano();
            st.setStegoObject(image);
            st.setKey("a");
            st.setMsgSize(4);
            try {
                byte[] msgLen = Files.readAllBytes(st.deSteganoObject().toPath());
                int msgLength = 0;
                for (int i = 0; i < 4; i++){
                msgLength += ((msgLen[i] & 0xFF) << (i * 8));System.out.println(msgLen[i]);
                }
                System.out.println(msgLength);
            } catch (IOException ex) {
                Logger.getLogger(NinePixelDriver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
