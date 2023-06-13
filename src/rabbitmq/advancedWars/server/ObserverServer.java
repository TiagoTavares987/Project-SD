package rabbitmq.advancedWars.server;

import com.rabbitmq.client.BuiltinExchangeType;
import rabbitmq.advancedWars.util.RabbitUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ObserverServer {

    /**
     * Creates new form ChatClientFrame
     *
     * @param args
     */
    public ObserverServer(String args[]) throws IOException, TimeoutException {

        //1. Init the GUI components

        Logger.getLogger(this.getClass().getName()).log(Level.INFO, " After initComponents()...");

        RabbitUtils.printArgs(args);

        //Read args passed via shell command
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String exchangeName = args[2];

        new Server(this, host, port, "guest", "guest", exchangeName, BuiltinExchangeType.TOPIC, "UTF-8", "");
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, " After initObserver()...");


    }

    //================================================ BEGIN TO CHANGE ================================================

    /**
     * Sends msg through the _05_observer to the exchange where all observers are binded
     *
     * @param msgToSend
     */
/*    private void sendMsg(String user, String msgToSend) {
        try {
            msgToSend = "[" + user + "]: " + msgToSend;
            this.server.sendMessage(msgToSend);
        } catch (IOException ex) {
            Logger.getLogger(ObserverServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    //================================================ END TO CHANGE ================================================


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException, TimeoutException {

        new ObserverServer(args);

    }
}

