package ratelimiter;

public class FixedWindowSingleDeviceRateLimiter implements RateLimiter {
    private final long maxRequestPerWindow;
    private final long windowSize; // in milliseconds
    private long windowStartTime;
    private long requestCount;

    public FixedWindowSingleDeviceRateLimiter(long maxRequestPerWindow, long windowSize) {
        this.maxRequestPerWindow = maxRequestPerWindow;
        this.windowSize = windowSize;
        this.windowStartTime = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean isRequestAllowed() {
        long now = System.currentTimeMillis();
        if(now - windowStartTime >= windowSize) {
            requestCount = 0;
            windowStartTime = now;
        }
        if(requestCount < maxRequestPerWindow) {
            requestCount++;
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
