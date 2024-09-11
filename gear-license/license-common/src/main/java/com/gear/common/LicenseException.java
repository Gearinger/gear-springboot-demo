package com.gear.common;

/**
 * 许可证例外
 *
 * @author guoyingdong
 * @date 2024/09/10
 */
public class LicenseException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public LicenseException(String message) {
        super(message);
    }

    public LicenseException(String message, Throwable cause) {
        super(message, cause);
    }
}
