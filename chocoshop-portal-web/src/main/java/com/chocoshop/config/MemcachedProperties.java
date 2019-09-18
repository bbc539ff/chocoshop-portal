package com.chocoshop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "memcache")
public class MemcachedProperties {

    private String server;

    private int poolSize;

    private boolean sanitizeKeys;

    private int opTimeout;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public boolean isSanitizeKeys() {
        return sanitizeKeys;
    }

    public void setSanitizeKeys(boolean sanitizeKeys) {
        this.sanitizeKeys = sanitizeKeys;
    }

    public int getOpTimeout() {
        return opTimeout;
    }

    public void setOpTimeout(int opTimeout) {
        this.opTimeout = opTimeout;
    }
}