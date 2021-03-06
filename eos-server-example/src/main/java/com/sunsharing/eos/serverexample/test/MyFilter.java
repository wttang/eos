/**
 * @(#)HttpProxyFilter
 * 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 *
 *<br> Copyright:  Copyright (c) 2015
 *<br> Company:厦门畅享信息技术有限公司
 *<br> @author ulyn
 *<br> 15-1-19 下午7:02
 *<br> @version 1.0
 *————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *————————————————————————————————
 */
package com.sunsharing.eos.serverexample.test;

import com.sunsharing.component.utils.crypto.Base64;
import com.sunsharing.eos.client.rpc.ProxyFilter;
import com.sunsharing.eos.common.filter.*;
import com.sunsharing.eos.common.rpc.RpcException;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre></pre>
 * <br>----------------------------------------------------------------------
 * <br> <b>功能描述:</b>
 * <br>
 * <br> 注意事项:
 * <br>
 * <br>
 * <br>----------------------------------------------------------------------
 * <br>
 */
public class MyFilter extends AbstractServiceFilter {
    Logger logger = Logger.getLogger(MyFilter.class);

    /**
     * 执行过滤
     *
     * @param req
     * @param res
     * @param filterChain
     */
    @Override
    protected void doFilter(ServiceRequest req, ServiceResponse res, FilterChain filterChain) throws ServiceFilterException, RpcException {
        logger.info("myFilter:" + req.getAppId() + "-"
                + req.getServiceId() + "-" + req.getServiceVersion()
                + "-" + req.getMethodName());
        logger.info("上下文：" + req.getAttributeMap());
        //这句一定要加上啊。。。不然不会往下执行
        filterChain.doFilter(req, res);
    }
}

