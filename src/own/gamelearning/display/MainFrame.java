package own.gamelearning.display;

import own.gamelearning.input.MouseInput;
import own.gamelearning.object.Vector2f;
import own.gamelearning.system.RateObserver;
import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.concurrent.Callable;

public class MainFrame extends JFrame implements Runnable{

    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;

    private RateObserver rate;

    private Canvas canvas;
    private BufferStrategy bs;

    private Thread gameThread;
    private volatile boolean running;

    private Vector2f[] vector2fs;
    private Vector2f[] vector2fsTmp;

    private float tx, ty, speed, targetx,targety;
    private float vector_tx,vector_ty;

    private boolean doTranslate;

    private MouseInput mouse;

    public MainFrame(){
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
        canvas.setBackground(Color.BLACK);
        canvas.setSize(WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);
        pack();
        getContentPane().add(canvas);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        canvas.requestFocus();
        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();

        mouse = new MouseInput();
        canvas.addMouseListener(mouse);
        canvas.addMouseMotionListener(mouse);
        canvas.addMouseWheelListener(mouse);

        initialize();

        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public void run() {
        running = true;

        while (running){
            gameLoop();
        }
    }

    private void gameLoop(){
        processInput();
        renderFrame();
        sleep(10L);
    }

    private void processInput(){
        mouse.poll();
//        for (int i = 0; i < vector2fs.length; ++i){
//            vector2fsTmp[i] = new Vector2f(vector2fs[i]);
//        }
        if (mouse.buttonDown(MouseEvent.BUTTON1)){
            float tx = 0;
            float ty = 0;
            for (int i = 0; i < vector2fs.length; ++i){
                if (i == 0){
                    tx = mouse.getPosition().x - vector2fs[i].x;
                    ty = mouse.getPosition().y - vector2fs[i].y;
                }
                vector2fs[i].Translate(tx,ty);
            }
        }

        if (mouse.buttonDownOnce(MouseEvent.BUTTON3)){
            doTranslate = true;
            targetx = mouse.getPosition().x;
            targety = mouse.getPosition().y;
        }

        if (doTranslate){
            speed = 1.0f;
            tx = targetx - vector2fs[0].x;
            ty = targety - vector2fs[0].y;


            float flag = 1;
            float fsa = 1;
            if (tx < 0){
                    flag = -1;
            }
            if(ty < 0){
                fsa = -1;
            }


            for (int i = 0; i < vector2fs.length; ++i){
                vector2fs[i].Translate(speed * flag,speed * fsa);
            }

            if (tx == 0 && ty == 0){
                doTranslate = false;
            }
        }
    }

    private void renderFrame(){
        do{
            do{
                Graphics g = null;
                try {
                    g = bs.getDrawGraphics();
                    g.clearRect(0,0, WIDTH,  HEIGHT);
                    render(g);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    g.dispose();
                }
            }while (bs.contentsRestored());
            bs.show();
        }while (bs.contentsLost());
    }

    private void sleep(long sleep){
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initialize(){
        rate = new RateObserver();
        rate.initialize();
        vector2fs = new Vector2f[]{
                new Vector2f(20, 20),
                new Vector2f(20, 80),
                new Vector2f(80, 20),
                new Vector2f(80, 80)
        };
        vector2fsTmp = new Vector2f[vector2fs.length];
        reset();
    }

    private void reset(){
        tx = WIDTH / 2;
        ty = HEIGHT / 2;

        vector_tx = 0.1f;
        vector_ty = 0.1f;

        for (int i = 0; i < vector2fs.length; ++i){
            vector2fs[i].Translate(tx,ty);
        }
    }

    private void render(Graphics g){
        rate.calculate();
        g.setColor(Color.WHITE);
        g.drawString(rate.getRate(),100,100);
        g.drawString(mouse.getCurrentPos(),100 ,120);

        g.setColor(Color.CYAN);


        Vector2f S = null;
        Vector2f E = vector2fs[vector2fs.length - 1];
        for (int i = 0; i < vector2fs.length; ++i){
            S = vector2fs[i];
            g.drawLine((int) S.x,(int)S.y,(int)E.x,(int)E.y);
            E = S;
        }

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
}
