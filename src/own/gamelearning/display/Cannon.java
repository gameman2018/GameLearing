package own.gamelearning.display;

import own.gamelearning.input.KeyboardInput;
import own.gamelearning.object.Vector2f;
import own.gamelearning.support.Matrix3x3f;
import own.gamelearning.system.RateObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;


public class Cannon extends JFrame implements Runnable {

    private Canvas canvas;
    private int WIDTH = 680;
    private int HEIGHT = 480;
    private volatile boolean running;
    private Thread gameThread;
    private RateObserver rate;
    private BufferStrategy bs;

    private float worldWidth;
    private float worldHeight;

    private Vector2f[] cannon;
    private Vector2f[] cannonCpy;
    private float cannonDelta,cannonRot;

    private Vector2f bullet;
    private Vector2f bulletCpy;
    private Vector2f velicty;

    private KeyboardInput keyboard;

    public Cannon(){
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
        canvas.setSize(WIDTH,HEIGHT);
        canvas.setIgnoreRepaint(true);
        canvas.setBackground(Color.WHITE);
        getContentPane().add(canvas);

        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIgnoreRepaint(true);
        keyboard = new KeyboardInput();
        canvas.addKeyListener(keyboard);
        setVisible(true);

        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
        canvas.requestFocus();

        gameThread = new Thread(this);
        gameThread.start();
    }

    private void initialize(){
        rate = new RateObserver();
        rate.initialize();

        worldWidth = 5.0f;
        worldHeight = 5.0f;

        cannonRot = 0.0f;
        cannonDelta = (float) Math.toRadians(90.0f);

        bullet = null;

        cannon = new Vector2f[]{
                new Vector2f(-0.5f,0.125f),
                new Vector2f(0.5f,0.125f),
                new Vector2f(0.5f,-0.125f),
                new Vector2f(-0.5f,-0.125f)
        };
        cannonCpy = new Vector2f[cannon.length];
    }
    @Override
    public void run() {
        running = true;
        initialize();
        long cur = System.nanoTime();
        long last = cur;
        double delta;
        while (running){
            cur = System.nanoTime();
            delta = cur - last;
            gameLoop(delta / 1.0E9);
            last = cur;
        }
    }
    private void gameLoop(double delta){
        processInput(delta);
        updateObjects(delta);
        frameRender(delta);
        sleep(10L);
    }

    private void processInput(double delta){
        keyboard.poll();

        if (keyboard.KeyDown(KeyEvent.VK_UP)){
            cannonRot += delta * cannonDelta;
        }

        if (keyboard.KeyDown(KeyEvent.VK_DOWN)){
            cannonRot -= delta * cannonDelta;
        }

        if (keyboard.KeyDownOnce(KeyEvent.VK_SPACE)){
            Matrix3x3f matrix3x3f = Matrix3x3f.translate(6.0f,0.0f);
            matrix3x3f = matrix3x3f.mul(Matrix3x3f.rotate(cannonRot));
            velicty = matrix3x3f.mul(new Vector2f());

            matrix3x3f = Matrix3x3f.translate(0.5f,0.0f);
            matrix3x3f = matrix3x3f.mul(Matrix3x3f.rotate(cannonRot));
            matrix3x3f = matrix3x3f.mul(Matrix3x3f.translate(-2.0f,-2.0f));
            bullet = matrix3x3f.mul(new Vector2f());
        }
    }
    private void updateObjects(double delta){
        Matrix3x3f matrix3x3f = Matrix3x3f.identity();
        matrix3x3f = matrix3x3f.mul(Matrix3x3f.rotate(cannonRot));
        matrix3x3f = matrix3x3f.mul(Matrix3x3f.translate(-2.0f,-2.0f));


        for (int i = 0; i < cannon.length;i++){
            cannonCpy[i] = matrix3x3f.mul(cannon[i]);
        }

        if (bullet != null){
            velicty.y += -9.8f * (float) delta;
            bullet.x += velicty.x * delta;
            bullet.y += velicty.y * delta;
            bulletCpy = new Vector2f(bullet);

            if (bullet.y <= -2.5f){
                bullet = null;
            }
        }
    }
    private void frameRender(double delta) {
        do {
            do {
                Graphics g = null;
                try {
                    g = bs.getDrawGraphics();
                    g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    render(g);
                } finally {
                    g.dispose();
                }
            } while (bs.contentsRestored());
            bs.show();
        } while (bs.contentsLost());
    }

    private void render(Graphics g){
        rate.calculate();
        g.setColor(Color.BLACK);
        g.drawString(rate.getRate(),20,20);


        float sx = (canvas.getWidth() - 1) / worldWidth;
        float sy = (canvas.getHeight() - 1) / worldHeight;
        float tx = (canvas.getWidth() - 1) / 2.0f;
        float ty = (canvas.getHeight() - 1) / 2.0f;
        Matrix3x3f viewport = Matrix3x3f.scale(sx,-sy);
        viewport = viewport.mul(Matrix3x3f.translate(tx,ty));

        for (int i = 0; i < cannon.length; i++){
            cannonCpy[i] = viewport.mul(cannonCpy[i]);
        }
        drawPloyGon(g,cannonCpy);

        if (bullet != null){
            bulletCpy = viewport.mul(bulletCpy);
            g.drawOval((int) bulletCpy.x - 5,(int)bulletCpy.y - 5,10,10);
        }

    }
    private void drawPloyGon(Graphics g,Vector2f[] vector2fs){
        Vector2f S = null;
        Vector2f E = vector2fs[vector2fs.length - 1];
        for (int i = 0; i < vector2fs.length;i++){
            S = vector2fs[i];
            g.drawLine((int)S.x,(int)S.y,(int)E.x,(int)E.y);
            E = S;
        }
    }
    private void sleep(long sleep){
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void onWindowClosing(){
        try {
            running = false;
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
