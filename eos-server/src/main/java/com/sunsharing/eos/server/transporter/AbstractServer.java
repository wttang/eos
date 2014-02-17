/**
 * @(#)AbstractServer
 * 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 *
 *<br> Copyright:  Copyright (c) 2014
 *<br> Company:厦门畅享信息技术有限公司
 *<br> @author ulyn
 *<br> 14-1-31 下午5:05
 *<br> @version 1.0
 *————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *————————————————————————————————
 */
package com.sunsharing.eos.server.transporter;

import com.sunsharing.eos.common.config.ServiceConfig;
import com.sunsharing.eos.common.config.ServiceMethod;
import com.sunsharing.eos.common.rpc.Invocation;
import com.sunsharing.eos.common.rpc.Result;
import com.sunsharing.eos.common.rpc.RpcException;
import com.sunsharing.eos.common.rpc.RpcServer;
import com.sunsharing.eos.common.rpc.impl.RpcResult;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
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
public abstract class AbstractServer implements RpcServer {
    Logger logger = Logger.getLogger(AbstractServer.class);
    //服务器端口
    protected int port = 20382;
    //是否运行中
    protected boolean isRunning = false;
    //存储服务
    protected Map<String, Object> serviceEngine = new HashMap<String, Object>();
    protected Map<String, ServiceConfig> serviceConfigEngine = new HashMap<String, ServiceConfig>();

    public AbstractServer(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void register(Object impl, ServiceConfig config) {
        this.serviceEngine.put(config.getId(), impl);
        this.serviceConfigEngine.put(config.getId(), config);
        if (!isRunning()) {
            this.start();
        }
        //往zookeeper注册服务，已经不需要了，直接写在ServiceConnectCallBack
    }

    public Result call(String serviceId, Invocation invocation) {
        Object obj = this.serviceEngine.get(serviceId);
        ServiceConfig serviceConfig = this.serviceConfigEngine.get(serviceId);
        RpcResult result = new RpcResult();
        if (obj != null) {
            try {
                ServiceMethod method = serviceConfig.getMethod(invocation.getMethodName());
                if (method == null) {
                    throw new NoSuchMethodException(invocation.getMethodName() + "的ServiceMethod==null");
                }
                //这边暂时直接使用jdk代理执行
                //此处的parameterTypes不用invocation的，规定不允许方法重载
                Method m = obj.getClass().getMethod(invocation.getMethodName(), method.getParameterTypes());
                Object o = m.invoke(obj, invocation.getArguments());
                result.setValue(o);
            } catch (NoSuchMethodException e) {
                String errorMsg = "server has no NoSuchMethodException：" + serviceConfig.getId() + " - " + invocation.getMethodName();
                logger.error(errorMsg, e);
                result.setException(new IllegalArgumentException(errorMsg, e));
            } catch (Exception th) {
                String errorMsg = "执行反射方法异常" + serviceConfig.getId() + " - " + invocation.getMethodName();
                logger.error(errorMsg, th);
                result.setException(new RpcException(RpcException.REFLECT_INVOKE_EXCEPTION, errorMsg));
            }
        } else {
            String errorMsg = "has no these class：" + serviceConfig.getId() + " - " + invocation.getMethodName();
            logger.error(errorMsg);
            result.setException(new IllegalArgumentException(errorMsg));
        }
        return result;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
}

