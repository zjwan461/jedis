import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author 苏犇
 * @date 2019/6/23 11:41
 */

public class JedisTestor {

    @Test
    public void testJedis() throws Exception {
        Jedis jedis = new Jedis("192.168.152.129", 6379, 1000);
        try {
            jedis.auth("123456");//设置密码
            jedis.select(0);//选择第一个数据库下标，从0开始
            jedis.flushDB();//清空第一个数据库的数据
            jedis.set("hello", "world");//jedis.xxx方法名字就是命令
            jedis.mset(new String[]{"a", "1", "b", "2", "c", "3"});
            List<String> str = jedis.mget("a", "c");
            System.out.println(str);
            System.out.println(jedis.get("hello"));
            System.out.println(jedis.incr("c"));
            System.out.println(jedis.del("b"));
        } catch (Exception e) {
            throw e;
        } finally {
            jedis.close();//释放连接
        }
    }

    @Test
    public void testHash() {
        Jedis jedis = new Jedis("192.168.152.129", 6379, 1000);
        try {
            jedis.auth("123456");
            jedis.select(0);
            jedis.flushDB();
            jedis.hset("user:1:info", "name", "suben");
            jedis.hset("user:1:info", "age", "24");
            jedis.hset("user:1:info", "sex", "男");
            String name = jedis.hget("user:1:info", "name");
            System.out.println(name);
            System.out.println(jedis.hgetAll("user:1:info"));

        } catch (Exception e) {
            throw e;
        } finally {
            jedis.close();
        }

    }
}
