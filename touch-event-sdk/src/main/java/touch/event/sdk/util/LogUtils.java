package touch.event.sdk.util;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: LogUtils
 *
 * @Author wangjianzhou
 * @Date 2023/3/12 00:14
 * @Version TODO
 */
public class LogUtils {
    private static final String LOG_TAG = "LogUtils :";
    private static boolean isDebug = true;
    private static boolean needStore;
    private static int logLevel = Log.INFO;
    private static int NO_LOGS = -1;

    public static boolean isDebug () {
        return isDebug;
    }

    public static int v (String tag, String logs) {
        if (isDebug) {
            return Log.v(LOG_TAG + tag, logs);
        }
        return NO_LOGS;
    }

    public static int i (String tag, String logs) {
        if (isDebug) {
            return Log.i(LOG_TAG + tag, logs);
        }
        return NO_LOGS;
    }

    public static int d (String tag, String logs) {
        if (isDebug) {
            return Log.i(LOG_TAG + tag, logs);
        }
        return NO_LOGS;
    }

    public static int w (String tag, String logs) {
        return Log.w(LOG_TAG + tag, logs);
    }

    public static int e (String tag, String logs) {
        return Log.e(LOG_TAG + tag, logs);
    }

    public static void exception (String tag, Exception e) {
        if (isDebug) {
            e.printStackTrace();
            Log.e(LOG_TAG, tag, e);
        }
        if (needStore) {
            for (StackTraceElement ele : e.getStackTrace()) {
                writeLogs(ele.getClassName() + " " + ele.getMethodName() + " " + ele.getFileName() + ":" + ele.getLineNumber());
            }
        }
    }

    private static void writeLogs (String content) {
        try {
            File file = new File("uv.ele.trace");
            if (!file.exists()) {
                file.createNewFile();
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            FileWriter writer = new FileWriter(file, true);
            writer.write(format.format(new Date() + " " + content + "\n"));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO  log level 是否需要引入log级别过滤，后期讨论
    //    public static void setLogLevel(int logV) {
    //        if (0 < logV && logV < 7) {
    //            logLevel = logV;
    //        }
    //    }
    public static void setDebugMode (boolean debugMode) {
        isDebug = debugMode;
    }

    public static boolean getDebugMode () {
        return isDebug;
    }

    public static void setStoreMode (boolean storeMode) {
        needStore = storeMode;
    }
}
