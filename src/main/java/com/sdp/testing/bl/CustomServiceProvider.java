package com.sdp.testing.bl;

import javax.money.spi.ServiceProvider;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomServiceProvider implements ServiceProvider {

    private CustomRateProvider customRateProvider;

    public CustomServiceProvider(CustomRateProvider customRateProvider) {
        this.customRateProvider = customRateProvider;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public <T> List<T> getServices(Class<T> serviceType) {
        if(serviceType.isAssignableFrom(CustomRateProvider.class)) {
            return (List<T>) Arrays.asList(customRateProvider);
        }
        return Collections.emptyList();
    }
}
