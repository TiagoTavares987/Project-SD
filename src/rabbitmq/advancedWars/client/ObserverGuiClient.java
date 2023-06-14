/**
 * <p>
 * Title: Projecto SD</p>
 * <p>
 * Description: Projecto apoio aulas SD</p>
 * <p>
 * Copyright: Copyright (c) 2011</p>
 * <p>
 * Company: UFP </p>
 *
 * @author Rui Moreira
 * @version 2.0
 */
package rabbitmq.advancedWars.client;

import com.rabbitmq.client.BuiltinExchangeType;
import rabbitmq.advancedWars.client.game.engine.Game;
import rabbitmq.advancedWars.util.RabbitUtils;


import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author rjm
 */
public class ObserverGuiClient {

    private Observer observer;

    /**
     * Creates new form ChatClientFrame
     *
     * @param args
     */
    public ObserverGuiClient(String args[]) {

            Logger.getLogger(this.getClass().getName()).log(Level.INFO, " After initComponents()...");

            RabbitUtils.printArgs(args);

            //Read args passed via shell command
            String host=args[0];
            int port=Integer.parseInt(args[1]);
            String exchangeName=args[2];
            //String room=args[3];
            int ply=Integer.parseInt(args[3]);
            String mapLv = args[4];
            String gameId = args[5];
            String createOrJoin = args[6];


            Thread thread = new Thread(){
                public void run(){
                    Game game = new Game(mapLv, ply);
                    //2. Create the _05_observer object that manages send/receive of messages to/from rabbitmq
                    try {
                        observer = new Observer(game, host, port, "guest", "guest", ply, mapLv, exchangeName, BuiltinExchangeType.TOPIC, "UTF-8", gameId, createOrJoin);
                    } catch (IOException | TimeoutException e) {
                        e.printStackTrace();
                    }
                    Logger.getLogger(this.getClass().getName()).log(Level.INFO, " After initObserver()...");
                    game.setObserver(observer);

                    game.GameLoop();
                }
            };
            thread.start();

            Logger.getLogger(this.getClass().getName()).log(Level.INFO, " After initObserver()...");

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int expectedArgs = 4;
                if (args.length >= expectedArgs) {
                    new ObserverGuiClient(args);
                } else {
                    Logger.getLogger(ObserverGuiClient.class.getName()).log(Level.INFO, "check args.length < "+expectedArgs+"!!!" );
                }
            }
        });
    }
}
