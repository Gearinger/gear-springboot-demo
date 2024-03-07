package com.gear.market.dto;

public class AMarketDTO {
    private String name = "name";
    private String addr = "addr";
    private String phone = "phone";

    public void setName(String name) {
        this.name = name;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getAddr() {
        return addr;
    }

    public String getPhone() {
        return phone;
    }
}
