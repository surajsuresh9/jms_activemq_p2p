package jms.hm.eligibilityCheck;

import jms.hm.model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class EligibilityCheckListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        System.out.println("Message Received");
        ObjectMessage objectMessage = (ObjectMessage) message;
        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(); JMSContext jmsContext = cf.createContext();) {
            InitialContext initialContext = new InitialContext();
            Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
            MapMessage replyMessage = jmsContext.createMapMessage();
            Patient patient = (Patient) objectMessage.getObject();
            String insuranceProvider = patient.getInsuranceProvider();
            System.out.println("insuranceProvider: " + insuranceProvider);
            if (insuranceProvider.equals("Blue Cross Blue Shield") || insuranceProvider.equals("United Health")) {
                if (patient.getCopay() < 40 && patient.getAmountToBePayed() < 1000) {
                    replyMessage.setBoolean("eligible", true);
                } else {
                    replyMessage.setBoolean("eligible", false);
                }
            }
            jmsContext.createProducer().send(replyQueue, replyMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
