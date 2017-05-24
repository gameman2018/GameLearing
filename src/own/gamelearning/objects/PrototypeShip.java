package own.gamelearning.objects;

import own.gamelearning.object.Vector2f;
import own.gamelearning.support.Matrix3x3f;
import own.gamelearning.support.Utility;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by 挨踢狗 on 2017/5/10.
 */
public class PrototypeShip {
    private Vector2f[] ship;
    private Vector2f velocity;
    private Vector2f position;
    private float rotationDelta;
    private float rotation;
    private float acceleration;
    private float maxVelocity;
    private float curAcc;
    private float friction;
    private PolygonWrapper wrapper;
    private boolean damaged;
    private ArrayList<Vector2f[]> renderList;

    public PrototypeShip(PolygonWrapper wrapper){
        this.wrapper = wrapper;
        friction = 0.25f;
        velocity = new Vector2f();
        position = new Vector2f();
        rotationDelta = (float)Math.toRadians(180.0);
        rotation = 0.0f;
        acceleration = 1.0f;
        maxVelocity = 0.5f;
        ship = new Vector2f[]{
            new Vector2f(0.0325f,0.0f),
                new Vector2f(-0.0325f,-0.0325f),
                new Vector2f(0.0f,0.0f),
                new Vector2f(-0.0325f,0.0325f)
        };
        renderList = new ArrayList<Vector2f[]>();
    }

    public void setDamaged(boolean damaged){
        this.damaged = damaged;
    }
    public boolean isDamaged(){
        return damaged;
    }
    public void rotateLeft(float time){
        rotation += rotationDelta * time;
    }
    public void rotateRight(float time){
        rotation -= rotationDelta * time;
    }

    public void setRotation(float rotation){
        this.rotation = rotation;
    }

    public void setThrusting(boolean thrusting){
        curAcc = thrusting ? acceleration : 0.0f;
    }
    public PrototypeBullet launchBullet(){
        Vector2f bulletPos = position.add(Vector2f.polar(rotation, 0.0325f));
        return new PrototypeBullet(bulletPos, rotation);
    }
    public void update(float time){
        updatePosition(time);
        renderList.clear();
        Vector2f[] world = transformPolygon();
        renderList.add(world);
        wrapper.wrapPolygon(world,renderList);
    }
    public void draw(Graphics g,Matrix3x3f view){
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(new Color(50,50,50));
        for (Vector2f[] polygon : renderList){
            for (int i = 0;i < polygon.length;i++){
                polygon[i] = view.mul(polygon[i]);
            }
            Utility.drawFill(g2d,polygon);
            Utility.drawPolygon(g2d,polygon);
        }
    }

    public boolean isTouching(PrototypeAsteroid asteroid){
        for (Vector2f[] polygon : renderList){
            for (int i = 0;i < polygon.length;i++){
                if (asteroid.contains(polygon[i]))return true;
            }
        }
        return false;
    }
    private Vector2f[] transformPolygon(){
        Matrix3x3f matrix3x3f = Matrix3x3f.rotate(rotation);
        matrix3x3f = matrix3x3f.mul(Matrix3x3f.translate(position));
        return transform(ship,matrix3x3f);
    }

    private Vector2f[] transform(Vector2f[] polygon,Matrix3x3f view){
        Vector2f[] copy = new Vector2f[polygon.length];
        for (int i = 0; i < polygon.length;i++){
            copy[i] = view.mul(polygon[i]);
        }
        return copy;
    }
    private void updatePosition(float time){
        Vector2f acce = Vector2f.polar(rotation,curAcc);
        velocity = velocity.add(acce.mul(time));
        float maxSpeed = (float) Math.min(maxVelocity / velocity.len() , 1.0);
        velocity = velocity.mul(maxSpeed);
        float slowDown = 1.0f - friction * time;
        velocity = velocity.mul(slowDown);
        position = position.add(velocity.mul(time));
        position = wrapper.wrapPosition(position);
    }
}
