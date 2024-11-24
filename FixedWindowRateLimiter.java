package ratelimiter;

import java.util.HashMap;
import java.util.Map;

public class FixedWindowRateLimiter implements RateLimiter {

    private static class RequestCounter {
        private long requestCount;
        private long windowStartTime;

        public RequestCounter(long windowStartTime) {
            this.windowStartTime = windowStartTime;
        }
    }

    private final long maxRequestsPerWindow;
    private final long windowSize;  //in millisecond
    private Map<Long, RequestCounter> requestMap;

    public FixedWindowRateLimiter(long maxRequestsPerWindow, long windowSize) {
        this.maxRequestsPerWindow = maxRequestsPerWindow;
        this.windowSize = windowSize;
        requestMap = new HashMap<>();
    }

    @Override
    public synchronized boolean isRequestAllowed(long clientId) {
        long now = System.currentTimeMillis();
        requestMap.putIfAbsent(clientId, new RequestCounter(now));
        RequestCounter requestCounter = requestMap.get(clientId);
        if(now - requestCounter.windowStartTime >= windowSize) {
            requestCounter.requestCount = 0;
            requestCounter.windowStartTime = now;
        }
        if(requestCounter.requestCount < maxRequestsPerWindow) {
            requestCounter.requestCount++;
            return true;
        }
        return false;
    }

    /*
        //Driver program to test the rate limiter
        RateLimiter rateLimiter = new FixedWindowRateLimiter(5, 1000);
        for(int i = 0; i < 20; i++) {
            if(rateLimiter.isRequestAllowed(12345)) {
                System.out.println("Request allowed at " + System.currentTimeMillis()/1000);
            } else {
                System.out.println("Request denied at " + System.currentTimeMillis()/1000);
            }
            Thread.sleep(100);
        }
     */
}
