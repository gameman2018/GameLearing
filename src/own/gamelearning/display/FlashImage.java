package own.gamelearning.display;

import own.gamelearning.support.FrameControl;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FlashImage extends FrameControl{
    private BufferedImage bi;
    private float shift;

    public FlashImage(){
           appWidth = 400;
           appHeight = 400;
           launchApp(this);
    }

    @Override
    protected void initialize() {
        super.initialize();
        bi = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bi.createGraphics();
        int x = 8;
        int y = 8;
        int dx = 256 / x;
        int dy = 256 / y;
        for (int i = 0;i < 8;i++){
            for (int j = 0;j < 8;j++){
                if ((i + j) % 2 == 0){
                    graphics2D.setColor(Color.GREEN);
                    graphics2D.fillRect(i * dx, j * dy,dx,dy);
                }
            }
        }

    }

    @Override
    protected void updateObjects(float delta) {
        super.updateObjects(delta);
        int r = canvas.getHeight() / 5;
        shift += r * delta;
        if (shift > r){
            shift -= r;
        }
    }

    @Override
    protected void render(Graphics g) {
        super.render(g);
        int h = canvas.getHeight() / 5;
        g.setColor(Color.ORANGE);
        for (int i = 0; i < 6; i++){
            g.fillRect(0, (int)((i * h) - shift),canvas.getWidth(),h / 2);
        }
        g.drawImage(bi,(appWidth - 256) / 2,(appHeight - 256) / 2,null);
    }
}
