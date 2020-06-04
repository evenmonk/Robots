package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import log.LogChangeListener;
import log.LogWindowSource;
import log.LogEntry;

public class LogWindow extends JInternalFrame implements LogChangeListener, Disposable
{
    private LogWindowSource logSource;
    private TextArea logContent;

    public LogWindow(LogWindowSource logSource) {
        super("Протокол работы", true, true, true, true);
        this.logSource = logSource;
        this.logSource.registerListener(this);
        logContent = new TextArea("");
        logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        logSource.all().forEach(entry -> content.append(entry.getMessage()).append("\n"));
        logContent.setText(content.toString());
        logContent.invalidate();
    }
    
    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }

//    public FrameInfo getInfo() {
//        return new FrameInfo(getWidth(), getHeight(), getLocation(), isMaximum(), isIcon());
//    }

    @Override
    public void onDispose() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}