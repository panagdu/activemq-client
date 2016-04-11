package uk.panasys.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.jms.*;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class ActivemqClientTest {

    public static final String TEST_QUEUE = "Q.TEST";
    private ActiveMQConnection connection;
    private ActivemqClient client;
    private Session session;

    @Rule
    public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

    @Before
    public void setUp() throws Exception {
        connection = (ActiveMQConnection) broker.createConnectionFactory().createConnection();
        connection.start();
        session= connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        client = new ActivemqClient(connection);
    }

    @Test
    public void testGetQueuesStatistics() throws Exception {
        //given
        Destination destination = session.createQueue(TEST_QUEUE);
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        String text = "Testing ActivemqClient" + Thread.currentThread().getName() + " : " + this.hashCode();
        TextMessage message = session.createTextMessage(text);
        producer.send(message);

        //when
        List<QueueSizeTuple> queuesStatistics = client.getQueuesStatistics();

        //then
        assertThat(queuesStatistics, hasItem(new QueueSizeTuple(TEST_QUEUE, 1)));
    }

    @After
    public void tearDown() throws Exception {
        session.close();
        connection.close();
    }
}