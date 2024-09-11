package com.gear.init;

import com.gear.config.LicenseClientConfig;
import com.gear.util.LicenseVerifyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
    private static final int RETRY_NUM = 10;
    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private static final AtomicBoolean ACTIVE_BOOLEAN = new AtomicBoolean(false);

    @PostConstruct
    public void init() {
        checkLicenseStatus();
    }

    public synchronized void checkLicenseStatus() {
        extracted(RETRY_NUM);
    }

    private void extracted(int retriesNum) {
        if (retriesNum < 0) {
            System.out.println("------------license active failed!------------");
            System.exit(0);
        }
        try {
            boolean verify = LicenseVerifyUtil.verify(licenseClientConfig.getActiveCode(), PUBLIC_KEY);
            if (verify) {
                System.out.println("~~~~~~~~~~~~~~~license active success~~~~~~~~~~~~~~~");
                ACTIVE_BOOLEAN.set(true);
                scheduleRetry(1, 60 * 60 * 24);
            } else {
                System.out.println("retry-num " + retriesNum + " ," + Thread.currentThread().getName());
                ACTIVE_BOOLEAN.set(false);
                scheduleRetry(retriesNum, 60);
            }
        } catch (Exception e) {
            ACTIVE_BOOLEAN.set(false);
            scheduleRetry(retriesNum, 60);
        }
    }

    private void scheduleRetry(int retriesNum, int second) {
        executorService.schedule(() -> extracted(retriesNum - 1), second, TimeUnit.SECONDS);
    }

}
