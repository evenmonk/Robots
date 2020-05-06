package gui;

import java.awt.BorderLayout;
import utils.Game;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame implements Disposable
{    
    public GameWindow(Game game)
    {
        super("Игровое поле", true, true, true, true);
        getContentPane().add(new GameVisualizer(game), BorderLayout.CENTER);
        pack();
    }

    @Override
    public void onDispose() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
