package com.sv.changefilenames;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        Test t = new Test();
        t.x();
    }

    private void x() {
        try (FileInputStream fis = getY()) {
            byte[] b = new byte[fis.available()];
            int bc = fis.read(b);
            System.out.println("bc...." + bc);
            if (bc > 0)
                System.out.println("["+new String(b)+"]");
            else
                System.out.println("000["+new String(new byte[0])+"]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FileInputStream getY() throws IOException {
        return new FileInputStream(new File("c:\\sv\\sv1.txt"));
    }
}
