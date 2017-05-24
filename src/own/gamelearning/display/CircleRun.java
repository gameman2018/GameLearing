package own.gamelearning.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;


public class CircleRun extends JFrame implements Runnable {

    private static final int SCREEN_W = 600;
    private static final int SCREEN_H = 600;

    private Canvas canvas;
    private Thread gameThread;
    private volatile boolean running;

    private BufferStrategy bs;

    private float curTime;
    private float lastTime;
    private double perTime;

    private float step;
    private float angle;

    public CircleRun(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosing();
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    protected void createAndShowGUI(){
        canvas = new Canvas();
        canvas.setSize(SCREEN_W, SCREEN_H);
        canvas.setIgnoreRepaint(true);
        canvas.setBackground(Color.BLACK);
        getContentPane().add(canvas);

        pack();
        setIgnoreRepaint(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();

        gameThread = new Thread(this);
        gameThread.start();
    }

    private void onWindowClosing(){
        try {
            running = false;
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    @Override
    public void run() {
        running = true;
        initialize();
        while (running){
            updateObjects(perTime / 1.0E9);
            renderFrame(perTime / 1.0E9);
            sleep(10L);
        }
    }

    private void initialize(){
        lastTime = System.nanoTime();
        curTime = System.nanoTime();

        angle = (float) (Math.PI);
        step = (float) (Math.PI / 20.0f);
    }

    private void updateObjects(double delta){
        curTime = System.nanoTime();
        perTime = curTime - lastTime;
        lastTime = curTime;

        angle += step * delta;

        if (angle > Math.PI * 2){
            angle -= Math.PI * 2;
        }
    }

    private void renderFrame(double delta){
        do{
            do {
                Graphics g = null;
                try {
                    g = bs.getDrawGraphics();
                    g.clearRect(0,0,SCREEN_W,SCREEN_H);
                    render(g);
                } finally {
                    if(g != null)
                    g.dispose();
                }
            }while(bs.contentsRestored());
            bs.show();
        }while (bs.contentsLost());
    }

    private void render(Graphics g){
        g.setColor(Color.WHITE);
        int x = canvas.getWidth() / 4;
        int y = canvas.getHeight() / 4;
        int w = x * 2;
        int h = y * 2;
        g.drawOval(x,y,w,h);

        float rw = w / 2;
        float rh = h / 2;

        int rx = (int)(rw * Math.cos(angle));
        int ry = (int)(rh * Math.sin(angle));

        int cx = (rx + w);
        int cy = (ry + h);

        g.drawLine(w,h,cx,cy);

        g.drawRect(cx - 2, cy - 2,4,4);
    }

    private void sleep(long sleep){
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
