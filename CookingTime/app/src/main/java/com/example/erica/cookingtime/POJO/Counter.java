package com.example.erica.cookingtime.POJO;


public class Counter {

    private int counter;

    public Counter(int count){
        counter = count;
    }

    public synchronized void decrement(){
        counter--;
    }

    public int getCounter() {
        return counter;
    }
}
