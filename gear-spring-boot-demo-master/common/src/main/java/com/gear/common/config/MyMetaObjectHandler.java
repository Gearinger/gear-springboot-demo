package com.gear.common.config;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    //使用MybatisPlus实现添加操作时，该方法会执行
    public void insertFill(MetaObject metaObject) {
        //参数：需要设置的属性；设置的时间；元数据(理解：表中的数据)
        this.setFieldValByName("createTime", DateTime.now().toTimestamp(), metaObject);
        this.setFieldValByName("updateTime", DateTime.now().toTimestamp(), metaObject);

    }

    @Override
    //使用MybatisPlus实现修改操作时，该方法会执行
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", DateTime.now().toTimestamp(), metaObject);
    }
}
