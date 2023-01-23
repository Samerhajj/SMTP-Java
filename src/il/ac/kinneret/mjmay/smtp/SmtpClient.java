package il.ac.kinneret.mjmay.smtp;

import il.ac.kinneret.mjmay.smtp.model.IncomingListener;
import il.ac.kinneret.mjmay.smtp.model.SharedData;
import il.ac.kinneret.mjmay.smtp.model.SmtpState;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class SmtpClient implements Initializable {
    public TextField tfUserName;
    public PasswordField tfPassword;
    public TextField tfServer;
    public TextField tfMailFrom;
    public TextField tfRcptTo;
    public TextField tfVrfy;
    public TextArea taLog;
    public Button bConnect;
    public TextField tfHelo;
    public TextField tfEhlo;
    public TextField tfRaw;
    private SmtpState smtpState;
    Thread listeningThread;

    /**
     * Runs when the connect/disconnect button is pressed
     * @param actionEvent Ignored
     */
    public void connect(ActionEvent actionEvent) {
        // see if we should connect or disconnect
        if (bConnect.getText().equals("Connect")) {
            // only try if there is a server here
            if (tfServer.getText().length() > 0) {
                // try to connect to the server
                smtpState = new SmtpState(tfServer.getText(), 25);
                // if we succeeded
                if (smtpState.isConnected()) {
                    taLog.appendText("Connected to " + tfServer.getText() + " on port 25.\n");
                    bConnect.setText("Disconnect");

                    //listen for incoming messages
                    InputStream isr = smtpState.getConnectionInputStream();
                    if (isr != null)
                    {
                        IncomingListener isl = new IncomingListener(isr);
                        listeningThread = new Thread(isl);
                        listeningThread.start();
                        isl.messageProperty().addListener((observableValue, s, t1) -> {
                            synchronized (SharedData.getSB()) {
                                taLog.appendText(SharedData.getSB().toString());
                                SharedData.clearSB();
                            }
                        });
                    }
                } else {
                    taLog.appendText("Error connecting to " + tfServer.getText() + " on port 25.\n");
                    bConnect.setText("Connect");
                }
            }
        } else
        {
            // disconnect
            if (smtpState != null){
                smtpState.close();
                listeningThread.interrupt();
                taLog.appendText("Disconnected from the server.\n");
                bConnect.setText("Connect");
            }
        }
    }

    /**
     * Runs when the helo button is pressed
     * @param actionEvent Ignored
     */
    public void helo(ActionEvent actionEvent) {
        /// TODO: Do the HELO command
        if(smtpState!=null && smtpState.isConnected())
        {
            smtpState.doHelo(tfHelo.getText());
            taLog.appendText("HELO " +tfHelo.getText());
        }

    }

    /**
     * Runs when the ehlo button is pressed
     * @param actionEvent Ignored
     */
    public void ehlo(ActionEvent actionEvent) {
        /// TODO: Do the EHLO command
        if(smtpState!=null && smtpState.isConnected())
        {
            smtpState.doEhlo(tfEhlo.getText());
            taLog.appendText("EHLO " +tfEhlo.getText());
        }

    }

    /**
     * Runs when the mail from button is pressed
     * @param actionEvent Ignored
     */
    public void mailFrom(ActionEvent actionEvent) {
        /// TODO: Do the MAIL FROM command
        if(smtpState!=null && smtpState.isConnected())
        {
            smtpState.doMailFrom(tfMailFrom.getText());
            taLog.appendText("MAIL FROM: " +"<"+tfMailFrom.getText()+">");
        }
    }

    /**
     * Runs when the recipient to button is pressed
     * @param actionEvent Ignored
     */
    public void rcptTo(ActionEvent actionEvent) {
        /// TODO: Do the RCPT TO command
        if(smtpState!=null && smtpState.isConnected())
        {
            smtpState.doRcptTo(tfRcptTo.getText());
            taLog.appendText("RCPT TO: " +"<"+tfRcptTo.getText()+">");
        }
    }

    /**
     * Runs when the data button is pressed
     * @param actionEvent Ignored
     */
    public void data(ActionEvent actionEvent) {
        /// TODO: Do the DATA command
        if(smtpState!=null && smtpState.isConnected())
        {
            smtpState.doData();
            taLog.appendText("DATA \n");
        }
    }

    /**
     * Runs when the verify button is pressed
     * @param actionEvent Ignored
     */
    public void vrfy(ActionEvent actionEvent) {
        /// TODO: Do the VRFY command
        if(smtpState!=null && smtpState.isConnected())
        {
           smtpState.doVrfy(tfVrfy.getText());
            taLog.appendText("VRFY "+tfVrfy.getText() +" \n");
        }
    }

    /**
     * Runs when the quit button is pressed
     * @param actionEvent
     */
    public void quit(ActionEvent actionEvent) {
        /// TODO: Do the QUIT command
        if(smtpState!=null && smtpState.isConnected())
        {
            smtpState.doQuit();
            taLog.appendText("QUIT\n");
        }
    }

    /**
     * Runs when the raw commands button is pressed
     * @param actionEvent
     */
    public void raw(ActionEvent actionEvent) {
        /// TODO: Do a raw command

        if(smtpState!=null && smtpState.isConnected())
        {
            smtpState.doRaw(tfRaw.getText());
            taLog.appendText(tfRaw.getText() + "\n");
        }
    }

    /**
     * Initializes the GUI
     * @param url Ignored
     * @param resourceBundle Ignored
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfUserName.setText("kin103");
        tfPassword.setText("kin101");
        tfServer.setText("12cm.yaweli.com");
        smtpState = null;

    }
}
