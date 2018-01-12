package com.zhibaowang.test;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

import com.threed.jpct.Camera;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.zhibaowang.app.R;
import com.zhibaowang.component.ZBoxView;
import com.zhibaowang.tools.B;
import com.zhibaowang.tools.S;
import com.zhibaowang.tools.ZScreenTool;
import com.zhibaowang.ui.ZBaseActivity;

public class TestActivity extends ZBaseActivity {
    //    ZShowView zButton;
    ZBoxView zButton;
    private GLSurfaceView glView;
    private MyRenderer mr = new MyRenderer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int wh[] = ZScreenTool.getWH(this);
        final int w_screen = wh[0];
        final int h_screen = wh[1];
        mr.bitmap = B.getBitmapById_Percent(this, R.drawable.test_icon, 1);
        // 传入Resources方法
        glView = new GLSurfaceView(this) {
            private float x_lastmove, y_lastmove;
            private float angle_x_last, angle_z_last;
            World world;

            @Override
            public boolean onTouchEvent(MotionEvent event) {
                float radius = 100;
                float angle_everytime = 0.001f;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (world == null) {
                            world = mr.world;
                        }
                        x_lastmove = event.getX();
                        y_lastmove = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        S.s("-----------------------------------------move");
                        float distance_x = event.getX() - x_lastmove;
                        float distance_y = event.getY() - y_lastmove;
                        S.s("x轴移动距离:" + distance_x);
                        S.s("y轴移动距离:" + distance_y);
                        x_lastmove = event.getX();
                        y_lastmove = event.getY();
                        S.s("x轴上次的坐标:" + x_lastmove);
                        S.s("y轴上次的坐标:" + y_lastmove);
                        Camera camera = world.getCamera();
                        float angle_x_move = 0;
                        float angle_z_move = 0;
                        if (distance_y == 0) {
                            angle_x_move = angle_everytime;
                            angle_z_move = 0;
                        }
                        if (distance_x == 0) {
                            angle_z_move = angle_everytime;
                            angle_x_move = 0;
                        } else {

                        }
                        float percent = 0;
                        if (distance_y != 0) {
                            percent = distance_x / distance_y;
                        }

                        S.s("x/y:" + percent);
                        angle_z_move = (float) Math.sqrt(Math.pow(angle_everytime, 2) / (1 + Math.pow(percent, 2)));
                        S.s("求得z轴方向上转动角度为:" + angle_z_move + " [上次总角度:" + angle_z_last + "]");
                        angle_x_move = percent * angle_z_move;
                        S.s("求得x轴方向上转动角度为:" + angle_x_move + " [上次总角度:" + angle_x_last + "]");
                        angle_x_last = (angle_x_last + angle_x_move);
                        angle_z_last = (angle_z_last + angle_z_move);

                        angle_x_last %= 360;
                        angle_z_last %= 360;

                        float x_camera = (float) (radius * Math.sin(Math.toDegrees(angle_x_last)));
                        float y_camera = (float) (radius * Math.cos(Math.toDegrees(angle_x_last)));
                        float z_camera = (float) (y_camera * Math.tan(Math.toDegrees(angle_z_last)));
                        S.s("求得球面三坐标为:x:" + x_camera + " y:" + y_camera + " z:" + z_camera);
                        SimpleVector sv = new SimpleVector(x_camera, y_camera, z_camera);
                        SimpleVector sv2 = new SimpleVector(-x_camera, -y_camera, -z_camera);
                        camera.setPosition(sv);
                        camera.lookAt(sv2);
                        break;
                }
                return true;
            }
        };
        glView.setRenderer(mr);

        setContentView(glView);
//        setContentView(R.layout.activity_test);
//         zButton=findViewById(R.id.zshowview);

//        zButton.setDrawable_center(QRCodeTool.getQRCodeBitmap("hahaa",300,300));
    }

    @Override
    public void finish() {
        zButton.close();
        super.finish();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
