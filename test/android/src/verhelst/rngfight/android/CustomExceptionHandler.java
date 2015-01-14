package verhelst.rngfight.android;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by Orion on 1/13/2015.
 */
    public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {

        private Thread.UncaughtExceptionHandler defaultUEH;

        private String localPath;

        private String url;

        /*

         * if any of the parameters is null, the respective functionality
         * will not be used
         */
        public CustomExceptionHandler(String localPath, String url) {
            this.localPath = localPath;
            this.url = url;
            this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        }

        public void uncaughtException(Thread t, Throwable e) {

            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            String stacktrace = result.toString();
            printWriter.close();
            String filename = System.currentTimeMillis() + ".stacktrace";

            if (localPath != null) {
                writeToFile(stacktrace, filename);
            }


            defaultUEH.uncaughtException(t, e);
        }

        private void writeToFile(String stacktrace, String filename) {
            try {
                BufferedWriter bos = new BufferedWriter(new FileWriter(
                        localPath + "/" + filename));
                bos.write(stacktrace);
                bos.flush();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


