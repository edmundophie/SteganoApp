/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package steganoapp.core;

import java.io.File;

/**
 *
 * @author asus
 */
public class NinePixelDriver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        {
            File image = new File("D:\\Kuliah\\image\\coba.bmp");
            NinePixelDifferenceStegano st = new NinePixelDifferenceStegano();
            st.setCoverObject(image);
            st.setKey("WillojkladsJOl");
            st.setMessage(new File("D:\\Kuliah\\image\\data store.txt"));
            st.getSteganoObject();
        }
        {
            File image = new File("stegObj");
            NinePixelDifferenceDeStegano st = new NinePixelDifferenceDeStegano();
            st.setStegoObject(image);
            st.setKey("WillojkladsJOl");
            st.setMsgSize(1267);
            st.deSteganoObject();
        }
    }
    
}
