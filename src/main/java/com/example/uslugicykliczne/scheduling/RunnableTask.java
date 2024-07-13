package com.example.uslugicykliczne.scheduling;

import com.example.uslugicykliczne.repo.CyclicalServiceRepo;

import java.util.Date;
import java.util.Objects;

public class RunnableTask implements Runnable{
    private String message;
    private final CyclicalServiceRepo cyclicalServiceRepo;
    public RunnableTask(String message, CyclicalServiceRepo cyclicalServiceRepo){
        this.message = message;
        this.cyclicalServiceRepo = cyclicalServiceRepo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RunnableTask that)) return false;
        return Objects.equals(message, that.message) && Objects.equals(cyclicalServiceRepo, that.cyclicalServiceRepo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, cyclicalServiceRepo);
    }

    @Override
    public void run() {
        //Send Emails
        System.out.println(new Date()+" Runnable Task with "+message
                +" on thread "+Thread.currentThread().getName());
    }
}
