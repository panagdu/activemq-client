package uk.panasys.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ActivemqClientFT {
    private ActiveMQConnection connection;

    @Before
    public void setUp() throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("failover:(tcp://localhost:61616)?randomize=false");
        connection = (ActiveMQConnection) connectionFactory.createConnection("user", "pass");
        connection.start();
    }

    @Test
    public void testGetQueuesStatistics() throws Exception {
        ActivemqClient client = new ActivemqClient(connection);

        List<QueueSizeTuple> queuesStatistics = client.getQueuesStatistics();

        assertThat(queuesStatistics.isEmpty(), is(false));
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }
}