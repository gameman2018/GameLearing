package own.gamelearning.objects;

import own.gamelearning.object.Vector2f;
import own.gamelearning.support.Matrix3x3f;
import own.gamelearning.support.Utility;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by 挨踢狗 on 2017/5/10.
 */
public class PrototypeAsteroid {
    public enum Size{
        Large,
        Medium,
        Small
    }
    private Size size;
    private Vector2f velocity;
    private Vector2f position;
    private float rotationDelta;
    private float rotation;
    private ArrayList<Vector2f[]> renderList;
    private Vector2f[] polygon;
    private PolygonWrapper wrapper;

    public Size getSize(){
        return size;
    }
    public void setSize(Size size){
        this.size = size;
    }

    public Vector2f getPosition(){
        return position;
    }

    public void setPosition(Vector2f position){
        this.position = position;
    }

    public void setPolygon(Vector2f[] polygon){
        this.polygon = polygon;
    }

    public PrototypeAsteroid(PolygonWrapper wrapper){
        this.wrapper = wrapper;
        renderList = new ArrayList<Vector2f[]>();
        velocity = getRandomVelocity();
        rotationDelta = getRandomRotationDelta();
    }

    private Vector2f getRandomVelocity(){
        float angle = getRandomRadians(0,360);
        float radius = getRandomFloat(0.06f,0.3f);
        return Vector2f.polar(angle,radius);
    }
    private float getRandomRotationDelta(){
        float rotationDelta = getRandomRadians(5,45);
        return new Random().nextBoolean() ? rotationDelta : -rotationDelta;
    }
    private float getRandomRadians(int minDegree,int maxDegree){
        int rand = new Random().nextInt(maxDegree - minDegree + 1);
        return (float)Math.toRadians(minDegree + rand);
    }
    private float getRandomFloat(float min, float max){
        return new Random().nextFloat() * (max - min) + min;
    }
    public void update(float time){
        position = position.add(velocity.mul(time));
        position = wrapper.wrapPosition(position);
        rotation += rotationDelta * time;

        renderList.clear();
        Vector2f[] world = transformPolygon();
        renderList.add(world);
        wrapper.wrapPolygon(polygon,renderList);
    }
    private Vector2f[] transformPolygon(){
        Matrix3x3f matrix3x3f = Matrix3x3f.rotate(rotation);
        matrix3x3f = matrix3x3f.mul(Matrix3x3f.translate(position));
        return transform(polygon, matrix3x3f);
    }
    private Vector2f[] transform(Vector2f[] polygon, Matrix3x3f mat){
        Vector2f[] copy = new Vector2f[polygon.length];
        for (int i = 0;i < polygon.length;i++){
            copy[i] = mat.mul(polygon[i]);
        }
        return copy;
    }
    private void draw(Graphics g, Matrix3x3f view){
        Graphics2D g2d = (Graphics2D)g;
        for (Vector2f[] polygon : renderList){
            for (int i = 0;i < polygon.length;i++){
                polygon[i] = view.mul(polygon[i]);
            }
            g2d.setColor(Color.GRAY);
            Utility.drawFill(g2d,polygon);
            g.setColor(Color.BLACK);
            Utility.drawPolygon(g,polygon);
        }
    }

    public boolean contains(Vector2f point){
        for (Vector2f[] polygon : renderList){
            return pointInPolygon(point,polygon);
        }
        return false;
    }
    private boolean pointInPolygon(Vector2f point, Vector2f[] polygon){
        boolean inside = false;
        Vector2f start = polygon[polygon.length - 1];
        boolean startAbove = start.y >= point.y;
        for (int i = 0;i < polygon.length;i++){
            Vector2f end = polygon[i];
            boolean endAbove = end.y >= point.y;
            if (endAbove != startAbove){
                float m = (end.y - start.y) / (end.x - start.x);
                float x = start.x + (point.y - start.y) / m;
                if (x>=point.x){
                    inside = !inside;
                }
            }
            startAbove = endAbove;
            start = end;
        }
        return inside;
    }
}
