package ratelimiter;

public class TokenBucketRateLimiter implements RateLimiter {

    private final long maxTokens; // max requests allowed during Burst
    private final double refillRate; // tokens/second
    private long lastRefillTimestamp;
    private long availableTokens;

    public TokenBucketRateLimiter(long maxTokens, long refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.lastRefillTimestamp = System.nanoTime();
        this.availableTokens = maxTokens;
    }

    private synchronized void refillTokens() {
        long now = System.nanoTime();
        long elapsedTime = now - lastRefillTimestamp;
        double newTokens = (elapsedTime / 1_000_000_000.0) * refillRate;
        if(newTokens >= 1) {
            availableTokens = Math.min(maxTokens, availableTokens + (long) newTokens);
            lastRefillTimestamp = now;
        }
    }

    @Override
    public synchronized boolean isRequestAllowed() {
        refillTokens();
        if(availableTokens > 0) {
            availableTokens--;
            return true;
        }
        return false;
    }

    /*
    //Driver program to test the rate limiter
        RateLimiter rateLimiter = new TokenBucketRateLimiter(5, 2);
        long startTime = System.currentTimeMillis();
        for(int i = 0; i < 20; i++) {
             if(rateLimiter.isRequestAllowed()) {
                System.out.println("Request allowed at " + (System.currentTimeMillis() - startTime)/1000.0);
            } else {
                System.out.println("Request denied at " + (System.currentTimeMillis() - startTime)/1000.0);
            }
            Thread.sleep(200);
        }
     */
}
