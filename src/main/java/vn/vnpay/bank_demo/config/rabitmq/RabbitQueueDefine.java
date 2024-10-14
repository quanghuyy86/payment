package vn.vnpay.bank_demo.config.rabitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitQueueDefine {
//    private final AmqpAdmin rabbitAdminMain;
//
//    @Bean
//    public Queue incomingQueue() {
//        //Define queue job
//        for (String queueName : JobQueue.queueNameList) {
//            Queue queue = new Queue(queueName, true, false, false, null);
//            rabbitAdminMain.declareQueue(queue);
//        }
//        return null;
//    }
}
