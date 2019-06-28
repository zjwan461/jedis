import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author 苏犇
 * @date 2019/6/23 12:52
 */

public class JedisPoolTestor {

    @Test
    public void testJedisPool() {
        GenericObjectPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100); //设置连接池中最多允许放100个jedis对象
        //建议maxidle与maxtotal一致
        config.setMaxIdle(100); //设置连接池中最大允许的空闲连接
        config.setMinIdle(10); //设置连接池中最小允许的连接数，根据实际情况考虑
        config.setTestOnBorrow(false); //借出连接的时候是否测试有效性，推荐false
        config.setTestOnReturn(false); //创建是否测试有效
        config.setTestOnCreate(false); //创建时是否测试有效
        config.setBlockWhenExhausted(true); //当连接池内jedis无可用资源时，是否等待资源， true
        config.setMaxWaitMillis(1000); //没有获取到资源时最长等待1秒，1秒后还没有的话就报错（抛出异常）
        JedisPool pool = new JedisPool(config, "192.168.152.129", 6379);
        Jedis jedis = null;
        try {
            jedis = pool.getResource(); //从连接池中借出borrow 一个jedis对象
            jedis.auth("123456");
            jedis.select(0);
            jedis.set("name", "suben");
            System.out.println(jedis.get("name"));
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null){
                jedis.close(); //在使用连接池的时候，close（）方法不在时关闭，而是归还连接池
            }
        }
    }

}
