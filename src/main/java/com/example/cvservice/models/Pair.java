package com.example.cvservice.models;

public class Pair<T, K> {
    public T first;
    public K second;

    private Pair(T first, K second) {
        this.first = first;
        this.second = second;
    }

    public static <T, K> Pair<T, K> create(T first, K second) {
        return new Pair<T,K>(first, second);
    }
}
