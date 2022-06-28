package com.example.cvservice.controllers;


import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Progress {
    // public static Observe<Float> getAllProgress = new Observe<>();

    // @Async
    // @GetMapping("/cv/getall_progress")
    // public Future<String> getAllProgress() {
    //     System.out.println("Execute method asynchronously - " + Thread.currentThread().getName());
    //     try {
    //         Thread.sleep(2000);
    //         return new AsyncResult<String>("hello world !!!!");
    //     } catch (InterruptedException e) {
    //         //
    //     }

    //     return null;    
    // }

    // public static class Observe<T> {
    //     private T data;
    //     private Observer observer;

    //     public T getData() {
    //         return data;
    //     }

    //     public void setData(T data) {
    //         this.data = data;
    //         observer.dataChange(data);
    //     }

    //     public void register(Observer observer) {
    //         this.observer = observer;
    //     }
    // }

    // public interface Observer {
    //     void dataChange(Object data);
    // }
}
