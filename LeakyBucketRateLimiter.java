package ratelimiter;

public class LeakyBucketRateLimiter implements RateLimiter {
    private final long bucketCapacity; //max requests to hold
    private final double leakRate; //requests per second
    private long lastLeakTimestamp;
    private long requestsInBucket;

    public LeakyBucketRateLimiter(long bucketCapacity, long leakRate) {
        this.bucketCapacity = bucketCapacity;
        this.leakRate = leakRate;
        lastLeakTimestamp = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean isRequestAllowed() {
        long now = System.currentTimeMillis();
        double elapsedTime = (now - lastLeakTimestamp)/1000.0;
        if(elapsedTime >= 1) {
            long leakedRequests = (long)(elapsedTime * leakRate);
            requestsInBucket -= leakedRequests;
            if(requestsInBucket < 0) {
                requestsInBucket = 0;
            }
            lastLeakTimestamp = now;
        }
        if(requestsInBucket < bucketCapacity) {
            requestsInBucket++;
            return true;
        }
        return false;
    }

    /*
    //Driver Program to test the rate limiter
        RateLimiter rateLimiter = new TokenBucketRateLimiter(5, 2);
        long startTime = System.currentTimeMillis();
        for(int i = 0; i < 20; i++) {
            if(rateLimiter.isRequestAllowed()) {
                System.out.println("Request allowed at " + (System.currentTimeMillis() - startTime)/1000.0);
            } else {
                System.out.println("Request denied at " + (System.currentTimeMillis() - startTime)/1000.0);
            }
            Thread.sleep(300);
        }
     */
}
