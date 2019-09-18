package com.chocoshop.config;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MemcacheConfig {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MemcachedProperties memcachedProperties;

    //构建builder
    @Bean
    public MemcachedClientBuilder getXMBuilder(){
        MemcachedClientBuilder memcachedClientBuilder = null;
        try {
            String server =memcachedProperties.getServer();
            memcachedClientBuilder= new XMemcachedClientBuilder(server);
            memcachedClientBuilder.setFailureMode(false);
            memcachedClientBuilder.setSanitizeKeys(memcachedProperties.isSanitizeKeys());
            memcachedClientBuilder.setConnectionPoolSize(memcachedProperties.getPoolSize());
            memcachedClientBuilder.setCommandFactory(new BinaryCommandFactory());
            memcachedClientBuilder.setOpTimeout(memcachedProperties.getOpTimeout());
            memcachedClientBuilder.setSessionLocator(new KetamaMemcachedSessionLocator());
            return  memcachedClientBuilder;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public MemcachedClient getXMClient(MemcachedClientBuilder builder){
        MemcachedClient client = null;
        try {
            client = builder.build();
            logger.info("MemcachedClient 创建成功 ");
            return client;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}