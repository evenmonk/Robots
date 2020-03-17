package gui;

import java.awt.*;
import java.io.Serializable;

public class WindowProperties implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String title;
    private Point location;
    private Dimension size;
    private boolean isIcon;
    private boolean isMaximum;

    public WindowProperties(String title){
        this.title = title;
    }

    public WindowProperties(String title, Point location, Dimension size, boolean isIcon, boolean isMaximum) {
        this.title = title;
        this.location = location;
        this.size = size;
        this.isIcon = isIcon;
        this.isMaximum = isMaximum;
    }

    public String getTitle() {
        return title;
    }

    public Point getLocation() {
        return location;
    }

    public Dimension getSize() {
        return size;
    }

    public boolean isIcon() {
        return isIcon;
    }

    public boolean isMaximum() {
        return isMaximum;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    public void setIcon(boolean icon) {
        isIcon = icon;
    }

    public void setMaximum(boolean maximum) {
        isMaximum = maximum;
    }
}
