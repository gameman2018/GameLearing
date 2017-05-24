package own.gamelearning.support;

import own.gamelearning.object.Vector2f;

/**
 * Created by 挨踢狗 on 2017/4/27.
 */
public class Matrix3x3f {

    private float[][] m = new float[3][3];

    public Matrix3x3f(){
    }

    public Matrix3x3f(float[][] m){
        setMatrix3x3f(m);
    }

    private void setMatrix3x3f(float[][] m){
        this.m = m;
    }

    public static Matrix3x3f zero(){
        return new Matrix3x3f(new float[][]{
                { 0.0f, 0.0f, 0.0f },
                { 0.0f, 0.0f, 0.0f },
                { 0.0f, 0.0f, 0.0f }
        });
    }

    public static Matrix3x3f identity(){
        return new Matrix3x3f(new float[][]{
                { 1.0f, 0.0f, 0.0f },
                { 0.0f, 1.0f, 0.0f },
                { 0.0f, 0.0f, 1.0f }
        });
    }

    public Matrix3x3f add(Matrix3x3f m1){
        return new Matrix3x3f(new float[][]{
                {
                        this.m[0][0] + m1.m[0][0],
                        this.m[0][1] + m1.m[0][1],
                        this.m[0][2] + m1.m[0][2]
                },
                {
                        this.m[1][0] + m1.m[1][0],
                        this.m[1][1] + m1.m[1][1],
                        this.m[1][2] + m1.m[1][2]
                },
                {
                        this.m[2][0] + m1.m[2][0],
                        this.m[2][1] + m1.m[2][1],
                        this.m[2][2] + m1.m[2][2]
                }
        });
    }

    public Matrix3x3f sub(Matrix3x3f m1){
        return new Matrix3x3f(new float[][]{
                {
                        this.m[0][0] - m1.m[0][0],
                        this.m[0][1] - m1.m[0][1],
                        this.m[0][2] - m1.m[0][2]
                },
                {
                        this.m[1][0] - m1.m[1][0],
                        this.m[1][1] - m1.m[1][1],
                        this.m[1][2] - m1.m[1][2]
                },
                {
                        this.m[2][0] - m1.m[2][0],
                        this.m[2][1] - m1.m[2][1],
                        this.m[2][2] - m1.m[2][2]
                }
        });
    }

    public Matrix3x3f mul(Matrix3x3f m1){
        return new Matrix3x3f(new float[][]{
                {
                        this.m[0][0] * m1.m[0][0] + this.m[0][1] * m1.m[1][0] + this.m[0][2] * m1.m[2][0],
                        this.m[0][0] * m1.m[0][1] + this.m[0][1] * m1.m[1][1] + this.m[0][2] * m1.m[2][1],
                        this.m[0][0] * m1.m[0][2] + this.m[0][1] * m1.m[1][2] + this.m[0][2] * m1.m[2][2]
                },
                {
                        this.m[1][0] * m1.m[0][0] + this.m[1][1] * m1.m[1][0] + this.m[1][2] * m1.m[2][0],
                        this.m[1][0] * m1.m[0][1] + this.m[1][1] * m1.m[1][1] + this.m[1][2] * m1.m[2][1],
                        this.m[1][0] * m1.m[0][2] + this.m[1][1] * m1.m[1][2] + this.m[1][2] * m1.m[2][2]
                },
                {
                        this.m[2][0] * m1.m[0][0] + this.m[2][1] * m1.m[1][0] + this.m[2][2] * m1.m[2][0],
                        this.m[2][0] * m1.m[0][1] + this.m[2][1] * m1.m[1][1] + this.m[2][2] * m1.m[2][1],
                        this.m[2][0] * m1.m[0][2] + this.m[2][1] * m1.m[1][2] + this.m[2][2] * m1.m[2][2]
                }
        });
    }

    public Vector2f mul(Vector2f v){
        return new Vector2f(
                 v.x * this.m[0][0] + v.y * this.m[1][0] + v.w * this.m[2][0],
                 v.x * this.m[0][1] + v.y * this.m[1][1] + v.w * this.m[2][1],
                 v.x * this.m[0][2] + v.y * this.m[1][2] + v.w * this.m[2][2]
        );
    }
    public static Matrix3x3f translate(Vector2f v){
        return translate(v.x,v.y);
    }
    public static Matrix3x3f translate(float x, float y){
        return new Matrix3x3f(new float[][]{
                { 1.0f, 0.0f, 0.0f},
                { 0.0f, 1.0f, 0.0f},
                { x, y, 1.0f}
        });
    }

    public static Matrix3x3f scale(Vector2f v){
        return scale(v.x,v.y);
    }
    public static Matrix3x3f scale(float x, float y){
        return new Matrix3x3f(new float[][]{
                { x, 0.0f, 0.0f},
                { 0.0f, y, 0.0f},
                { 0.0f, 0.0f, 1.0f}
        });
    }

    public static Matrix3x3f shear(Vector2f v){
        return shear(v.x,v.y);
    }
    public static Matrix3x3f shear(float x, float y){
        return new Matrix3x3f(new float[][]{
                { 1.0f, y, 0.0f},
                { x, 1.0f, 0.0f},
                { 0.0f, 0.0f, 1.0f}
        });
    }

    public static Matrix3x3f rotate(float rad){
        return new Matrix3x3f(new float[][]{
                { (float) Math.cos(rad), (float) Math.sin(rad), 0.0f},
                { (float) -Math.sin(rad), (float) Math.cos(rad), 0.0f},
                { 0.0f, 0.0f, 1.0f}
        });
    }
}
