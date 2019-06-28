import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author 苏犇
 * @date 2019/6/23 14:54
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SpringJedisTestor {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testSetGet() {
        redisTemplate.opsForValue().set("a", "b");
        //默认情况下，spring-Data-Redis 会采用JDK序列化的方式将所有key value 进行二进制序列化
        // 这会导致redis数据库的值可读性极差， 需要使用序列化
        String str = (String) redisTemplate.opsForValue().get("a");
        System.out.println(str);
    }

    @Test
    public void testJson() {
        User u1 = new User("u1", "p1");
        User u2 = new User("u2", "p2");
        redisTemplate.opsForValue().set("user:u1", u1);
        redisTemplate.opsForValue().set("user:u2", u2);
    }

    @Test
    public void testObjectdeserializer() {
        User u1 = (User) redisTemplate.opsForValue().get("user:u1");
        System.out.println(u1.getUsername() + "=" + u1.getPassword());
    }

    @Test
    public void testHash() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        User u = new User("u3", "p3");
        //关键是如何将JavaBean转为map
        Map<String, String> m1 = BeanUtils.describe(u);
        redisTemplate.opsForHash().putAll("user:m1", m1);
    }

    @Test
    public void testHashDeseri() throws InvocationTargetException, IllegalAccessException {
        Map m1 = redisTemplate.opsForHash().entries("user:m1");
        User u1 = new User();
        BeanUtils.populate(u1, m1);
        System.out.println(u1.getUsername() + "=" + u1.getPassword());
    }

    @Test
    public void testList() {
        for (int i = 0; i < 10; i++) {
            User u = new User("l" + i, "p" + i);
            redisTemplate.opsForList().rightPush("VipUserRank", u);
        }


        List users = redisTemplate.opsForList().range("VipUserRank", 1, 5);
        users.forEach(System.out::println);
    }


    @Test
    //数据库层面的命令要使用execute接口实现
    public void testFlushDB() {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                connection.ping();
                return null;
            }
        });
    }
}
