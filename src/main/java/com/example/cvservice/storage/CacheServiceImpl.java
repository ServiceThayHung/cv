/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.cvservice.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class CacheServiceImpl implements CacheService {
    private final Path root = Paths.get("cache");
     
    @Override
    public void save(int number) throws IOException{
        File file = new File(root.toFile(), "id.txt");
        
        ObjectOutputStream os = new ObjectOutputStream(
                new FileOutputStream(file)
        );
        
        os.writeInt(number);
        os.close();
    }

    @Override
    public int read() {
        File file = new File(root.toFile(), "id.txt");
        
        ObjectInputStream os;
        int id = 0;
        
        try {
            os = new ObjectInputStream(new FileInputStream(file));
            
            id = os.readInt();
            os.close();
        } catch (IOException ex) {
            Logger.getLogger(CacheServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return id;
    }
    
    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
