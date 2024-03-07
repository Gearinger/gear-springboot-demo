package com.gear;


/**
 * 学生
 *
 * @author guoyd
 * @version 1.0.0
 * @date 2021/03/29
 */
public class Student {

    public Student(String name, Integer age, Integer type){
        this.name = name;
        this.age= age;
        this.type = type;
    }

    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    private String name;

    private Integer age;

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
