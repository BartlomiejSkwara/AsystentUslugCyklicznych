package com.example.uslugicykliczne.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class ActivityTrackerService {
    private final HashMap<String, LocalDateTime> lastActivity;
    private final ArrayList<String> markedForForceLogout;

    @Value("${jwt.token.lifetime-in-minutes}")
    private long tokenLifetimeInMinutes;

    public ActivityTrackerService() {
        this.lastActivity = new HashMap<>();
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
    }
    public void removeUserActivity(String login) {
        lastActivity.remove(login);
    }

    public HashMap<String, LocalDateTime> getActivities(){

        Iterator<Map.Entry<String, LocalDateTime>> iterator = lastActivity.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, LocalDateTime> activity = iterator.next();

            if(activity.getValue().isBefore(LocalDateTime.now().minusMinutes(tokenLifetimeInMinutes))) {
                iterator.remove();
            }
        }

        return lastActivity;
    }
}
