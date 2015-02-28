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
            File image = new File("D:\\lena.bmp");
            NinePixelDifferenceStegano st = new NinePixelDifferenceStegano();
            st.setCoverObject(image);
//            st.setKey("Willojklads");
            st.setMessage(new File("D:\\input.txt"));
            System.out.println(st.getMaxMsgSize());
            st.getSteganoObject();
        }
        {
            File image = new File("D:\\lenaS.bmp");
            NinePixelDifferenceDeStegano st = new NinePixelDifferenceDeStegano();
            st.setStegoObject(image);
            st.setMsgSize(50);
            st.deSteganoObject();
        }
    }
    
}
