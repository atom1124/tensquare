package com.tensquare.test;

import com.tensquare.EncryptApplication;
import com.tensquare.encrypt.rsa.RsaKeys;
import com.tensquare.encrypt.service.RsaService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EncryptApplication.class)
public class EncryptTest {

    @Autowired
    private RsaService rsaService;

    @Before
    public void before() throws Exception{
    }

    @After
    public void after() throws Exception {
    }

    /**
     * 加密测试
     */
    @Test
    public void genEncryptDataByPubKey() {
        String data = "{\"title\":\"十次方项目\"}";

        try {

            String encData = rsaService.rsaEncryptDataPEM(data, RsaKeys.getServerPubKey());

            System.out.println("data: " + data);
            System.out.println("encData: " + encData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 解密测试
     */
    @Test
    public void genByPubKey() {
        String data = "NtEfHHufWZJyfGrRx+XP2uXnTtwfvPfljCMnALJy7raz54Ht0Z70w+x7o6+fc7SgHY+uarF560ZMyAuHElO59d21DaOdk2yDG0BN8V8DiWxLyOx9+bnHVTH849OeuHRQA9QtnclcKP9f2ZVbV+KGPm/9IObhVyusmpDCkgC/9u3YYckivmzSiCYY+yWl7D+jVBS/WiG0rqO+ssiFMDEqR5sVarDiATSPl5FlSj7SHEKKiSBBeHo4Y4NeFSQHiJDzasBAWY4gj62zoyF/et5TP6V8ug9OgeC3tKHneEYua1Qq2LneZg5khWNd+CnRrc7zIgsRZz0arRz0K0MpktzTsg==";

        try {

            String mydata = rsaService.rsaDecryptDataPEM(data, RsaKeys.getServerPrvKeyPkcs8());

            System.out.println("data: " + data);
            System.out.println("encData: " + mydata);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
