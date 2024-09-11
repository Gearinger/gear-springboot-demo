package com.gear.controller;

import com.gear.model.ClientLicenseInfo;
import com.gear.config.LicenseClientConfig;
import com.gear.model.LicenseParam;
import com.gear.util.LicenseVerifyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gear.constant.Constant.PUBLIC_KEY;

/**
 * 许可客户端
 *
 * @author guoyingdong
 * @date 2024/09/11
 */
@RequestMapping("/licenseClient")
@RestController
@RequiredArgsConstructor
public class LicenseClientController {

    private final LicenseClientConfig licenseClientConfig;

    /**
     * 许可证信息
     *
     * @return {@link ResponseEntity}<{@link LicenseParam}>
     * @throws Exception 例外
     */
    @GetMapping("/licenseInfo")
    public ResponseEntity<ClientLicenseInfo> licenseInfo() throws Exception {
        LicenseParam licenseParam = LicenseVerifyUtil.parseLicense(licenseClientConfig.getActiveCode(), PUBLIC_KEY);
        ClientLicenseInfo clientLicenseInfo = new ClientLicenseInfo(licenseParam);
        boolean verify = LicenseVerifyUtil.verify(licenseClientConfig.getActiveCode(), PUBLIC_KEY);
        clientLicenseInfo.setIsActive(verify);
        return ResponseEntity.ok(clientLicenseInfo);
    }

}
