package com.example.takehome.utils.ratelimit;

import com.example.takehome.AppProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class SimpleRateLimiterImpl implements SimpleRateLimit {

    private final Map<Boolean, Bucket> bucktes;

    public SimpleRateLimiterImpl(AppProperties appProperties) {
        bucktes = Map.of(
                true, new Bucket(appProperties.getAuthenticatedRps()),
                false, new Bucket(appProperties.getUnauthenticatedRps())
        );
    }

    synchronized public boolean enter(boolean isAuthenticated) {
        return bucktes.get(isAuthenticated).hasCapacity();
    }

    @Data
    private static class Bucket {

        private long mRPS;
        private long capacity;
        private LocalDateTime lastUsed;
        public Bucket(int mRPS) {
            this.mRPS = mRPS;
        }

        public boolean hasCapacity() {
            capacity++;
            var hasCapacity = true;
            lastUsed = Objects.isNull(lastUsed) ? LocalDateTime.now() : lastUsed;

            if (ChronoUnit.SECONDS.between(lastUsed, LocalDateTime.now()) <= 1) {

                if (capacity > mRPS) {
                    hasCapacity = false;
                }

            } else {
                capacity = 0;
                lastUsed = LocalDateTime.now();
            }


            return hasCapacity;
        }
    }
}
