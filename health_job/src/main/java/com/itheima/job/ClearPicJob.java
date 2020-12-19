package com.itheima.job;

import com.itheima.constant.RedisConstant;
import com.itheima.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearPicJob {

    @Autowired
    private JedisPool jedisPool;

    public void deletePic(){
        System.out.println("delete pic start");

        Jedis resource = jedisPool.getResource();

        Set<String> picNames = resource.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (picNames == null || picNames.size()==0)
            return;

        for (String picName : picNames) {
            QiniuUtils.deleteFileFromQiniu(picName);
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
            System.out.println("deleted\t"+picName);
        }
    }
}
