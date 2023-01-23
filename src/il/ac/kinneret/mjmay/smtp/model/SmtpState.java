package il.ac.kinneret.mjmay.smtp.model;

import org.w3c.dom.UserDataHandler;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SmtpState {

    private Socket clientSocket;
    PrintWriter pwOut;

    public SmtpState(String serverName, int port)
    {
        try {
            clientSocket = new Socket(serverName, port);
            pwOut = new PrintWriter(clientSocket.getOutputStream());
        } catch (Exception ex)
        {
            clientSocket = null;
            pwOut = null;
        }
    }

    public boolean isConnected()
    {
        return (clientSocket != null && !clientSocket.isClosed());
    }

    /**
     * Close the open connection
     * @return True if the closing went ok.  False otherwise.
     */
    public boolean close() {
        try {
            if (isConnected()) {
                pwOut.close();
                clientSocket.close();
                clientSocket = null;
                return true;
            }
        } catch (Exception ex)
        { }
        return false;
    }

    /**
     * Returns the input stream for the open socket connection.
     * @return  The input stream for the open socket if it can accesesd. NULL otherwise.
     */
    public InputStream getConnectionInputStream() {
        if (isConnected())
        {
            try {
                return clientSocket.getInputStream();
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    public String doHelo(String heloString) {
        /// TODO: Send the HELO command
        if (isConnected())
        {
            pwOut.println("HELO "+heloString );
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response= SharedData.getSB().toString();
            }
            return response;
        }
        return null;
    }

    public String doEhlo(String ehloString) {
        /// TODO: Send the EHLO command
        if (isConnected())
        {
            pwOut.println("EHLO "+ehloString );
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response= SharedData.getSB().toString();
                return response;
            }

        }
        return "CONNET PLS";
    }

    public String doMailFrom(String mailFrom) {
        /// TODO: Send the mail from command
        if (isConnected())
        {
            pwOut.println("MAIL FROM:<"+mailFrom+">");
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response= SharedData.getSB().toString();
            }
            return response;
        }
        return "CONNET PLS";
    }

    public String doRcptTo(String rcptTo) {
        /// TODO: Send the RCPT TO command
        if (isConnected())
        {
            pwOut.println("RCPT TO:<"+rcptTo+">");
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response= SharedData.getSB().toString();
            }
            return response;
        }
        return "CONNET PLS";
    }

    public String doData() {
        /// TODO: Send the DATA command

        if (isConnected())
        {
            pwOut.println("DATA");
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response= SharedData.getSB().toString();
            }
            return response;
        }
        return "CONNET PLS";
    }

    public String doVrfy(String vrfy) {
        /// TODO: Send the VRFY command
        if (isConnected())
        {
            pwOut.println("VRFY "+ vrfy);
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response= SharedData.getSB().toString();
            }
            return response;
        }
        return "CONNECT PLS";
    }

    public String doQuit() {
        /// TODO: Send the QUIT command
        if (isConnected())
        {
            pwOut.println("QUIT");
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response= SharedData.getSB().toString();
            }
            return response;
        }
        return "CONNET PLS";
    }

    public String doRaw(String rawCommand) {
        /// TODO: Send the raw command
        if (isConnected())
        {
            pwOut.println(rawCommand);
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response= SharedData.getSB().toString();
            }
            return response;
        }
        return "CONNET PLS";
    }
}
