package com.gear.model;

import lombok.Data;

@Data
public class RsaKeyPair {

    private String privateKey;

    private String publicKey;

    public RsaKeyPair(String publicKeyStr, String privateKeyStr) {
        this.privateKey = privateKeyStr;
        this.publicKey = publicKeyStr;
    }
}
