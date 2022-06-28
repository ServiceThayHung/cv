/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.cvservice.storage;

import java.io.IOException;

/**
 *
 * @author Admin
 */
public interface CacheService {
    void init();
    void save(int number) throws IOException;
    int read();
}
