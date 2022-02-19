package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.security.DefaultDosProtectionService;
import com.es.phoneshop.model.product.security.DosProtectionService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class DefaultDosProtectionServiceTest {

    DosProtectionService dosProtectionService = DefaultDosProtectionService.getInstance();
    boolean isHaveAccess;

    @Before
    public void init() {
        for (int i = 0; i < 21; i++) {
            isHaveAccess = dosProtectionService.isAllowedIp("1");
        }
    }

    @Test()
    public void testIsAllowedIp() {
        assertFalse(isHaveAccess);
    }

    @Test(timeout = 60000)
    public void testClear() throws InterruptedException {
        isHaveAccess = dosProtectionService.isAllowedIp("1");

        assertFalse(isHaveAccess);
    }
}
