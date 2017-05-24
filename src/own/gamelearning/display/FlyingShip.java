package own.gamelearning.display;

import own.gamelearning.object.Vector2f;
import own.gamelearning.objects.PolygonWrapper;
import own.gamelearning.objects.PrototypeBullet;
import own.gamelearning.objects.PrototypeShip;
import own.gamelearning.support.FrameControl;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class FlyingShip extends FrameControl {
    private ArrayList<PrototypeBullet> bulltes;
    private PrototypeShip ship;
    private PolygonWrapper wrapper;

    public FlyingShip(){
        appBorderScale = 0.9f;
        appCanvasBackgrounColor = Color.WHITE;
        appFPSColor = Color.BLACK;
        appWidth = 680;
        appHeight = 680;
        appWorldWidth = 2.0f;
        appWorldHeight = 2.0f;
        appSleep = 1L;
        launchApp(this);
    }

    @Override
    protected void initialize() {
        super.initialize();
        bulltes = new ArrayList<>();
        wrapper = new PolygonWrapper(appWorldWidth,appWorldHeight);
        ship = new PrototypeShip(wrapper);
    }

    @Override
    protected void processInput(float delta) {
        super.processInput(delta);
        if (appKeyboard.KeyDown(KeyEvent.VK_LEFT)){
            ship.rotateLeft(delta);
        }
        if (appKeyboard.KeyDown(KeyEvent.VK_RIGHT)){
            ship.rotateRight(delta);
        }
        if (appKeyboard.KeyDownOnce(KeyEvent.VK_SPACE)){
            bulltes.add(ship.launchBullet());
        }
        ship.setThrusting(appKeyboard.KeyDown(KeyEvent.VK_UP));
    }

    @Override
    protected void updateObjects(float delta) {
        super.updateObjects(delta);
        ship.update(delta);
        ArrayList<PrototypeBullet> copy = new ArrayList<PrototypeBullet>(bulltes);
        for (PrototypeBullet bullet : copy){
            bullet.update(delta);
            if(wrapper.hasPosLeftWorld(bullet.getPosition())){
                bulltes.remove(bullet);
            }
        }
    }

    @Override
    protected void render(Graphics g) {
        super.render(g);
        ship.draw(g,getViewportTransform());
        for (PrototypeBullet bullet : bulltes){
            bullet.draw(g,getViewportTransform());
        }
    }
}
