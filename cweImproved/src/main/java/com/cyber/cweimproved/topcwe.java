package com.cyber.cweimproved;

import java.io.*;

public class topcwe {

    private void cwe502(){
        try {
            File file = new File("object.obj");
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            javax.swing.JButton button = (javax.swing.JButton) in.readObject();
            in.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

//    private final Object readObject(ObjectInputStream in) throws java.io.IOException {
//        throw new java.io.IOException("Cannot be deserialized"); }
}
