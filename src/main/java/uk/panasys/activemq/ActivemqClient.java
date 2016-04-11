package uk.panasys.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ActivemqClient {

    private ActiveMQConnection connection;

    public ActivemqClient(ActiveMQConnection amqConnection) throws JMSException {
        this.connection = amqConnection;
    }

    public List<QueueSizeTuple> getQueuesStatistics() throws JMSException {
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Set<ActiveMQQueue> amqs = connection.getDestinationSource().getQueues();
        List<QueueSizeTuple> queueSizeTupleList = amqs.stream()
                .map(amq -> new QueueSizeTuple(amq.getPhysicalName(), getNumberOfMessage(session, amq)))
                .collect(Collectors.toList());
        session.close();
        return queueSizeTupleList;
    }

    private int getNumberOfMessage(Session session, ActiveMQQueue queue) {
        int numMsgs = 0;
        try {
            QueueBrowser queueBrowser = session.createBrowser(queue);
            Enumeration e = queueBrowser.getEnumeration();
            while (e.hasMoreElements()) {
                e.nextElement();
                numMsgs++;
            }
            queueBrowser.close();

        } catch (JMSException e) {
            throw new IllegalArgumentException("Could not get the size of the queue", e);
        }
        return numMsgs;
    }
}
