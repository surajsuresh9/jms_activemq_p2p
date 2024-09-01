package jms.hm.clinicals;

import jms.hm.model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class ClinicalApp {

    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/requestQueue");
        Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext();) {

            JMSProducer producer = jmsContext.createProducer();
            ObjectMessage objectMessage = jmsContext.createObjectMessage();
            objectMessage.setObject(new Patient(123, "Bob", "Blue Cross Ble Shield", 30d, 50d));
            producer.send(queue, objectMessage);

            JMSConsumer consumer = jmsContext.createConsumer(replyQueue);
            MapMessage responseMessage = (MapMessage) consumer.receive(30000);
            System.out.println("Patient eligibility: " + responseMessage.getBoolean("eligible"));

        }
    }
}
