package com.manageaccount.manageaccount.config;

import com.manageaccount.manageaccount.service.NotificationService;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.annotation.EnableJms;
import jakarta.jms.ConnectionFactory; // Sử dụng jakarta.jms thay vì javax.jms

@Configuration
@EnableJms
public class JmsConfig {

    // Cấu hình connection factory kết nối tới ActiveMQ
    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("tcp://localhost:61616"); // URL của ActiveMQ broker
        return connectionFactory; // Không cần ép kiểu cast, vì chúng đã cùng loại ConnectionFactory
    }

    // Cấu hình JmsTemplate để gửi message vào ActiveMQ
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setDefaultDestinationName("payment.queue"); // Tên của Queue
        return jmsTemplate;
    }


    // Cấu hình listener container để lắng nghe message từ Queue
    @Bean
    public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory, NotificationService notificationService) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setDestinationName("payment.queue"); // Tên của Queue mà NotificationService sẽ lắng nghe
        MessageListenerAdapter adapter = new MessageListenerAdapter(notificationService);
        adapter.setDefaultListenerMethod("receivePayment"); // Đặt phương thức xử lý tin nhắn
        container.setMessageListener(adapter);
        return container;
    }
}
