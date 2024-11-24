package ratelimiter;

public interface RateLimiter {

    default boolean isRequestAllowed() {
        return false;
    }

    default boolean isRequestAllowed(long clientId) {
        return false;
    }
}
