package com.example.takehome.utils.ratelimit;

public interface SimpleRateLimit {


    boolean enter(boolean isAuthenticated);

}