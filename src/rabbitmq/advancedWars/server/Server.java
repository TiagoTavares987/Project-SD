package rabbitmq.advancedWars.server;

import com.rabbitmq.client.*;
import rabbitmq.advancedWars.util.RabbitUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    //Reference for rabbitmq.advancedWars.client.game.gui
    private ObserverServer gui;
    //Preferences for exchange...
    private final Channel channelToRabbitMq;
    private final String exchangeName;
    private final BuiltinExchangeType exchangeType;
    //private final String[] exchangeBindingKeys;
    private final String exchangeBindingKeys;
    private final String messageFormat;
    private final HashMap<String, Integer> waitingList = new HashMap<>(); // identificador do jogo e nr jogadores na lista de espera

    public Server(ObserverServer gui, String host, int port, String user, String pass, String exchangeName, BuiltinExchangeType exchangeType, String messageFormat, String bindingKeys) throws IOException, TimeoutException {

        this.gui = gui;
        this.exchangeName = exchangeName;
        this.exchangeType = exchangeType;

        Connection connection = RabbitUtils.newConnection2Server(host, port, user, pass);
        this.channelToRabbitMq = RabbitUtils.createChannel2Server(connection);

        this.messageFormat = messageFormat;

        this.exchangeBindingKeys = bindingKeys;
        bindExchangeToChannelRabbitMQ();
        attachConsumerToChannelExchangeWithKey();
    }

    private void bindExchangeToChannelRabbitMQ() throws IOException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Declaring Exchange '" + this.exchangeName + "' with type " + this.exchangeType);
        this.channelToRabbitMq.exchangeDeclare(exchangeName, exchangeType);
    }

    /**
     * Creates a Consumer associated with an unnamed queue.
     */
    public void attachConsumerToChannelExchangeWithKey() {
        try {
            String queue = this.channelToRabbitMq.queueDeclare().getQueue();

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            channelToRabbitMq.confirmSelect();
            System.out.println("confirm");
            channelToRabbitMq.queueBind(queue, exchangeName, "server.*");
            System.out.println("queuebind");
            channelToRabbitMq.waitForConfirms();
            System.out.println("waitconfirm");

            // recebe mensagem
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), messageFormat);
                String routingKey = delivery.getEnvelope().getRoutingKey();
                System.out.println("received message " + message + " from routing key " + routingKey);

                if(routingKey.equals("server.create")) {
                    System.out.println("received message " + message + " from routing key server.create");
                    String game[] = message.split("-");
                    waitingList.put(game[0], Integer.valueOf(game[1])-1);
                }
                else if(routingKey.equals("server.join")) {
                    System.out.println("received message " + message + " from routing key server.join");
                    String game[] = message.split("-");
                    waitingList.put(game[0], waitingList.get(game[0])-1);
                }
                else {
                    // quando é msg do game, envia de volta para os varios clientes
                    String gameId = routingKey.split("\\.")[1];
                    setReceivedMessage(message, gameId);
                }
                System.out.println(" [x] Consumer Tag [" + consumerTag + "] - Received '" + message + "'");
            };
            CancelCallback cancelCallback = consumerTag -> System.out.println(" [x] Consumer Tag [" + consumerTag + "] - Cancel Callback invoked!");

            this.channelToRabbitMq.basicConsume(queue, true, deliverCallback, cancelCallback);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Publish messages to existing exchange instead of the nameless one.
     * - The routingKey is empty ("") since the fanout exchange ignores it.
     * - Messages will be lost if no queue is bound to the exchange yet.
     * - Basic properties can be: MessageProperties.PERSISTENT_TEXT_PLAIN, etc.
     */
    public void sendMessage(String msgToSend, String routingKey) throws IOException {
        channelToRabbitMq.basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, msgToSend.getBytes(messageFormat));
    }


    /**
     * @param receivedMessage the received message to set
     */
    public void setReceivedMessage(String receivedMessage, String gameId) throws IOException {

        if(waitingList.get(gameId) > 0)
            return;

        this.sendMessage(receivedMessage, "client." + gameId); // propaga as msg para clientes todos
    }

}
