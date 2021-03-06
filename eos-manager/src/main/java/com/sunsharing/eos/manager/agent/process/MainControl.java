/**
 * @(#)MainControl
 * 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 *
 *<br> Copyright:  Copyright (c) 2014
 *<br> Company:厦门畅享信息技术有限公司
 *<br> @author ulyn
 *<br> 14-2-5 下午3:21
 *<br> @version 1.0
 *————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *————————————————————————————————
 */
package com.sunsharing.eos.manager.agent.process;

import com.sunsharing.eos.common.rpc.protocol.RequestPro;
import com.sunsharing.eos.common.rpc.protocol.ResponsePro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <pre></pre>
 * <br>----------------------------------------------------------------------
 * <br> <b>功能描述:</b>
 * <br>
 * <br> 注意事项:
 * <br>   1.判断是否审批通过
 * <br>   2.判断是否需要测试
 * <br>   3.调用实际代码
 * <br>   4.调用监控逻辑
 * <br>----------------------------------------------------------------------
 * <br>
 */
public class MainControl {

    Process[] processes = new Process[]{
            new ACLProcess(),
            new MockProcess(),
            new RemoteProcess(),
            new MonitorProcess()
    };

    public void process(RequestPro req, ResponsePro res) {
        ProcessChain processChain = new ProcessChain();
        processChain.setProcessList(processes);
        processChain.doProcess(req, res, processChain);
    }
}

