package com.zhibaowang.component;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.Light;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;
import com.zhibaowang.app.R;
import com.zhibaowang.tools.S;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import java.util.Arrays;

public class ZBoxRender implements Renderer {

    public Activity mActivity = null;
    private FrameBuffer fb = null;
    private World world = null;
    Camera cam = null;
    // 立方体
    private Object3D cube = null;
    private Object3D pick_1, pick_2, pick_3, pick_4, pick_5, pick_6;
    // 光照类
    private Light sun = null;
    private RGBColor back = new RGBColor(0, 0, 0);
    // 旋转
    public float rotateY = 0;
    public float rotateX = 0;
    public float angle = 0;
    private final float radius_position = 10f;

    public ZBoxRender(Context context) {
        mActivity = (Activity) context;
    }

    public int Pickint(int fX, int fY) {
        //fY = fb.getHeight() - fY;
        SimpleVector dir = Interact2D.reproject2D3DWS(cam, fb, fX, fY).normalize();
        Object[] res = world.calcMinDistanceAndObject3D(cam.getPosition(), dir, 10000);

        Object3D picked = (Object3D) res[1];

        if (picked == null) {
            return -1;
        }
        if (picked.getID() == pick_1.getID()) {
            pick_1.setAdditionalColor(255, 0, 0);
            return 1;
        } else if (picked.getID() == pick_2.getID()) {
            pick_2.setAdditionalColor(255, 0, 0);
            return 2;
        } else if (picked.getID() == pick_3.getID()) {
            pick_3.setAdditionalColor(255, 0, 0);
            return 3;
        } else if (picked.getID() == pick_4.getID()) {
            pick_4.setAdditionalColor(255, 0, 0);
            return 4;
        } else if (picked.getID() == pick_5.getID()) {
            pick_5.setAdditionalColor(255, 0, 0);
            return 5;
        } else if (picked.getID() == pick_6.getID()) {
            pick_6.setAdditionalColor(255, 0, 0);
            return 6;
        }

        return -1;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // TODO Auto-generated method stub

        if (angle > 360) {
            angle = 0;
        } else {
            angle += 0.001f;
        }
//        cube.translateMesh();
        float x_next = (float) (radius_position * Math.cos(angle));
        float y_next = (float) (radius_position * Math.sin(angle));
        cube.translate(x_next - x, y_next - y, 0);/**//**/
        x = x_next;
        y = y_next;
        if (rotateY != 0) {
            if (Math.abs(rotateY) > 0.001f) {
                if (rotateY > 0) {
                    rotateY -= 0.001f;
                } else {
                    rotateY += 0.001f;
                }
            }
            cube.rotateY(rotateY);
        }

        if (rotateX != 0) {
            if (Math.abs(rotateX) > 0.001f) {
                if (rotateX > 0) {
                    rotateX -= 0.001f;
                } else {
                    rotateX += 0.001f;
                }
            }
            cube.rotateX(rotateX);
        }
        fb.clear(back);
        world.renderScene(fb);
        world.draw(fb);
        fb.display();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        // TODO Auto-generated method stub  
        if (fb != null) {
            fb.dispose();
        }

        // 创建一个宽度为w,高为h的FrameBuffer
        fb = new FrameBuffer(gl, w, h);
    }

    public float x, y;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // TODO Auto-generated method stub  
        world = new World();
        float[] bounds = world.getBounds(true);
        S.s("bounds:" + Arrays.toString(bounds));
        world.setAmbientLight(255, 255, 255);//灯光

        sun = new Light(world);
        sun.setIntensity(250, 0, 0);

        //纹理
        Texture texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(mActivity
                .getResources().getDrawable(R.drawable.testface)), 1024, 1024));
        TextureManager.getInstance().addTexture("texture", texture);
        //立方体
//        cube = Primitives.getCone(15);//圆锥体
//        cube = Primitives.getCube(10);//正方体
//        cube = Primitives.getBox(1,20f);//正方形底面的长方体
//        cube = Primitives.getCylinder(15);//圆柱体
//        cube = Primitives.getDoubleCone(15);//双圆锥
        cube = Primitives.getEllipsoid(30, 10, 1.0f);//椭圆体,(x-a)^2+(y-b)^2=r^2
//        cube.translate(0, 20, 0);
        if (x == 0 && y == 0) {
            SimpleVector sv_x = cube.getXAxis();
            SimpleVector sv_y = cube.getYAxis();
            x = sv_x.x;
            y = sv_y.y;
        }
//        cube = Primitives.getPlane(6,15);//
//        cube = Primitives.getPyramide(15);//四棱锥
//        cube = Primitives.getSphere(15);//球体
        cube.calcTextureWrapSpherical();
        cube.setTexture("texture");
        cube.strip();
        cube.build();
        world.addObject(cube);

        // 摄像机
        cam = world.getCamera();
        cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);
        cam.lookAt(cube.getTransformedCenter());

        SimpleVector sv = new SimpleVector();
        sv.set(cube.getTransformedCenter());
        sv.y -= 100;
        sv.z -= 100;
        sun.setPosition(sv);
        MemoryHelper.compact();
    }
}  
