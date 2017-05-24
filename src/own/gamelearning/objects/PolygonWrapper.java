package own.gamelearning.objects;

import own.gamelearning.object.Vector2f;
import own.gamelearning.support.Matrix3x3f;

import java.util.ArrayList;

/**
 * Created by 挨踢狗 on 2017/5/10.
 */
public class PolygonWrapper {
    private float worldWidth;
    private float worldHeight;
    private Vector2f worldMax;
    private Vector2f worldMin;

    public PolygonWrapper(float worldWidth, float worldHeight){
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        worldMax = new Vector2f(worldWidth / 2.0f, worldHeight / 2.0f);
        worldMin = worldMax.inv();
    }
    public boolean hasPosLeftWorld(Vector2f position){
        return position.x < worldMin.x || position.x > worldMax.x || position.y < worldMin.y || position.y > worldMax.y;
    }
    public Vector2f wrapPosition(Vector2f position){
        Vector2f wrappedPosition = new Vector2f();
        if (position.x < worldMin.x){
            wrappedPosition.x = position.x + worldWidth;
        }
        else if (position.x > worldMax.x){
            wrappedPosition.x = position.x - worldWidth;
        }
        if (position.y < worldMin.y){
            wrappedPosition.y = position.y + worldHeight;
        }
        else if (position.y < worldMax.y){
            wrappedPosition.y = position.y - worldHeight;
        }
        return wrappedPosition;
    }
    public void wrapPolygon(Vector2f[] polygon, ArrayList<Vector2f[]> renderList){
        Vector2f min = getMinVector2f(polygon);
        Vector2f max = getMaxVector2f(polygon);
        boolean north = max.y > worldMax.y;
        boolean south = min.y < worldMin.y;
        boolean west = min.x < worldMin.x;
        boolean east = max.x > worldMax.x;
        if (north)renderList.add(wrapNorth(polygon));
        if (south)renderList.add(wrapSouth(polygon));
        if (west)renderList.add(wrapWest(polygon));
        if (east)renderList.add(wrapEast(polygon));
        if (north && west)renderList.add(wrapNorthWest(polygon));
        if (north && east)renderList.add(wrapNorthEast(polygon));
        if (south && west)renderList.add(wrapSouthWest(polygon));
        if (south && east)renderList.add(wrapSouthEast(polygon));
    }
    private Vector2f[] wrapNorth(Vector2f[] polygon){
        return transform(polygon,Matrix3x3f.translate(0.0f, worldHeight));
    }
    private Vector2f[] wrapSouth(Vector2f[] polygon){
        return transform(polygon,Matrix3x3f.translate(0.0f, -worldHeight));
    }
    private Vector2f[] wrapWest(Vector2f[] polygon){
        return transform(polygon,Matrix3x3f.translate(-worldWidth,0.0f));
    }
    private Vector2f[] wrapEast(Vector2f[] polygon){
        return transform(polygon,Matrix3x3f.translate(worldWidth,0.0f));
    }
    private Vector2f[] wrapNorthWest(Vector2f[] polygon){
        return transform(polygon,Matrix3x3f.translate(worldWidth,-worldHeight));
    }
    private Vector2f[] wrapNorthEast(Vector2f[] polygon){
        return transform(polygon,Matrix3x3f.translate(-worldWidth,-worldHeight));
    }
    private Vector2f[] wrapSouthWest(Vector2f[] polygon){
        return transform(polygon,Matrix3x3f.translate(worldWidth,worldHeight));
    }
    private Vector2f[] wrapSouthEast(Vector2f[] polygon){
        return transform(polygon,Matrix3x3f.translate(-worldWidth,worldHeight));
    }
    private Vector2f[] transform(Vector2f[] polygon, Matrix3x3f view){
        Vector2f[] copy = new Vector2f[polygon.length];
        for (int i = 0; i < copy.length;i++){
            copy[i] = view.mul(polygon[i]);
        }
        return copy;
    }
    private Vector2f getMinVector2f(Vector2f[] polygon){
        Vector2f min = new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
        for (Vector2f v : polygon){
            if (v.x < min.x) min.x = v.x;
            if (v.y < min.y) min.y = v.y;
        }
        return min;
    }
    private Vector2f getMaxVector2f(Vector2f[] polygon){
        Vector2f max = new Vector2f(-Float.MAX_VALUE,-Float.MAX_VALUE);
        for (Vector2f v : polygon){
            if (v.x > max.x) max.x = v.x;
            if (v.y > max.y) max.y = v.y;
        }
        return max;
    }
}
