package own.gamelearning.objects;

import own.gamelearning.object.Vector2f;
import own.gamelearning.support.Matrix3x3f;

import java.awt.*;

/**
 * Created by 挨踢狗 on 2017/5/10.
 */
public class PrototypeBullet {
    private Vector2f velocity;
    private Vector2f position;
    private Color color;
    private float radius;

    public PrototypeBullet(Vector2f position, float angle){
        this.position = position;
        velocity = Vector2f.polar(angle,1.0f);
        radius = 0.006f;
        color = Color.BLUE;
    }
    public void draw(Graphics g, Matrix3x3f view){
        Vector2f topLeft = new Vector2f(position.x - radius,position.y + radius);
        topLeft = view.mul(topLeft);
        Vector2f bottomRight = new Vector2f(position.x + radius,position.y - radius);
        bottomRight = view.mul(bottomRight);
        int circleX = (int)topLeft.x;
        int circleY = (int)topLeft.y;
        int circleW = (int)(bottomRight.x - topLeft.x);
        int circleH = (int)(bottomRight.y - topLeft.y);
        g.fillOval(circleX,circleY,circleW,circleH);
    }
    public void update(float time){
        position = position.add(velocity.mul(time));
    }
    public Vector2f getPosition(){
        return position;
    }
}
