package ratelimiter;

import java.util.LinkedList;
import java.util.Queue;

public class SlidingWindowSingleDeviceRateLimiter implements RateLimiter {
    private final long maxRequestsPerWindow;
    private final long windowSizeInMillis;
    private final Queue<Long> requestQueue;

    public SlidingWindowSingleDeviceRateLimiter(long maxRequestsPerWindow, long windowSizeInMillis) {
        this.maxRequestsPerWindow = maxRequestsPerWindow;
        this.windowSizeInMillis = windowSizeInMillis;
        this.requestQueue = new LinkedList<>();
    }

    @Override
    public synchronized boolean isRequestAllowed() {
        long now = System.currentTimeMillis();
        while(!requestQueue.isEmpty() && now - requestQueue.peek() >= windowSizeInMillis) {
            requestQueue.poll();
        }
        if(requestQueue.size() < maxRequestsPerWindow) {
            requestQueue.offer(now);
            return true;
        }
        return false;
    }

    /*
    // Driver program to test the rate limiter
    RateLimiter rateLimiter = new SlidingWindowSingleDeviceRateLimiter(5, 1000);
    for(int i = 0; i < 20; i++) {
        if(rateLimiter.isRequestAllowed()) {
            System.out.println("Request allowed at " + System.currentTimeMillis()/1000);
        } else {
            System.out.println("Request denied at " + System.currentTimeMillis()/1000);
        }
        Thread.sleep(100);
    }
     */
}
