package cn.fivk.test;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import java.util.Set;

/**
 * 使用Jedis操作Redis
 */
public class JedisTest {

    @Test
    public void testRedis() {
        //1 获取连接
        Jedis jedis = new Jedis("localhost",6379);

        //2 执行具体的操作
        jedis.set("username","xiaoming");

        String username = jedis.get("username");
        System.out.println(username);

        //3 关闭连接
        jedis.close();
    }
}