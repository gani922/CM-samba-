package com.pramaindia.customer_management.security;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RateLimiterService {

    private static final int MAX_REQUESTS_PER_MINUTE = 100;
    private static final int MAX_REQUESTS_PER_HOUR = 1000;
    private static final int BLOCK_DURATION_MINUTES = 15;

    private final Map<String, RequestCounter> requestCounters = new ConcurrentHashMap<>();
    private final Map<String, Long> blockedIps = new ConcurrentHashMap<>();

    public boolean isAllowed(String clientIp, String endpoint) {
        // Check if IP is blocked
        if (isIpBlocked(clientIp)) {
            return false;
        }

        RequestCounter counter = requestCounters
                .computeIfAbsent(clientIp, k -> new RequestCounter());

        long currentTime = System.currentTimeMillis();

        // Check minute limit
        if (counter.minuteCount.get() >= MAX_REQUESTS_PER_MINUTE) {
            if (currentTime - counter.minuteStart < 60000) {
                blockIp(clientIp);
                return false;
            } else {
                counter.resetMinuteCounter(currentTime);
            }
        }

        // Check hour limit
        if (counter.hourCount.get() >= MAX_REQUESTS_PER_HOUR) {
            if (currentTime - counter.hourStart < 3600000) {
                blockIp(clientIp);
                return false;
            } else {
                counter.resetHourCounter(currentTime);
            }
        }

        // Increment counters
        counter.minuteCount.incrementAndGet();
        counter.hourCount.incrementAndGet();

        return true;
    }

    private boolean isIpBlocked(String clientIp) {
        Long blockedTime = blockedIps.get(clientIp);
        if (blockedTime != null) {
            if (System.currentTimeMillis() - blockedTime > BLOCK_DURATION_MINUTES * 60 * 1000) {
                blockedIps.remove(clientIp);
                return false;
            }
            return true;
        }
        return false;
    }

    private void blockIp(String clientIp) {
        blockedIps.put(clientIp, System.currentTimeMillis());
    }

    private static class RequestCounter {
        AtomicInteger minuteCount = new AtomicInteger(0);
        AtomicInteger hourCount = new AtomicInteger(0);
        long minuteStart = System.currentTimeMillis();
        long hourStart = System.currentTimeMillis();

        void resetMinuteCounter(long currentTime) {
            minuteCount.set(1);
            minuteStart = currentTime;
        }

        void resetHourCounter(long currentTime) {
            hourCount.set(1);
            hourStart = currentTime;
        }
    }
}