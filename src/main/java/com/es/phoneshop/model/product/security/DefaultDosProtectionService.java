package com.es.phoneshop.model.product.security;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {

    private static final long THRESHOLD = 20;
    private static final long TIME_TO_RESET = 60000L;

    private Map<String, Long> countMap = new ConcurrentHashMap();

    private DefaultDosProtectionService() {
        TimerTask task = new TimerTask() {
            public void run() {
                countMap.clear();
            }
        };
        Timer timer = new Timer("Timer");
        timer.schedule(task, TIME_TO_RESET, TIME_TO_RESET);
    }

    private static class SingletonHolder {
        public static final DefaultDosProtectionService HOLDER_INSTANCE = new DefaultDosProtectionService();
    }

    public static DefaultDosProtectionService getInstance() {
        return DefaultDosProtectionService.SingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public boolean isAllowedIp(String ip) {
        Long count = countMap.get(ip);
        if (count == null) {
            count = 1L;
        } else {
            if (count > THRESHOLD) {
                return false;
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }
}
