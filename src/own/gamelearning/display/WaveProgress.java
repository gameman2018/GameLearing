package own.gamelearning.display;

import own.gamelearning.object.Vector2f;
import own.gamelearning.support.FrameControl;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaveProgress extends FrameControl {
    private float waveHeight;
    private float waveWidth;
    private float axisWidth;
    private List<Vector2f> wavePoints;
    private Vector2f start;
    private float waveSpeed;
    private float offset;
    private float timer;
    private float range;
    private Color waveColor;
    private boolean waveDirection;

    public WaveProgress(){
        appWidth = 800;
        appHeight = 300;
        launchApp(this);
    }

    @Override
    protected void initialize() {
        super.initialize();
        waveColor = Color.CYAN;
        waveHeight = 30.0f;
        waveWidth = 0.011f;
        waveSpeed = 0.16f;
        offset = 0.0f;
        appSleep = 8L;
        axisWidth = appWidth;
        wavePoints = new ArrayList<Vector2f>();
        start = new Vector2f(0,appHeight / 2.0f + appHeight / 4.0f);
    }


    @Override
    protected void updateObjects(float delta) {
        super.updateObjects(delta);
        wavePoints.clear();
        offset += waveSpeed;
        for (float i = start.x;i < axisWidth;i += 0.1){
            float y = (float) -Math.sin(i * waveWidth + offset);
            y = start.y + y * waveHeight * range;
            wavePoints.add(new Vector2f(i,y));
            System.out.println(wavePoints.size());
        }
        if (start.y <= 10){

            waveDirection = false;
        }
        if (start.y >= appHeight / 2.0f + appHeight / 4.0f){
            waveDirection = true;
        }

        if (waveDirection){
            start.y -= 0.2;
        }else {
            start.y += 0.2;
        }
        timer += delta;
        if (timer >= 2){
            if (range >= 1.0f){
                range -= 0.05;
            }else{
                range += 0.05;
            }
            timer -= 2;
            waveColor = new Color(new Random().nextInt());
        }
    }

    @Override
    protected void render(Graphics g) {
        super.render(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        Vector2f S = wavePoints.get(0);
        Vector2f E = null;
        g2d.setColor(waveColor);
        for (int i = 0;i < wavePoints.size();i+=2){
            E = wavePoints.get(i + 1 > wavePoints.size() - 1 ? i : i + 1);
            g2d.drawLine((int)S.x,(int)S.y,(int)E.x,(int)E.y);
            g2d.drawLine((int)S.x,(int)S.y,(int)S.x,appHeight);
            S = E;
        }
        g.setColor(Color.RED);
        String str1 = "波浪系数: ";
        byte[] bytes1 = new byte[0];
        try {
            bytes1 = str1.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        g.drawString(new String(bytes1) + String.format("%.2f",range * 100) + "%(每2秒自增5%)", 20,45);
    }
}
