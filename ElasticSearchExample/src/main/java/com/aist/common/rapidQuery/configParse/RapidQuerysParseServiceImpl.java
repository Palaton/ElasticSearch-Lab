package com.aist.common.rapidQuery.configParse;

import com.aist.common.rapidQuery.configParse.config.RapidQuerysContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Service;

import java.io.InputStream;


/**
 * 
 * 
 * @author ouyq
 * @version $Id: RapidQuerysParseServiceImpl.java, v 0.1 2016年10月12日 下午5:01:33 ouyq Exp $
 */
@Service
public class RapidQuerysParseServiceImpl implements RapidQuerysParseService {

    private static final String DEFAULT_QUICK_QUERY_XML = "query/rapidQuery.xml";

    private String              fileName                = DEFAULT_QUICK_QUERY_XML;

    /**
     * @see SmartLifecycle
     */
    private volatile boolean autoStartup = true;

    /**
     * @see SmartLifecycle
     */
    private volatile boolean running;

    /**
     * @see SmartLifecycle
     */
    private volatile int phase;
    
    @Override
    public void reloadFile() {
        RapidQuerysContext queryContext = RapidQuerysContext.getInstance();

        InputStream resource = this.getClass().getClassLoader().getResourceAsStream(fileName);

        try {
            queryContext.LOCK.lock();

            if (queryContext.isParsed() && !queryContext.isDebug()) {
                return;
            }

            queryContext.clear();

            new RapidQueryXmlReader().loadMainFile(resource);

        } finally {
            queryContext.LOCK.unlock();
        }
    }

    /**
     * 设置 fileName 的值
     * 
     * @param fileName
     */

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    
    @Override
    public int getPhase() {
        return this.phase;
    }

    /**
     * @param phase the phase
     * @see SmartLifecycle
     */
    public void setPhase(int phase) {
        this.phase = phase;
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public boolean isAutoStartup() {
        return this.autoStartup;
    }

    /**
     * @param autoStartup true to automatically start
     * @see SmartLifecycle
     */
    public void setAutoStartup(boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    @Override
    public void start() {
        reloadFile();
    }

    @Override
    public void stop() {
    }

    @Override
    public void stop(Runnable runnable) {
        stop();
        runnable.run();
    }

    public static void main(String[] args) {
        RapidQuerysParseServiceImpl impl = new RapidQuerysParseServiceImpl();
        impl.reloadFile();
        RapidQuerysContext queryContext  = RapidQuerysContext.getInstance();
        System.out.println(queryContext.getConfigFileList());
        System.out.println("finish");
    }

}
