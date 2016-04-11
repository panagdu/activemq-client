package uk.panasys.activemq;

public class QueueSizeTuple {
    private String queueName;
    private Integer size;

    public QueueSizeTuple(String queueName, Integer size) {
        this.queueName = queueName;
        this.size = size;
    }

    @Override
    public String toString() {
        return "{" +
                "queueName='" + queueName + '\'' +
                ", size=" + size +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QueueSizeTuple)) {
            return false;
        }

        QueueSizeTuple that = (QueueSizeTuple) o;

        if (!queueName.equals(that.queueName)) {
            return false;
        }
        return size.equals(that.size);

    }

    @Override
    public int hashCode() {
        int result = queueName.hashCode();
        result = 31 * result + size.hashCode();
        return result;
    }
}
