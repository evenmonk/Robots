package log;

public class Node {
    private LogEntry message;
    private Node next;
    private Node previous;

    public Node(LogEntry message)
    {
        this.message = message;
    }

    public void setNext(Node next)
    {
        this.next = next;
    }

    public void setPrevious(Node prev)
    {
        previous = prev;
    }

    public LogEntry message()
    {
        return message;
    }

    public Node next()
    {
        return next;
    }

    public Node previous()
    {
        return previous;
    }
}
