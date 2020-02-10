package com.tensquare.encrypt.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import com.tensquare.encrypt.rsa.RsaKeys;
import com.tensquare.encrypt.service.RsaService;
import org.apache.commons.codec.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * 网关过滤器  将请求的数据解密
 * @version 1.0
 */
@Component
public class RsaRequestFilter extends ZuulFilter {

    @Autowired
    private RsaService rsaService;

    /**
     * 过滤器类型 设置请求前 后 中 异常 执行此过滤器
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器优先级 数字越小优先级越高  0是最高级别
     * 同样类型的过滤器 看filterOrder值越小优先执行
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 过滤器开关
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 业务逻辑代码
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {

        try {
            System.out.println("过滤器被执行了。。。");
            //1.获取请求数据
            RequestContext currentContext = RequestContext.getCurrentContext();
            HttpServletRequest request = currentContext.getRequest();
            //获取输入流
            ServletInputStream inputStream = request.getInputStream();
            //将输入流转字符串
            String params = StreamUtils.copyToString(inputStream, Charsets.UTF_8);
            //2.将数据解密
            if(!StringUtils.isEmpty(params)){
                params = rsaService.rsaDecryptDataPEM(params, RsaKeys.getServerPrvKeyPkcs8());
                System.out.println("解密后的数据：：：："+params);
                //3.转发请求
                if(!StringUtils.isEmpty(params)){
                    byte[] bytes = params.getBytes();
                    currentContext.setRequest(new HttpServletRequestWrapper(request){
                        @Override
                        public ServletInputStream getInputStream() throws IOException {
                            return new ServletInputStreamWrapper(bytes);
                        }

                        @Override
                        public int getContentLength() {
                            return bytes.length;
                        }

                        @Override
                        public long getContentLengthLong() {
                            return bytes.length;
                        }
                    });

                }
                //4.设置转发信息
                currentContext.addZuulRequestHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //放行 转发到相应微服务了
        return null;
    }
}
