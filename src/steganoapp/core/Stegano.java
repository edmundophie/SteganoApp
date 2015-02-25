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
public interface Stegano {
    void setCoverObject(File image);
    void setKey(String key);
    void setMessage(File messageFile);
    int getMaxMsgSize();
    File getSteganoObject();
}
