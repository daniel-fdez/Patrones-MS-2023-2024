// imports. . .
public class OrderServiceActivator implements javax.jms.MessageListener{ // Queue session and receiver: see JMS API for details
    private QueueSession orderQueueSession;
    private QueueReceiver orderQueueReceiver;
    // Note: values should come from property files or
    // environment. Hard coded here for illustration only
    private String connectionFactoryName = "PendingOrdersQueueFactory";
    private String queueName = "PendingOrders";
    // use a service locator to locate administered JMS
    // components such as a Queue or a Queue Connection
    // factory
    private ServiceLocator serviceLocator;
    private QueueConnectionFactory queueConnectionFactory;
    private QueueConnection queueConnection;
    public OrderServiceActivator(String connectionFactoryName, String queueName) {
        super();
        this.connectionFactoryName = connectionFactoryName;this.queueName = queueName;
        startListener();
    }
    private void startListener() {
        try {
            serviceLocator = ServiceLocator.getInstance();
            queueConnectionFactory =
            serviceLocator.getQueueConnectionFactory(connectionFactoryName);queueConnection = queueConnectionFactory.createQueueConnection();
            // See JMS API for method usage and arguments
            orderQueueSession = queueConnection.createQueueSession (...);Queue ordersQueue = serviceLocator.getQueue(queueName);
            orderQueueReceiver = orderQueueSession.createReceiver(ordersQueue);orderQueueReceiver.setMessageListener(this);
        }
        catch (JMSException excp) {
            // handle error
        }
    }
    // The JMS API specifies the onMessage method in the
    // javax.jms.MessageListener interface.
    // This method is asynchronously invoked
    // when a message arrives on the Queue being
    // listened to by the ServiceActivator.
    // See JMS Specification and API for more details.
    public void onMessage(Message message) {
        try {
            // parse Message msg. See JMS API for Message.
            // Get the order object from the message
            ObjectMessage objectMessage = (javax.jms.ObjectMessage) message;Order order = (Order) objectMessage.getObject();
            // Use the Business Delegate for Order Processor
            // Session Façade to invoke order processing
            OrderProcessorDelegate orderProcessorBD = new OrderProcessorDelegate();orderProcessorBD.processOrder(order);
            // send any response here...
        }
        catch (JMSException jmsexcp) {
            // Handle JMSExceptions, if any
            // Send error response, throw runtime exception
        } 
        catch (Exception excp) {
            // Handle any other exceptions
            // Send error response, throw runtime exception
        } 
    }
    public void close() {
        try { // cleanup before closing
            orderQueueReceiver.setMessageListener (null);
            orderQueueSession.close();
        }
        catch(Exception excp) {
        // Handle exception - Failure to close
        }
    } 
}

// imports...
public class OrderSenderAppService {
    // Queue session and sender: see JMS API for details
    private QueueSession orderQueueSession;
    private QueueSender orderQueueSender;
    // These values could come from some property files
    private String connectionFactoryName = "PendingOrdersQueueFactory";
    private String queueName = "PendingOrders";
    // use a service locator to locate administered
    // JMS components such as a Queue or a Queue.
    // Connection factory
    private JMSServiceLocator serviceLocator;
    // . . .
    // method to initialize and create queue sender
    private void createSender() {
        try {
            // using ServiceLocator and getting Queue
            // Connection Factory is similar to the
            // Service Activator code.
            serviceLocator = ServiceLocator.getInstance();
            queueConnectionFactory = serviceLocator.getQueueConnectionFactory(connectionFactoryName);queueConnection = queueConnectionFactory.createQueueConnection();// See JMS API for method usage and arguments
            orderQueueSession = queueConnection.createQueueSession(/*. . .*/);
            Queue ordersQueue = serviceLocator.getQueue(queueName);
            orderQueueSender = orderQueueSession.createSender(ordersQueue);
        }
        catch(Exception excp) {
            // Handle exception - Failure to create sender
        }
    }
    // method to dispatch order to fulfillment service
    // for asynchronous processing
    public void sendOrder(Order order) {
        try {
            // create a new Message to send Order object
            ObjectMessage orderObjectMessage = queueSession.createObjectMessage(order);
            // set object message properties and delivery
            // mode as required.
            // See JMS API for ObjectMessage
            // Set the Order into the object message
            orderObjectMessage.setObject(order);
            // send the message to the Queue
            orderQueueSender.send(orderObjectMessage);
            // . . .
        } catch (Exception e) {
            // Handle exceptions 
        }
        // . . .
    }
    // . . .
    public void close() {
        try {
            // cleanup before closing
            orderQueueReceiver.setMessageListener(null); orderQueueSession.close();
        }
        catch(Exception excp) {
            // Handle exception - Failure to close
        }
    }
}
