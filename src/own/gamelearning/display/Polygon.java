package own.gamelearning.display;

import own.gamelearning.object.Vector2f;
import own.gamelearning.support.FrameControl;
import own.gamelearning.support.Matrix3x3f;
import own.gamelearning.support.Utility;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by 挨踢狗 on 2017/5/3.
 */
public class Polygon extends FrameControl {

    private final static int MAX_POINTS = 20;
    private Random random;
    private Vector2f mousePos;
    private Vector2f mouseRePos;

    private ArrayList<Vector2f> polygon;
    private ArrayList<Vector2f> polygonCpy;

    private ArrayList<Vector2f> insides;
    private ArrayList<Vector2f> outsides;

    private boolean winding;

    public Polygon(){
        appMaintainRatio = false;
        appSleep = 200L;
//        appWidth = 1920;
//        appHeight = 1080;
        launchApp(this);
    }
    @Override
    protected void initialize() {
        super.initialize();
        random = new Random();
        polygon = new ArrayList<Vector2f>();
        polygonCpy = new ArrayList<Vector2f>();
        insides = new ArrayList<Vector2f>();
        outsides = new ArrayList<Vector2f>();
    }

    @Override
    protected void createAndShowGUI() {
        super.createAndShowGUI();
    }

    @Override
    protected void updateObjects(float delta) {
        super.updateObjects(delta);
        if (polygon.size() >= 2){
            polygonCpy.clear();
            for (Vector2f v : polygon){
                Matrix3x3f matrix3x3f = Matrix3x3f.identity();
                matrix3x3f = matrix3x3f.mul(getViewportTransform());
                polygonCpy.add(matrix3x3f.mul(v));
            }
            insides.clear();
            outsides.clear();
            for (int i = 0; i < MAX_POINTS; i++){
                Vector2f point = new Vector2f(random.nextFloat() * appWorldWidth - 8.0f,random.nextFloat() * appWorldHeight - 4.5f);
                if (pointInPolygon(point,polygon, winding)){
                    insides.add(point);
                }else {
                    outsides.add(point);
                }
            }
            System.out.println("I: " + insides.size() + " O: " + outsides.size());
        }
    }

    private boolean pointInPolygon(Vector2f point, List<Vector2f> poly, boolean winding){
        int inside = 0;
        Vector2f start = poly.get(poly.size() - 1);
        boolean startAbove = start.y >= point.y;
        for (Vector2f end : poly){
            boolean endAbove = end.y >= point.y;
            if (startAbove != endAbove){
                float m = (end.y - start.y) / (end.x - start.x);
                float x = start.x + (point.y - start.y) / m;
                if (x >= point.x){
                    if (winding){
                        inside += startAbove ? 1 : -1;
                    }else {
                        inside = inside == 1 ? 0 : 1;
                    }
                }
            }
            startAbove = endAbove;
            start = end;
        }
        return inside != 0;
    }

    @Override
    protected void processInput(float delta) {
        super.processInput(delta);
        mousePos = getWorldMousePosition();
        mouseRePos = getRelativeWorldMousePosition();

        if (appMouse.buttonDownOnce(MouseEvent.BUTTON1)){
            polygon.add(mousePos);
        }

        if (appMouse.buttonDownOnce(MouseEvent.BUTTON3)){
            polygon.clear();
        }

        if (appKeyboard.KeyDownOnce(KeyEvent.VK_SPACE)){
            winding = !winding;
        }
    }

    @Override
    protected void render(Graphics g) {
        super.render(g);
        g.setColor(Color.WHITE);
        g.drawString("X: " + mousePos.x + "  Y: " + mousePos.y,20,35);
        g.drawString("X: " + mouseRePos.x + "  Y: " + mouseRePos.y,20,50);
        if (polygon.size() >= 2){
            Matrix3x3f viewport = getViewportTransform();
            g.setColor(Color.RED);
            for (Vector2f v : insides){
                v = viewport.mul(v);
                ImageIcon i = new ImageIcon(getClass().getResource("../images/timg.jpg"));
//                g.fillOval((int)v.x,(int)v.y,6,6);
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(i.getImage(),(int)v.x,(int) v.y,60,60,null);
            }
            g.setColor(Color.blue);
            for (Vector2f v : outsides){
                v = viewport.mul(v);
                ImageIcon i = new ImageIcon(getClass().getResource("../images/t.png"));
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(i.getImage(),(int)v.x,(int) v.y,60,60,null);
//                g.fillOval((int)v.x,(int)v.y,6,6);
            }
            if (pointInPolygon(mousePos,polygon,winding)){
                g.setColor(Color.MAGENTA);
            }else {
                g.setColor(Color.ORANGE);
            }
            Utility.drawPolygon(g,polygonCpy);
        }

    }
}
