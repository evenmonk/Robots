package log;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class LogWindowSource {

    private final LoggingStructure messages;
    private final List<LogChangeListener> listeners;
    private volatile LogChangeListener[] m_activeListeners;

    public LogWindowSource(int queueLength) {
        messages = new LoggingStructure(queueLength);
        listeners = new CopyOnWriteArrayList<>();
    }

    public void registerListener(LogChangeListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(LogChangeListener listener) {
        listeners.remove(listener);
    }

    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        try {
            messages.add(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogChangeListener [] activeListeners = m_activeListeners;

        if (activeListeners == null)
        {
            synchronized (listeners)
            {
                if (activeListeners == null)
                {
                    activeListeners = listeners.toArray(new LogChangeListener [0]);
                    activeListeners = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : activeListeners)
        {
            listener.onLogChanged();
        }
    }

    public int size() {
        return messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= messages.size()) {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, messages.size());
        return messages.subList(startFrom, indexTo);
    }

    public Iterable<LogEntry> all() {
        return messages;
    }
}