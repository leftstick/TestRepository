package org.howard.portal.kit.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * The purpose of this class is to provide a external command handler
 */
public class ShellRunner {
    private Process process;

    private String cmdline = null;

    private Properties runProperties = null;

    private File dir = null;

    private int lastExitValue = Integer.MAX_VALUE;

    private boolean isWindows = System.getProperty("os.name").startsWith("Windows");

    /**
     * <code>consoleHandle</code>
     */
    OutputHandle consoleHandle;

    private Thread threadin;

    private Thread threaderr;

    private String windowsComander = "cmd.exe /c ";

    /**
     * Creates a new instance of <code>ShellRunner</code>.
     * 
     * @param cmdline
     */
    public ShellRunner(String cmdline) {
        this();
        setCmdline(cmdline);
    }

    /**
     * Creates a new instance of <code>ShellRunner</code>.
     */
    public ShellRunner() {
        consoleHandle = new BaseOutConsoleHandle();
    }

    /**
     * @return return value
     */
    public int run() {
        if (processIsRunning()) {
            throw new IllegalStateException("Can not start command  when processor is running");
        }

        String runCommands = getRunCommand();
        try {
            process = Runtime.getRuntime().exec(runCommands, getEnvString(), getRunpath());
        } catch (IOException e1) {
            throw new RuntimeException("call System Process Error :" + e1.getMessage(), e1);
        }
        threadin = new ReadThread(process.getInputStream(), "[INPUT]", false);
        threaderr = new ReadThread(process.getErrorStream(), "[ERROR]", true);
        threadin.start();
        threaderr.start();
        int rcode = 0;
        try {
            rcode = process.waitFor();
        } catch (InterruptedException e) {
            stopProcess();
            rcode = process.exitValue();
        } finally {
            process = null;
        }

        lastExitValue = rcode;
        try {
            threadin.join();
            threaderr.join();
        } catch (InterruptedException e) {
            //
        }
        return rcode;
    }

    /**
     * @return Arrays of env
     */
    public String[] getEnvString() {
        if (this.runProperties == null)
            return null;
        String[] result = new String[runProperties.size()];

        int i = 0;
        for (Iterator<Entry<Object, Object>> iterator = runProperties.entrySet().iterator(); iterator.hasNext();) {
            Entry<Object, Object> entry = iterator.next();
            String element = entry.getKey().toString();
            result[i++] = element + "=" + entry.getValue().toString();

        }
        return result;
    }

    /**
     * @return cmdline
     */
    public String getRunCommand() {
        return cmdline;
    }

    //    /**
    //     * append message to OutPut Handler
    //     * 
    //     * @param messageCode
    //     * @throws IOException
    //     */
    //    public void appendMessage(byte[] messageCode) throws IOException {
    //        if (process != null) {
    //            process.getOutputStream().write(messageCode);
    //        } else {
    //            throw new IOException("Process is not start or has been destoryed.");
    //        }
    //    }
    //
    //    /**
    //     * append message to OutPut Handler with system specified
    //     * line.separator
    //     * 
    //     * @param message
    //     * @throws IOException
    //     */
    //    public void appendMessageLn(String message) throws IOException {
    //        appendMessage((message + System.getProperty("line.separator")).getBytes());
    //    }

    /**
     * stop
     */
    public void stopProcess() {
        Process r = process;
        if (r != null) {
            r.destroy();
        }

        if (threadin != null) {
            threadin.interrupt();
        }

        if (threaderr != null) {
            threaderr.interrupt();
        }

    }

    /**
     * @return true if running, otherwise false.
     */
    public boolean processIsRunning() {
        return process != null;
    }

    /**
     * @return running env properties.
     */
    public Properties getRuningEnv() {
        return runProperties;
    }

    /**
     * @param runProperties
     */
    public void setRuningEnv(Properties runProperties) {
        this.runProperties = runProperties;
    }

    /**
     * @return run path
     */
    public File getRunpath() {
        return dir;
    }

    /**
     * @param dir
     */
    public void setRunpath(File dir) {
        this.dir = dir;
    }

    private static final class BaseOutConsoleHandle implements OutputHandle {
        @Override
        public void onOutRead(String message, boolean isErrorOut) {
            (isErrorOut ? System.err : System.out).println(message);
        }
    }

    /**
     * The purpose of this class is to provide a async Thread to read
     * output
     */
    class ReadThread extends Thread {

        private final InputStream inputStream;

        private final String cName;

        private final boolean isErrorOut;

        /**
         * Creates a new instance of <code>ReadThread</code>.
         * 
         * @param iStream
         * @param cName
         * @param isErrorOut
         */
        ReadThread(InputStream iStream, String cName, boolean isErrorOut) {

            this.inputStream = iStream;
            this.cName = cName;
            this.isErrorOut = isErrorOut;
        }

        /**
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {
            BufferedInputStream stream = new BufferedInputStream(this.inputStream);
            Reader reader = new InputStreamReader(stream,Charset.forName("GBK"));
            BufferedReader reader2 = new BufferedReader(reader);

            String readSize = null;
            try {
                while ((readSize = reader2.readLine()) != null && !isInterrupted()) {
                    consoleHandle.onOutRead(readSize, isErrorOut);
                }
            } catch (IOException e) {
                throw new RuntimeException(cName + "=" + e.getMessage(), e);
            }
        }
    }

    /**
     * @return lastExitValue
     */
    public int getLastExitValue() {
        return lastExitValue;
    }

    /**
     * @param cmdline
     */
    public void setCmdline(String cmdline) {
        String cmd = cmdline;
        if (this.processIsRunning()) {
            throw new IllegalStateException("Can not set cmd line when processor is running");
        }

        if (isWindows) {
            if ((cmd.length() > 4)
                    && (cmd.substring(0, 4).equalsIgnoreCase("cmd ") || cmd.substring(0, 4).equalsIgnoreCase("cmd."))) {
                // nothing
            } else {
                cmd = getWindowsCommandMode() + cmd;
            }
        }
        this.cmdline = cmd;
    }

    /**
     * @return windowsComander
     */
    public String getWindowsCommandMode() {
        return windowsComander;
    }

    public static void main(String[] args) {
        ShellRunner runner = new ShellRunner();
        runner.setCmdline("mvn E:\\Study\\portal-team\\design\\test2-war");
        runner.run();
        System.out.print("code : " + runner.getLastExitValue());

    }

    /**
     * @param consoleHandle
     */
    public void setConsoleHandle(OutputHandle consoleHandle) {
        this.consoleHandle = consoleHandle;
    }

    /**
     * @param windowsComanderMode
     */
    public void setWindowsComanderMode(String windowsComanderMode) {
        this.windowsComander = windowsComanderMode;
    }

    /**
     * The purpose of this class is to provide a interface of command
     * output handle
     */
    interface OutputHandle {
        /**
         * @param message
         * @param isErrorOut
         */
        public void onOutRead(String message, boolean isErrorOut);
    }
}
