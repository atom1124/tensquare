package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description :
 * @Author : Li
 * @Date: 2020-04-12 21:41
 */
@RunWith(SpringRunner.class)
public class Demo implements It {

    @Test
    public void getHello() {
        System.out.println(save("我愛你"));

    }

    @Override
    public String save(String message) {
        return message;
    }
}
