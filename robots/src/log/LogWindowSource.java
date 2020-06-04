package log;

import java.util.Collections;
import java.util.ArrayList;


public class LogWindowSource {
    private int m_iQueueLength;

    private final LoggingStructure m_messages;
    //private final ArrayList<LogChangeListener> m_listeners;
    private ArrayList<LogChangeListener> m_activeListeners;

    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_messages = new LoggingStructure(iQueueLength);
        m_activeListeners = new ArrayList<LogChangeListener>();
    }

    public void registerListener(LogChangeListener listener) {
        synchronized(m_activeListeners)
        {
            m_activeListeners.add(listener);
        }
    }

    public void unregisterListener(LogChangeListener listener) {
        synchronized(m_activeListeners)
        {
            m_activeListeners.remove(listener);
        }
    }

    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        try {
            m_messages.add(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LogChangeListener [] activeListeners = m_activeListeners;
//
//        if (activeListeners == null)
//        {
//            synchronized (m_listeners)
//            {
//                if (m_activeListeners == null)
//                {
//                    activeListeners = m_listeners.toArray(new LogChangeListener [0]);
//                    m_activeListeners = activeListeners;
//                }
//            }
//        }
        for (LogChangeListener listener : m_activeListeners)
        {
            listener.onLogChanged();
        }
    }

    public int size() {
        return m_messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= m_messages.size()) {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_messages.size());
        return m_messages.subList(startFrom, indexTo);
    }

    public Iterable<LogEntry> all() {
        return m_messages;
    }
}