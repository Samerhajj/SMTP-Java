package il.ac.kinneret.mjmay.smtp.model;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IncomingListener extends Task<Void> {

    BufferedReader brIn;
    public IncomingListener(InputStream inputStream)
    {
        brIn = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    protected Void call() throws Exception {
        // read what we can
        StringBuilder sb = new StringBuilder();
        do
        {
            boolean doneBlock = false;
            String line = null;
            // lock the string builder
            while (!doneBlock) {
                line = brIn.readLine();
                int code = -1;
                if (line != null) {
                    sb.append(line + "\n");
                    System.out.println(line);

                    try {
                        code = Integer.parseInt(line.split(" ")[0]);
                    } catch (NumberFormatException nfe)
                    {
                        code = -1;
                    }

                }
                if (line == null || code != -1)
                {
                    doneBlock = true;
                }
            }
            //System.out.println(builder.toString() + "\n");
            // we're done if there's nothing here
            if (line == null || sb.toString().length() == 0)
            {
                break;
            }

            synchronized (SharedData.getSB()) {
                SharedData.getSB().append(sb);
            }
            // clear our cache
            sb.setLength(0);
            updateMessage(line);
        }
        while (!isCancelled());
        return null;
    }
}
