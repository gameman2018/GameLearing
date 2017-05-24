package own.gamelearning.support;

import own.gamelearning.input.KeyboardInput;
import own.gamelearning.input.MouseInput;
import own.gamelearning.object.Vector2f;
import own.gamelearning.system.RateObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

/**
 * Created by 挨踢狗 on 2017/5/3.
 */
public class FrameControl extends JFrame implements Runnable {

    private Thread game;
    private volatile boolean running;
    private BufferStrategy bs;

    protected RateObserver rate;
    protected Canvas canvas;
    protected KeyboardInput appKeyboard;
    protected MouseInput appMouse;

    protected Color appFPSColor = Color.BLACK;
    protected Color appFrameBackgrounColor = Color.LIGHT_GRAY;
    protected Color appCanvasBackgrounColor = Color.WHITE;

    protected float appWorldWidth = 16.0f;
    protected float appWorldHeight = 9.0f;
    protected int appWidth = 680;
    protected int appHeight = 480;

    protected boolean appMaintainRatio = false;


    protected float appBorderScale = 0.8f;

    protected String appTitle = "Frame";
    protected Font appFont = new Font("Courier New",Font.PLAIN,14);

    protected long appSleep = 10L;

    public FrameControl(){

    }

    protected void createAndShowGUI(){
        canvas = new Canvas();
        canvas.setBackground(appCanvasBackgrounColor);

        canvas.setIgnoreRepaint(true);
        getContentPane().add(canvas);
        setLocationByPlatform(true);
        if (appMaintainRatio){
            getContentPane().setBackground(appFrameBackgrounColor);
            setSize(appWidth,appHeight);
            setLayout(null);
            getContentPane().addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    onComponentResized(e);
                }
            });
        }else {
            canvas.setSize(appWidth,appHeight);
            setResizable(false);
            pack();
        }

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIgnoreRepaint(true);
        setTitle(appTitle);

        appKeyboard = new KeyboardInput();
        canvas.addKeyListener(appKeyboard);
        appMouse = new MouseInput();
        canvas.addMouseListener(appMouse);
        canvas.addMouseMotionListener(appMouse);
        canvas.addMouseWheelListener(appMouse);

        setVisible(true);

        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
        canvas.requestFocus();

        game = new Thread(this);
        game.start();
    }

    protected void onComponentResized(ComponentEvent e){
        Dimension size = getContentPane().getSize();
        appWidth = size.width;
        appHeight = size.height;
        int vw = (int) (appWidth * appBorderScale);
        int vh = (int) (appHeight * appBorderScale);
        int vx = (size.width - vw) / 2;
        int vy = (size.height - vh) / 2;

        int newW = vw;
        int newH = (int) (vw * appWorldHeight / appWorldWidth);
        if (vh < newH){
            newW = (int) (vh * appWorldWidth / appWorldHeight);
            newH = vh;
        }

        vx += (vw - newW) / 2;
        vy += (vh - newH) / 2;

        canvas.setLocation(vx,vy);
        canvas.setSize(newW ,newH);
    }

    protected Matrix3x3f getViewportTransform(){
        return Utility.createViewport(appWorldWidth,appWorldHeight,appWidth,appHeight);
    }

    protected Matrix3x3f getReverseViewportTransform(){
        return Utility.createReverseViewport(appWorldWidth,appWorldHeight,appWidth,appHeight);
    }

    protected Vector2f getWorldMousePosition(){
        Matrix3x3f screenToWorld = getReverseViewportTransform();
        Point mousePos = appMouse.getPosition();
        Vector2f screenPos = new Vector2f(mousePos.x,mousePos.y);
        return screenToWorld.mul(screenPos);
    }

    protected Vector2f getRelativeWorldMousePosition(){
        float sx = appWorldWidth / (canvas.getWidth() - 1);
        float sy = appWorldHeight / (canvas.getHeight() - 1);
        Matrix3x3f viewport = Matrix3x3f.scale(sx,-sy);
        Point p = appMouse.getPosition();
        return viewport.mul(new Vector2f(p.x,p.y));
    }

    protected void initialize(){
        rate = new RateObserver();
        rate.initialize();
    }

    @Override
    public void run() {
        initialize();
        running = true;
        long cur = System.nanoTime();
        long last = cur;
        float delta;
        while (running){
            cur = System.nanoTime();
            delta = cur - last;
            gameLoop((float) (delta / 1.0E9));
            last = cur;
        }
        terminate();
    }

    protected void terminate(){

    }
    private void gameLoop(float delta){
        processInput(delta);
        updateObjects(delta);
        renderFrame();
        sleep();
    }

    private void renderFrame(){
        do {
            do {
                Graphics g = null;
                try {
                    g = bs.getDrawGraphics();
                    g.clearRect(0,0,getWidth(),getHeight());
                    render(g);
                } finally {
                    if (g != null)
                    g.dispose();
                }
            }while (bs.contentsRestored());
            bs.show();
        }while (bs.contentsLost());
    }
    protected void processInput(float delta){
        appKeyboard.poll();
        appMouse.poll();
    }
    protected void updateObjects(float delta){

    }
    protected void render(Graphics g){
        //g.setFont(appFont);
        g.setColor(appFPSColor);
        rate.calculate();
        g.drawString(rate.getRate(),20,20);
    }
    private void sleep(){
        try {
            Thread.sleep(appSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void onWindowClosing(){
        try {
            running = false;
            game.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    protected static void launchApp(final FrameControl app){
        app.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                app.onWindowClosing();
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                app.createAndShowGUI();
            }
        });
    }
}
