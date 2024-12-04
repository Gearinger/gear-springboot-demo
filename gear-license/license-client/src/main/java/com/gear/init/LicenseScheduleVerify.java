package com.gear.init;

import com.gear.config.LicenseClientConfig;
import com.gear.util.LicenseVerifyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.gear.constant.Constant.PUBLIC_KEY;

/**
 * 许可证验证
 *
 * @author guoyingdong
 * @date 2024/09/11
 */
@Component
@RequiredArgsConstructor
public class LicenseScheduleVerify {

    private final LicenseClientConfig licenseClientConfig;
    private static final AtomicBoolean ACTIVE_BOOLEAN = new AtomicBoolean(false);

    @PostConstruct
    public void init() {
        checkLicenseStatus();
    }

    public synchronized void checkLicenseStatus() {
        boolean verify = LicenseVerifyUtil.verify(licenseClientConfig.getActiveCode(), PUBLIC_KEY);
        if (verify) {
            System.out.println("~~~~~~~~~~~~~~~license active success~~~~~~~~~~~~~~~");
            ACTIVE_BOOLEAN.set(true);
        } else {
            System.out.println("~~~~~~~~~~~~~~~license active failed~~~~~~~~~~~~~~~");
            ACTIVE_BOOLEAN.set(false);
        }
    }

}
