package ratelimiter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class SlidingWindowRateLimiter implements RateLimiter {
    private final long maxRequestsPerWindow;
    private final long windowSizeInMillis;
    private final Map<Long, Queue<Long>> requestMap;

    public SlidingWindowRateLimiter(long maxRequestsPerWindow, long windowSizeInMillis) {
        this.maxRequestsPerWindow = maxRequestsPerWindow;
        this.windowSizeInMillis = windowSizeInMillis;
        this.requestMap = new HashMap<>();
    }

    @Override
    public synchronized boolean isRequestAllowed(long clientId) {
        long now = System.currentTimeMillis();
        requestMap.putIfAbsent(clientId, new LinkedList<>());
        Queue<Long> requestQueue = requestMap.get(clientId);
        while(!requestQueue.isEmpty() && now - requestQueue.peek() >= windowSizeInMillis) {
            requestQueue.poll();
        }
        if(requestQueue.size() < maxRequestsPerWindow) {
            requestQueue.offer(now);
            return true;
        }
        return false;
    }
}
