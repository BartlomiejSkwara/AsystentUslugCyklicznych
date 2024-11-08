package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.utility.TimeUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ActivityTrackerService {
//    record UserActivity(String login, Integer passedMinutes){}

    private final HashMap<String, LocalDateTime> lastActivity;
    private final HashMap<String,Integer> calculatedMinutesActivities;

    private final ArrayList<String> markedForForceLogout;


    @Value("${jwt.token.lifetime-in-minutes}")
    private long tokenLifetimeInMinutes;

    public ActivityTrackerService() {
        this.lastActivity = new HashMap<>();
        this.calculatedMinutesActivities = new HashMap<>();
        this.markedForForceLogout = new ArrayList<>();
    }

    public void forceLogout(String login){
        markedForForceLogout.add(login);
        removeUserActivity(login);
    }
    public boolean removeFromMarkedForLogoutList(String login){
        return markedForForceLogout.remove(login);
    }
    public void addUserActivity(String login, LocalDateTime time) {
        lastActivity.put(login, time);
        calculatedMinutesActivities.put(login,0);
    }
    public void removeUserActivity(String login) {
        lastActivity.remove(login);
        calculatedMinutesActivities.remove(login);
    }

    public HashMap<String, Integer> getCalculatedMinutesActivities(){

        Iterator<Map.Entry<String, LocalDateTime>> iterator = lastActivity.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, LocalDateTime> activity = iterator.next();
            if(activity.getValue().isBefore(LocalDateTime.now().minusMinutes(tokenLifetimeInMinutes))) {
                calculatedMinutesActivities.remove(activity.getKey());
                iterator.remove();
            }
        }

        LocalDateTime localDateTime = LocalDateTime.now();

        for (Map.Entry<String, Integer> activity : calculatedMinutesActivities.entrySet()) {
            activity.setValue((int) Duration.between(lastActivity.get(activity.getKey()),localDateTime).toMinutes());
        }
//        calculatedMinutesActivities.forEach((s, integer) -> integer = ;



        return calculatedMinutesActivities;
    }
}
