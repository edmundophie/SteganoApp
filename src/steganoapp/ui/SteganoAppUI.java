/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganoapp.ui;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import steganoapp.core.DeStegano;
import steganoapp.core.FourPixelDiffrenceDeStegano;
import steganoapp.core.FourPixelDiffrenceStegano;
import steganoapp.core.NinePixelDifferenceDeStegano;
import steganoapp.core.NinePixelDifferenceStegano;
import steganoapp.core.PSNR;
import steganoapp.core.StandardLSB;
import steganoapp.core.StandardLSBDeStegano;
import steganoapp.core.Stegano;
import steganoapp.core.vigenereChiperExtended;

/**
 *
 * @author edmundophie
 */
public class SteganoAppUI extends javax.swing.JFrame {

    private Stegano st;
    
    private DeStegano dst;
    private File extractedMessage;
    /**
     * Creates new form SteganoAppUI
     */
    public SteganoAppUI() {
        initComponents();
        jButton6.setVisible(false);
        st = new StandardLSB();
        dst = new StandardLSBDeStegano();
        extractedMessage = new File("exMessage");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jFileChooser2 = new javax.swing.JFileChooser();
        jFileChooser3 = new javax.swing.JFileChooser();
        jFileChooser4 = new javax.swing.JFileChooser();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SteganoApp");

        jLabel1.setText("Embedded message");

        jButton2.setText("Encrypt & Embed");
        jButton2.setToolTipText("");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setText("Cover image");

        jLabel3.setText("Stegokey");

        jButton1.setText("Browse...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel9.setText("Metode");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Standard", "4-Pixel Differencing", "9-Pixel Differencing" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton5.setText("Browse...");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jTextArea1.setBackground(new java.awt.Color(55, 55, 55));
        jTextArea1.setColumns(20);
        jTextArea1.setForeground(new java.awt.Color(204, 204, 204));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("\n\n");
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5))
                            .addComponent(jTextField1)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jButton5)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jButton1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Embed", jPanel1);

        jLabel5.setText("Stego object");

        jLabel6.setText("Stegokey");

        jLabel7.setText("Extracted file");

        jButton3.setText("Browse...");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Extract message");
        jButton4.setToolTipText("");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Standard", "4-Pixel Differencing", "9-Pixel Differencing" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel10.setText("Metode");

        jButton6.setText("Simpan...");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jTextArea2.setEditable(false);
        jTextArea2.setBackground(new java.awt.Color(55, 55, 55));
        jTextArea2.setColumns(20);
        jTextArea2.setForeground(new java.awt.Color(204, 204, 204));
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(5);
        jTextArea2.setText("\n");
        jScrollPane2.setViewportView(jTextArea2);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel10)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addGap(55, 55, 55)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jButton3))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.LEADING, 0, 306, Short.MAX_VALUE))
                            .addComponent(jButton6))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3)
                            .addComponent(jLabel14)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6))))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Extract", jPanel2);

        jLabel11.setFont(new java.awt.Font("Phosphate", 0, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 0, 0));
        jLabel11.setText("STEGANOAPP");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 646, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(253, 253, 253)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        File messageFile = jFileChooser1.getSelectedFile();
        File coverImgFile = jFileChooser2.getSelectedFile();
        String stegoKey = jTextField1.getText();
        long maxMessageSize = (coverImgFile!=null)?coverImgFile.length()/8:0;
        
        // Empty field
        if(messageFile==null || coverImgFile==null || stegoKey.isEmpty())
            jTextArea1.setText("Some field is not set yet.");
        else if(messageFile.length()>maxMessageSize) {// Message File too big
            jTextArea1.setText("Error: Message file is  too big.\n");
            jTextArea1.append("Message size : " + messageFile.length() + " byte\n");
            jTextArea1.append("Cover size : " + coverImgFile.length() + " byte\n");
            jTextArea1.append("Max. message size : " + maxMessageSize + " byte\n");
        }    
        else {
            File encryptedMessage = new File("EncryptedMessage");
            try {
                encryptedMessage.delete();
                byte[] encrypted = Encrypt(Files.readAllBytes(messageFile.toPath()),stegoKey);
                byte[] msgLen = new byte[4];
                System.out.println(encrypted.length);
                for (int i = 0; i < 4; i++){
                msgLen[i] = (byte)((encrypted.length >> (i*8)) & (0xFF));
                }
                byte[] composite = new byte[msgLen.length + encrypted.length];
                System.arraycopy(msgLen, 0, composite, 0, msgLen.length);
                System.arraycopy(encrypted, 0, composite, msgLen.length, encrypted.length);
                FileOutputStream output = new FileOutputStream(encryptedMessage);
                output.write(composite);
                output.close();
                for (int i = 0; i < 4; i++) System.out.println(composite[i]);
            } catch (IOException ex) {
                Logger.getLogger(SteganoAppUI.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
                return;
            }
            // Standard LSB
            st.setCoverObject(coverImgFile);
            st.setMessage(encryptedMessage);
            st.setKey(stegoKey);
            File stegoImgFile = st.getSteganoObject();
            
            // Tampilkan gambar di window baru
            try {
                CoverImageWindow coverWindow = new CoverImageWindow(coverImgFile);
                StegoImageWindow stegoWindow = new StegoImageWindow(stegoImgFile);
            } catch (IOException ex) {
                System.out.println("Couldn't load image. " + ex);
            }
            // Hitung PSNR
            PSNR psnr = new PSNR();
            psnr.setImage(coverImgFile, stegoImgFile);
            jTextArea1.setText(("Message embedded success!\n"));
            jTextArea1.append(("-------------------------\n"));
            try {
                jTextArea1.append("PSNR : " + psnr.getPSNR());
            } catch (IOException ex) {
                System.out.println("Failed to load image file. " + ex);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        if(jComboBox1.getSelectedItem().toString().equalsIgnoreCase("Standard")) {
            st = new StandardLSB();
        } 
        else if(jComboBox1.getSelectedItem().toString().equalsIgnoreCase("4-Pixel Differencing")) {
            // 4-Pixel Differencing  
            st = new FourPixelDiffrenceStegano();
        }
        else {
            st = new NinePixelDifferenceStegano();
            // 9-Pixel Differencing
        }
        File messageFile = jFileChooser1.getSelectedFile();
        File coverFile = jFileChooser2.getSelectedFile();
        jTextArea1.setText("");

        // Check file size
        if (messageFile != null) 
            jTextArea1.append("Message size : " + messageFile.length() + " byte\n");
        if(coverFile!=null) {
            long maxMessageSize = coverFile.length()/8;
            jTextArea1.append("Cover size : " + coverFile.length() + " byte\n");
            st.setCoverObject(coverFile);
            jTextArea1.append("Max. message size : " + st.getMaxMsgSize() + " byte\n");
            if(messageFile != null)
            if (messageFile.length()>maxMessageSize)
                jTextArea1.append("Error: Message file is too big.");
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        if(jComboBox2.getSelectedItem().toString().equalsIgnoreCase("Standard")) {
            dst = new StandardLSBDeStegano();
        } 
        else if(jComboBox2.getSelectedItem().toString().equalsIgnoreCase("4-Pixel Differencing")) {
            // 4-Pixel Differencing  
            dst = new FourPixelDiffrenceDeStegano();
        }
        else {
            // 9-Pixel Differencing
            dst = new NinePixelDifferenceDeStegano();
        }           
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String stegoKey = jTextField3.getText();
        File stegoFile = jFileChooser3.getSelectedFile();
        System.out.println(stegoFile.getName());
        dst.setMsgSize(4);
        dst.setKey(stegoKey);
        dst.setStegoObject(stegoFile);
        byte[] msgLen;
        try {
        msgLen = Files.readAllBytes(dst.deSteganoObject().toPath());
        }
        catch (IOException ex) {
        Logger.getLogger(SteganoAppUI.class.getName()).log(Level.SEVERE, null, ex);
        return;
        }
        int msgLength = 0;
        for (int i = 0; i < 4; i++){
        msgLength += ((msgLen[i] & 0xFF) << (i * 8));
        }
        System.out.println(msgLength);
        dst.setStegoObject(stegoFile);
        dst.setKey(stegoKey);
        dst.setMsgSize(4+ msgLength);
        File compositeMessage = dst.deSteganoObject();
        File encryptedMessage = new File("encMess");
        try{
        encryptedMessage.delete();
        FileOutputStream out = new FileOutputStream(encryptedMessage);
        byte[] compositeData = Files.readAllBytes(compositeMessage.toPath());
        byte[] encryptedData = new byte[compositeData.length - 4];
        for (int i = 0; i < encryptedData.length; i++)
        encryptedData[i] = compositeData[i+4];
        out.write(encryptedData);
        out.close();
        }
        catch (Exception ex) {
        Logger.getLogger(SteganoAppUI.class.getName()).log(Level.SEVERE, null, ex);
        jTextArea2.setText("Pesan tidak berhasil diextrak");
        return;
        }
        {
        try {
        FileOutputStream output = new FileOutputStream(extractedMessage);
        output.write(Decrypt(Files.readAllBytes(encryptedMessage.toPath()), stegoKey));
        } catch (FileNotFoundException ex) {
        Logger.getLogger(SteganoAppUI.class.getName()).log(Level.SEVERE, null, ex);
        jTextArea2.setText("Pesan tidak berhasil diekstrak");
        return;
        } catch (IOException ex) {
        Logger.getLogger(SteganoAppUI.class.getName()).log(Level.SEVERE, null, ex);
        jTextArea2.setText("Pesan tidak berhasil diekstrak");
        return;
        }
        }
        jTextArea2.setText("Pesan berhasil diekstrak");
        jButton6.setVisible(true);
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int returnVal = jFileChooser4.showSaveDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                // Dapatkan lokasi save
                String path = jFileChooser4.getSelectedFile().getAbsolutePath();
                File messageFile = new File(path);
                
                // Baca semua byte stegoImage
                byte[] data = StandardLSB.convertFile2Bytes(extractedMessage);
                
                // Tulis semua byte stegoImage ke sebuah file di lokasi save
                FileOutputStream out;
                out = new FileOutputStream(messageFile);
                out.write(data);
                out.close();
            } catch (IOException ex) {
                System.out.println("Error processing file." + ex);
            }
        } else {
            System.out.println("File access cancelled by user.");
        }        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int returnVal = jFileChooser3.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser3.getSelectedFile();
            jLabel14.setText(file.getName());
        } else {
            System.out.println("File access cancelled by user.");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File messageFile = jFileChooser1.getSelectedFile();
            File coverFile = jFileChooser2.getSelectedFile();
            
            jLabel8.setText(messageFile.getName());
            // Check file size
            jTextArea1.setText(null);
            jTextArea1.append("Message size : " + messageFile.length() + " byte\n");
            if(coverFile!=null) {
                long maxMessageSize = coverFile.length()/8;
                jTextArea1.append("Cover size : " + coverFile.length() + " byte\n");
                jTextArea1.append("Max. message size : " + st.getMaxMsgSize() + " byte\n");
                if(messageFile.length()>maxMessageSize)
                    jTextArea1.append("Error: Message file is too big.");
            }
        } else {
            System.out.println("File access cancelled by user.");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int returnVal = jFileChooser2.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File messageFile = jFileChooser1.getSelectedFile();
            File coverFile = jFileChooser2.getSelectedFile();
            jLabel4.setText(coverFile.getName());
            st.setCoverObject(coverFile);
            // Check file size
            long maxMessageSize = coverFile.length()/8;
            jTextArea1.setText(null);
                        
            jTextArea1.append("Cover size : " + coverFile.length() + " byte\n");
            jTextArea1.append("Max. message size : " + st.getMaxMsgSize() + " byte\n");
            
            if(messageFile!=null) {
                jTextArea1.insert("Message size : " + messageFile.length() + " byte\n", 0);
                if(messageFile.length()>maxMessageSize)
                    jTextArea1.append("Error: Message file is too big.");
            }
        } else {
            System.out.println("File access cancelled by user.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SteganoAppUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SteganoAppUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SteganoAppUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SteganoAppUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SteganoAppUI().setVisible(true);
            }
        });
    }
    
    public static byte[] Encrypt (byte[] plaintext, String key) {  
        StringBuilder sb = new StringBuilder();
        for (byte b : plaintext) {
            sb.append((char)b);
        }

        vigenereChiperExtended chpr = new vigenereChiperExtended();
        chpr.isAutoKey = false;
        chpr.key = key;

        String plainTextString = sb.toString();
        String chiperTextString = chpr.encrypt(plainTextString);
        sb = new StringBuilder(chiperTextString);
        byte r[] = new byte [sb.length()];
        for (int i = 0; i < sb.length(); i++) {
            r[i] = (byte) ((int)sb.charAt(i));
        }
        return r;
    }
    
    public static byte[] Decrypt (byte[] chipertext, String key) {  
        StringBuilder sb = new StringBuilder();
        for (byte b : chipertext) {
            sb.append((char)b);
        }

        vigenereChiperExtended chpr = new vigenereChiperExtended();
        chpr.isAutoKey = false;
        chpr.key = key;

        String chiperTextString = sb.toString();
        String plainTextString = chpr.decrypt(chiperTextString);
        sb = new StringBuilder(plainTextString);
        byte r[] = new byte [sb.length()];
        for (int i = 0; i < sb.length(); i++) {
            r[i] = (byte) ((int)sb.charAt(i));
        }
        return r;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JFileChooser jFileChooser2;
    private javax.swing.JFileChooser jFileChooser3;
    private javax.swing.JFileChooser jFileChooser4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
