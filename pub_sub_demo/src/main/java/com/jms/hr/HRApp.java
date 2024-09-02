package com.jms.hr;

import com.jms.hr.model.Employee;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class HRApp {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Topic topic = (Topic) initialContext.lookup("topic/empTopic");
        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {
            Employee e = new Employee(123, "John", "Doe", "jdoe@gmail.com", "SSE", "4455667789");
            jmsContext.createProducer().send(topic, e);
            System.out.println("Message sent");
        }
    }
}
