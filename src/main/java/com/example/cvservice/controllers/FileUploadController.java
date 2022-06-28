/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.cvservice.controllers;

import com.example.cvservice.models.FileInfo;
import com.example.cvservice.models.Pair;
import com.example.cvservice.models.ResponseMessage;
import com.example.cvservice.storage.CacheService;
import com.example.cvservice.storage.StorageService;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

/**
 *
 * @author Admin
 */
@RestController
public class FileUploadController {
    @Autowired
    private StorageService storageService;
    
    @Autowired
    private CacheService cacheService;
    
    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> listUploadedFiles() throws IOException {
        List<FileInfo> fileInfos = storageService.loadAll().map(
            path -> new FileInfo(
                path.getFileName().toString(), 
                MvcUriComponentsBuilder.fromMethodName(
                        FileUploadController.class,
                        "serveFile", path.getFileName().toString()
                ).build().toUri().toString()
            )
        ).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/files/{id}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable int id) {
        Resource file = storageService.loadAsResource(id + ".pdf");
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload")
    public ResponseMessage handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            int id = cacheService.read() + 1;
            
            storageService.store(file, String.format("%s.%s", id + "", "pdf"));
            
            cacheService.save(id);
            
            return new ResponseMessage("Upload successful", id);
        } catch(FileAlreadyExistsException e) {
            e.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        
        return new ResponseMessage("Upload failed", -1);
    }

    @PostMapping("/uploads")
    public List<ResponseMessage> handleFileUpload(@RequestParam("files") MultipartFile[] files) {
        List<ResponseMessage> res = new ArrayList<>();
        
        for(MultipartFile file : files) {
            try {
                int id = cacheService.read() + 1;
                
                storageService.store(file, String.format("%s.%s", id + "", "pdf"));
                
                cacheService.save(id);
    
                res.add(new ResponseMessage("Upload successful", id));
                
                continue;
            } catch(FileAlreadyExistsException e) {
                e.printStackTrace();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            } 

            res.add(new ResponseMessage("Upload failed", -1));
        }

        return res;
    }

    @DeleteMapping("/deleteAll")
    public void clearStorage() {
        storageService.deleteAll();
        cacheService.delete();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleStorageFileNotFound(Exception exc) {
            return ResponseEntity.notFound().build();
    }
}
