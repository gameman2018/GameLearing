package own.gamelearning.support;

import own.gamelearning.object.Vector2f;

import java.awt.*;
import java.util.List;


/**
 * Created by 挨踢狗 on 2017/5/3.
 */
public class Utility {
    public static Matrix3x3f createViewport(float worldWidth, float worldHeight, float screenWidth, float screenHeight){
        float sx = screenWidth / worldWidth;
        float sy = screenHeight / worldHeight;
        float tx = (screenWidth - 1)/ 2.0f;
        float ty = (screenHeight - 1)/ 2.0f;
        Matrix3x3f viewport = Matrix3x3f.scale(sx,-sy);
        viewport = viewport.mul(Matrix3x3f.translate(tx,ty));
        return viewport;
    }

    public static Matrix3x3f createReverseViewport(float worldWidth, float worldHeight, float screenWidth, float screenHeight){
        float sx = worldWidth / (screenWidth - 1);
        float sy = worldHeight / (screenHeight -1);
        float tx = (screenWidth - 1)/ 2.0f;
        float ty = (screenHeight - 1)/ 2.0f;
        Matrix3x3f viewport = Matrix3x3f.translate(-tx,-ty);
        viewport = viewport.mul(Matrix3x3f.scale(sx,-sy));
        return viewport;
    }

    public static void drawPolygon(Graphics g, Vector2f[] vector2fs) {
        Vector2f START = null;
        Vector2f END = vector2fs[vector2fs.length - 1];
        for (int i = 0;i < vector2fs.length; i++){
            START = vector2fs[i];
            g.drawLine((int)START.x, (int)START.y, (int)END.x, (int)END.y);
            END = START;
        }
    }

    public static void drawPolygon(Graphics g, List<Vector2f> vector2fList){
        Vector2f END = vector2fList.get(vector2fList.size() - 1);
        for (Vector2f v : vector2fList){
            g.drawLine((int)v.x, (int)v.y, (int)END.x, (int)END.y);
            END = v;
        }
    }

    public static void drawFill(Graphics2D g, Vector2f[] vector2fs){
        Polygon polygon = new Polygon();
        for (Vector2f v : vector2fs){
            polygon.addPoint((int)v.x,(int)v.y);
        }
        g.fill(polygon);
    }
    public static void drawFill(Graphics2D g, List<Vector2f> vector2fs){
        Polygon polygon = new Polygon();
        for (Vector2f v : vector2fs){
            polygon.addPoint((int)v.x, (int)v.y);
        }
        g.fill(polygon);
    }
}
