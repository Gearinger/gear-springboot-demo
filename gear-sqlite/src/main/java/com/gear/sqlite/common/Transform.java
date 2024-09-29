package com.gear.sqlite.common;

import lombok.Data;

@Data
public class Transform {

    /**
     * x
     */
    private Double x;

    /**
     * y
     */
    private Double y;

    /**
     * z
     */
    private Double z;

    /**
     * 缩放X
     */
    private Double scaleX;

    /**
     * 缩放Y
     */
    private Double scaleY;

    /**
     * 缩放Z
     */
    private Double scaleZ;

    /**
     * 旋转X（度）
     */
    private Double rotationX;

    /**
     * 旋转Y（度）
     */
    private Double rotationY;

    /**
     * 旋转Z（度）
     */
    private Double rotationZ;

    @Override
    public String toString() {
        return "Transform{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", scaleX=" + scaleX +
                ", scaleY=" + scaleY +
                ", scaleZ=" + scaleZ +
                ", rotationX=" + rotationX +
                ", rotationY=" + rotationY +
                ", rotationZ=" + rotationZ +
                '}';
    }

}
