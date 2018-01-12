package com.zhibaowang.test;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;
import android.view.View;

import com.threed.jpct.Camera;
import com.threed.jpct.Config;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;

/**
 * MyRenderer类
 *
 * @author Administrator
 */
public class MyRenderer implements Renderer {
    // FrameBuffer对象
    private FrameBuffer fb;
    // World对象
    public World world;
    // RGBColor
    private RGBColor back = new RGBColor(0, 0, 0);
    // Object3D对象
    private Object3D sphere1 = null;
    private Object3D sphere2 = null;
    private Object3D sphere3 = null;
    private Object3D cube = null;



    private SimpleVector tmp = new SimpleVector();

    // FPS
    private int fps = 0;
    private long time = System.currentTimeMillis();
    private boolean stop = false;


    SimpleVector sv_start=new SimpleVector(0,0,-80);
    // 默认构造
    // 对该项目的一些优化
    public MyRenderer() {
        // 绘制的最多的Polygon数量,默认为4096,此处如果超过500，则不绘制
        Config.maxPolysVisible = 500;
        // 最远的合适的平面,默认为1000
        Config.farPlane = 1500;
        // Modifies the multiplicator for the transparency calculations in
        // jPCT-AE. The actual formula is trans=offset+objTrans*mul, default for
        // offset is 0.1f.
        Config.glTransparencyMul = 0.1f;
        // Modifies the offset for the transparency calculations in jPCT-AE. The
        // actual formula is trans=offset+objTrans*mul, default for offset is
        // 0.1f.
        Config.glTransparencyOffset = 0.1f;
        // 使JPCT-AE这个引擎使用顶点而不是顶点数组缓冲对象，因为它可能会使某些硬件更快
        // 但在Samsung Galaxy,它并不能工作的很好，可能使之崩溃，这就是它默认为false的原因
        Config.useVBO = true;

        Texture.defaultToMipmapping(true);
        Texture.defaultTo4bpp(true);
    }

    public void onDrawFrame(GL10 gl) {
        try {
            if (!stop) {
                // Do collision detections(碰撞检测)

                // 第1个参数为cube的移动(translation)
                // 第2个参数为碰撞的区域大小(ellips)
                // 此处的第3个参数为:递归深度的碰撞检测，较高的值将提高碰撞检测精度，而且降低性能。合理的值介于1和5。
                // trsn包含当前的坐标信息
                SimpleVector move = new SimpleVector(0, 0, 1);
                SimpleVector ellips = new SimpleVector(7, 7, 7);
                SimpleVector trsn = cube.checkForCollisionEllipsoid(move, ellips, 5);
                // 用下面的球体间碰撞也能很好的工作
                // SimpleVector trsn = cube.checkForCollisionSpherical(move, 2);
                cube.translate(trsn);
//                Camera camera=world.getCamera();
//                camera.setPosition(0,-100,0);
//                camera.rotateX(-0.001f);
//                if (cube.getTranslation(tmp).z > 500) {
//                    cube.clearTranslation();
//                    cube.translate(sv_start);
//                }

                // 以定义好的RGBColor清屏
                fb.clear(back);
                // 变换和灯光所有的多边形
                world.renderScene(fb);
                // 绘制由renderScene产生的场景
                world.draw(fb);
                // 渲染显示图像
                fb.display();
                // fps加1
                fps++;
                // 打印输出fps
                if (System.currentTimeMillis() - time > 1000) {
                    System.out.println(fps + "fps");
                    fps = 0;
                    time = System.currentTimeMillis();
                }
            } else {
                if (fb != null) {
                    fb.dispose();
                    fb = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 打印异常信息
            Logger.log("Drawing thread terminated!", Logger.MESSAGE);
        }
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (fb != null) {
            fb = null;
        }
        // 新产生一个FrameBuffer对象
        fb = new FrameBuffer(gl, width, height);
    }
    public Bitmap bitmap;
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Logger.log("onCreate");
        // 混合渲染
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        // 新建world对象
        world = new World();
        // 纹理相关
        TextureManager tm = TextureManager.getInstance();
        Texture texture2 = new Texture(BitmapHelper.rescale(bitmap,1024,1024));
        tm.addTexture("texture2", texture2);
        // 初始化各3D元素

        // 返回一个给定数量的面,第一个20表示面的数量,第二是球体的缩放
        sphere1 = Primitives.getSphere(20, 10);
        // 以纹理对象的方式给对象包装上纹理
        sphere1.calcTextureWrapSpherical();
        // 为sphere1设置纹理图片
        sphere1.setTexture("texture2");

        // 返回一个立方体，其中2为其缩放变量
        cube = Primitives.getCube(2);
        // 以(10, -40, -30)来旋转立方体
        cube.translate(sv_start);
        cube.setAdditionalColor(RGBColor.GREEN);

        // 复制当前的Object3D对象
        sphere2 = sphere1.cloneObject();
        sphere3 = Primitives.getSphere(20,5);
        // 以Z轴20处来旋转球体(其实就是向里移动球体)
        sphere2.translate(20, 0, 0);
        // 以Z轴40处来旋转球体
        sphere3.translate(0, 20, 0);


        // 将3D元素添加到world对象中
        world.addObject(sphere1);
        world.addObject(sphere2);
        world.addObject(sphere3);
        world.addObject(cube);

        // 建立碰撞模式
        sphere1.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
        world.addObject(sphere1);
        sphere2.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
        world.addObject(sphere2);
        sphere3.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
        world.addObject(sphere3);
        // 建立碰撞模式
        cube.setCollisionMode(Object3D.COLLISION_CHECK_SELF);

        // 设置环境光
        world.setAmbientLight(255, 255, 255);
        // 编译所有对象
        world.buildAllObjects();

        // Camera相关
        Camera cam = world.getCamera();
//        // 向里以50的速度移动
//        cam.moveCamera(new SimpleVector(100,100,100), 100);
        // 向外以60的速度移动
        cam.setPosition(0,0,-100);
//        cam.rotateCameraX(45);
        // 以sphere2对象作为中心视角
        cam.lookAt(sphere1.getTransformedCenter());
//        cam.lookAt(new SimpleVector(-100,-100,-100));

        // 回收内存
        MemoryHelper.compact();
    }





}
