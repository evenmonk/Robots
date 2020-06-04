package log;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LoggingStructure implements Iterable<LogEntry>
{
    private Node head;
    private Node tall;
    private final int maxSize;
    private int localSize = 0;
    private boolean isIterating = false;

    public LoggingStructure(int size)
    {
        maxSize = size;
    }

    public synchronized void add(LogEntry message) throws Exception
    {
        if (isIterating) throw new Exception();
        Node newHead = new Node(message);
        if(head != null)
        {
            newHead.setNext(head);
            head.setPrevious(newHead);
        }
        head = newHead;
        if (tall == null)
            tall = head;
        if(localSize == maxSize)
        {
            Node newTall = tall.previous();
            newTall.setNext(null);
            tall = newTall;
        }
        else
            localSize++;
    }

    public int size()
    {
        return localSize;
    }

    @Override
    public Iterator<LogEntry> iterator() {
        return new Iterator<LogEntry>() {
            private Node next = head;
            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public LogEntry next() {
                if(hasNext())
                {
                    Node result = next;
                    next = next.next();
                    return result.message();
                }
                else throw new NoSuchElementException();
            }

        };
    }

    public synchronized Iterable<LogEntry> subList(int startFrom, int indexTo) {
        isIterating = true;
        Iterator<LogEntry> iterator = new Iterator<>() {
            private int localIndex = 0;
            private Node localHead = head;

            @Override
            public boolean hasNext() {
                while (localIndex < startFrom) {
                    localHead = localHead.next();
                    localIndex++;
                }
                boolean hasNext = localHead.next() != null && localIndex < indexTo;
                if (hasNext)
                    return true;
                else {
                    isIterating = false;
                    return false;
                }
            }

            @Override
            public LogEntry next() {
                if (this.hasNext()) {
                    Node result = localHead;
                    localHead = head.next();
                    localIndex++;
                    return result.message();
                } else
                    throw new NoSuchElementException();
            }
        };

        return new Iterable<LogEntry>() {

            @Override
            public Iterator<LogEntry> iterator() {
                return iterator;
            }

        };
    }
}
