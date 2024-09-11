package com.gear.controller;

import com.gear.db.LicenseServerInfo;
import com.gear.model.LicenseParam;
import com.gear.model.RsaKeyPair;
import com.gear.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 许可证服务器端
 *
 * @author guoyingdong
 * @date 2024/09/11
 */
@RequestMapping("/licenseServer")
@RestController
@RequiredArgsConstructor
public class LicenseServerController {

    private final LicenseService licenseService;

    /**
     * 创建许可证
     *
     * @param licenseParam 许可证参数
     * @return {@link ResponseEntity}<{@link String}>
     */
    @PostMapping("/createLicense")
    public ResponseEntity<String> createLicense(@RequestBody LicenseParam licenseParam) {
        String result = licenseService.createLicense(licenseParam);
        return ResponseEntity.ok(result);
    }

    /**
     * 列表许可证
     *
     * @return {@link ResponseEntity}<{@link List}<{@link LicenseServerInfo}>>
     */
    @GetMapping("/listLicense")
    public ResponseEntity<List<LicenseServerInfo>> listLicense() {
        List<LicenseServerInfo> list = licenseService.listLicense();
        return ResponseEntity.ok(list);
    }

    /**
     * 生成密钥对
     *
     * @return {@link ResponseEntity}<{@link RsaKeyPair}>
     */
    @PostMapping("/generateKeyPair")
    public ResponseEntity<RsaKeyPair> generateKeyPair() {
        RsaKeyPair result = licenseService.generateKeyPair();
        return ResponseEntity.ok(result);
    }

    /**
     * 验证激活代码
     *
     * @param activeCode 活动代码
     * @return {@link ResponseEntity}<{@link Boolean}>
     */
    @PostMapping("/verifyActiveCode")
    public ResponseEntity<Boolean> verifyActiveCode(@RequestBody String activeCode) {
        boolean result = licenseService.verifyActiveCode(activeCode);
        return ResponseEntity.ok(result);
    }

}
