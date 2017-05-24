package own.gamelearning.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by 挨踢狗 on 2017/5/2.
 */
public class KeyboardInput implements KeyListener {

    private int[] polled;
    private boolean[] states;

    public KeyboardInput(){
        polled = new int[256];
        states = new boolean[256];
    }

    public synchronized void poll(){
        for (int i = 0;i < polled.length; i++){
            if (states[i]){
                polled[i] ++;
            }else {
                polled[i] = 0;
            }
        }
    }

    public boolean KeyDown(int key){
        return polled[key] > 0;
    }

    public boolean KeyDownOnce(int key){
        return polled[key] == 1;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        states[e.getKeyCode()] = true;
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        states[e.getKeyCode()] = false;
    }
}
