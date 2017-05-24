package own.gamelearning.input;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by 挨踢狗 on 2017/4/25.
 */
public class MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener {

    private static final int MOUSE_COUNT = 3;
    private Point mousePos;
    private Point currentPos;
    private boolean[] mouse;
    private int[] polled;
    private int notches;
    private int polledNotches;

    public MouseInput(){
        mousePos = new Point(0, 0);
        currentPos = new Point(0,0);
        mouse = new boolean[MOUSE_COUNT];
        polled = new int[MOUSE_COUNT];
    }

    public synchronized void poll(){
        mousePos = new Point(currentPos);
        polledNotches = notches;
        notches = 0;
        for (int i = 0; i < mouse.length; ++i){
            if (mouse[i]){
                polled[i]++;
            }else {
                polled[i] = 0;
            }
        }
    }

    public Point getPosition(){
        return mousePos;
    }

    public int getNotches(){
        return polledNotches;
    }

    public boolean buttonDown(int button){
        return polled[button - 1] > 0;
    }

    public boolean buttonDownOnce(int button){
        return polled[button - 1] == 1;
    }

    public String getCurrentPos(){
        return "当前X坐标: " + getPosition().getX() + " 当前Y坐标: " + getPosition().getY();
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public synchronized void mousePressed(MouseEvent e) {
        int button = e.getButton() - 1;
        if (button >= 0 && button < mouse.length){
            mouse[button] = true;
        }
    }



    @Override
    public synchronized void mouseReleased(MouseEvent e) {
        int button = e.getButton() - 1;
        if (button >= 0 && button < mouse.length){
            mouse[button] = false;
        }
    }

    @Override
    public synchronized void mouseEntered(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public synchronized void mouseExited(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public synchronized void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public synchronized void mouseMoved(MouseEvent e) {
        currentPos = e.getPoint();
    }

    @Override
    public synchronized void mouseWheelMoved(MouseWheelEvent e) {
        notches += e.getWheelRotation();
    }
}
