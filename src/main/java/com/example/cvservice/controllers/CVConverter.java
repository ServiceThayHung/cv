package com.example.cvservice.controllers;

import com.example.cvservice.models.ConvertResult;
import com.example.cvservice.models.Metadata;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.example.cvservice.storage.StorageService;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/cv")
public class CVConverter {
    @Autowired
    private StorageService storageService;
    
    @GetMapping("/gettextscan/{id}")
    public String convert(@PathVariable int id) {
        String fileName = String.format("%s.%s", id + "", "pdf");

        return convertPDFtoText(storageService.load(fileName).toFile());
    }
    
    @GetMapping("/getmetadata/{id}")
    public Metadata extractMetadata(@PathVariable int id) {
        Metadata metadata = new Metadata();

        String fileName = String.format("%s.%s", id + "", "pdf");

        String content = convertPDFtoText(storageService.load(fileName).toFile());

        // find email with regex
        String regex = "[a-z0-9!#$%&'*+/=?^_`\\{|\\}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`\\{|\\}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        System.out.println(content);

        if(matcher.find()) {
            metadata.email = content.substring(matcher.start(), matcher.end());        
        }
    
        return metadata;
    }

    @GetMapping("/getall/{id}")
    public ConvertResult extractCV(@PathVariable int id) {
        ConvertResult res = new ConvertResult();

        Metadata metadata = new Metadata();

        String fileName = String.format("%s.%s", id + "", "pdf");

        String content = convertPDFtoText(storageService.load(fileName).toFile());

        // find email with regex
        String regex = "[a-z0-9!#$%&'*+/=?^_`\\{|\\}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`\\{|\\}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        System.out.println(content);

        if(matcher.find()) {
            metadata.email = content.substring(matcher.start(), matcher.end());        
        }
    
        res.content = content;
        res.metadata = metadata;

        return res;
    }

    private String convertPDFtoText(final File file) {
        if(file == null || file.length() == 0) return "";
        String parsedText = "";
        
        try(PDDocument document = PDDocument.load(file)) {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            pdfTextStripper.setStartPage(1);
            pdfTextStripper.setEndPage(5);
            parsedText = pdfTextStripper.getText(document);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return parsedText;
    }
}
