/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganoapp.core;

import java.io.File;

/**
 *
 * @author edmundophie
 */
public interface DeStegano {
       void setCoverObject(File cover);
       void setKey(String key);
       void setMsgSize(int size);
       void deSteganoObject(String filename, String filepath);
}
