/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganoapp.core;

import java.io.File;
import java.util.Random;

/**
 *
 * @author calvin-pc
 */
public class FourPixelDriver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        {
            File image = new File("D:\\Kuliah\\image\\coba.bmp");
            FourPixelDiffrenceStegano st = new FourPixelDiffrenceStegano();
            st.setCoverObject(image);
            st.setKey("Willojklads");
            st.setMessage(new File("D:\\Kuliah\\image\\data store.txt"));
            System.out.println(st.getMaxMsgSize());
            st.getSteganoObject();
        }
        {
            File image = new File("D:\\Kuliah\\image\\ac.bmp");
            FourPixelDiffrenceDeStegano st = new FourPixelDiffrenceDeStegano();
            st.setStegoObject(image);
            st.setKey("Willojklads");
            st.setMsgSize(10000);
            st.deSteganoObject();
        }
    }
    
}
