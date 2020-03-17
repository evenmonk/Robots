package gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.*;

import javax.swing.*;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final File file = new File(System.getProperty("user.home") + "/windows.txt");
    private final WindowProperties mainWs = new WindowProperties("Main");

    public MainApplicationFrame() {
        // Make the big window be indented 50 pixels from each edge
        // of the screen.
        final int inset = 50;
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        setContentPane(desktopPane);

        setVisible(true);

        boolean openLogWindow = false;
        boolean openGameWindow = false;

        final LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        final GameWindow gameWindow = new GameWindow();
        addWindow(gameWindow);

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                while (true) {
                    try {
                        final WindowProperties ws = (WindowProperties) ois.readObject();
                        switch (ws.getTitle()) {
                            case "Main": {
                                loadMainFrameState(ws);
                                break;
                            }
                            case "Протокол работы": {
                                openLogWindow = true;
                                loadInternalFrameState(logWindow, ws);
                                break;
                            }
                            case "Игровое поле": {
                                openGameWindow = true;
                                loadInternalFrameState(gameWindow, ws);
                                break;
                            }
                        }
                    } catch (final EOFException e) {
                        break;
                    } catch (final PropertyVetoException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            pack();
            setExtendedState(MAXIMIZED_BOTH);
            openGameWindow = true;
            openLogWindow = true;
            gameWindow.setSize(400, 400);
            logWindow.setLocation(10, 10);
            logWindow.setSize(300, 800);
        }

        try {
            if (!openGameWindow)
                gameWindow.setClosed(true);
            if (!openLogWindow)
                logWindow.setClosed(true);
        } catch (final PropertyVetoException e) {
            e.printStackTrace();
        }

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowIconified(final WindowEvent e) {
                mainWs.setIcon(true);
            }

            @Override
            public void windowDeiconified(final WindowEvent e) {
                mainWs.setIcon(false);
                if (mainWs.isMaximum())
                    setExtendedState(MAXIMIZED_BOTH);
            }

            @Override
            public void windowClosing(final WindowEvent e) {
                closeMainWindow();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                mainWs.setSize(e.getComponent().getSize());
                if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                    mainWs.setMaximum(true);
                }
                if (getExtendedState() == JFrame.NORMAL) {
                    mainWs.setMaximum(false);
                }
            }

            @Override
            public void componentMoved(final ComponentEvent e) {
                mainWs.setLocation(e.getComponent().getLocation());
            }
        });
    }

    private void loadMainFrameState(final WindowProperties ws) {
        if (ws.isIcon()) {
            if (ws.isMaximum())
                mainWs.setMaximum(true);
            setExtendedState(ICONIFIED);
        } else {
            if (ws.isMaximum())
                setExtendedState(MAXIMIZED_BOTH);
            else
                setExtendedState(NORMAL);
        }
        setLocation(ws.getLocation());
        setSize(ws.getSize());
    }

    private void loadInternalFrameState(final JInternalFrame internalFrame, final WindowProperties ws)
            throws PropertyVetoException {
        internalFrame.setLocation(ws.getLocation());
        internalFrame.setSize(ws.getSize());
        if (ws.isIcon())
            internalFrame.setIcon(true);
        else if (ws.isMaximum())
            internalFrame.setMaximum(true);
    }

    protected LogWindow createLogWindow() {
        final LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(final JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private void closeMainWindow() {
        final Object[] options = { "Да", "Нет" };
        final int reply = JOptionPane.showOptionDialog(null, "Вы уверены, что хотите выйти?", "Выход",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (reply == JOptionPane.YES_OPTION) {
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new BufferedOutputStream(new FileOutputStream(file)));) {
                oos.writeObject(mainWs);
                final JInternalFrame[] frames = desktopPane.getAllFrames();
                for (final JInternalFrame internalFrame : frames) {
                    final WindowProperties ws = new WindowProperties(internalFrame.getTitle(),
                            internalFrame.getLocation(), internalFrame.getSize(), internalFrame.isIcon(),
                            internalFrame.isMaximum());
                    oos.writeObject(ws);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
            System.exit(0);
        }

    }

    // protected JMenuBar createMenuBar() {
    // JMenuBar menuBar = new JMenuBar();
    //
    // //Set up the lone menu.
    // JMenu menu = new JMenu("Document");
    // menu.setMnemonic(KeyEvent.VK_D);
    // menuBar.add(menu);
    //
    // //Set up the first menu item.
    // JMenuItem menuItem = new JMenuItem("New");
    // menuItem.setMnemonic(KeyEvent.VK_N);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(
    // KeyEvent.VK_N, ActionEvent.ALT_MASK));
    // menuItem.setActionCommand("new");
    //// menuItem.addActionListener(this);
    // menu.add(menuItem);
    //
    // //Set up the second menu item.
    // menuItem = new JMenuItem("Quit");
    // menuItem.setMnemonic(KeyEvent.VK_Q);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(
    // KeyEvent.VK_Q, ActionEvent.ALT_MASK));
    // menuItem.setActionCommand("quit");
    //// menuItem.addActionListener(this);
    // menu.add(menuItem);
    //
    // return menuBar;
    // }

    private JMenuBar generateMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        final JMenu exitMenu = new JMenu("Меню выхода");

        {
            final JMenuItem exitButton = new JMenuItem("Закрыть приложение");
            exitButton.addActionListener((event) -> closeMainWindow());
            exitMenu.add(exitButton);
        }

        final JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription("Управление режимом отображения приложения");

        {
            final JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            final JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        final JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");

        {
            final JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }

        menuBar.add(exitMenu);
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        return menuBar;
    }

    private void setLookAndFeel(final String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}