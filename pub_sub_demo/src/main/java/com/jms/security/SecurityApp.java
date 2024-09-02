package com.jms.security;

import com.jms.hr.model.Employee;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class SecurityApp {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Topic topic = (Topic) initialContext.lookup("topic/empTopic");
        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {

            // Durabale Consumer
            //            JMSConsumer consumer = jmsContext.createConsumer(topic);
            jmsContext.setClientID("my_client_123");
            JMSConsumer consumer = jmsContext.createDurableConsumer(topic, "subscription1");
            consumer.close();

            Thread.sleep(10000);
            consumer = jmsContext.createDurableConsumer(topic, "subscription1");

            Message message = consumer.receive();
            Employee employee = message.getBody(Employee.class);
            System.out.println("Employee received in Security App: " + employee);
        }
    }
}
