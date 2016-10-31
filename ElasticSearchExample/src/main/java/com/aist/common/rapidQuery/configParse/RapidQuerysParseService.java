package com.aist.common.rapidQuery.configParse;

import org.springframework.context.SmartLifecycle;

/**
 * 〈一句话功能简述〉 功能详细描述
 * 
 * @author HuangSiwei
 * @create 2014年9月3日 上午11:13:14
 * @version 1.0.0
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */

public interface RapidQuerysParseService extends SmartLifecycle {
    public void reloadFile();
}
