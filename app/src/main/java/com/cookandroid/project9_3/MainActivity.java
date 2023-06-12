package com.cookandroid.project9_3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    ImageButton ibZoomin, ibZoomout, ibRotate, ibBright,
            ibDar, ibEmbos, ibBlur;
    MyGraphicView graphView;


    static float scaleX =1, scaleY=1;
    static float angle=0;
    static float color=1;
    static boolean blur=false;
    static boolean embos= false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyGraphicView(this));
        setTitle("미니 포토샵(개선)");

//pcitureLayout을 인플레이트 후, MyGraphhicView형 클래스변수 첨부
//아래쪽 레이아웃에, MyGraphicView에서 설정한 내용이 출력됨
        LinearLayout picturLayout=(LinearLayout)
                findViewById(R.id.pictureLayout);
        graphView=(MyGraphicView) new MyGraphicView(this);
        picturLayout.addView(graphView);

//onCreate안에서 메소드 호출
        clickIcons();
    }

    private void clickIcons() {
        ibZoomin=(ImageButton) findViewById(R.id.ibZoomin);
        ibZoomin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scaleX = scaleX +0.2f;
                scaleY=scaleY+0.2f;
                graphView.invalidate();
            }
        });

        ibZoomout=(ImageButton) findViewById((R.id.ibZoomin));
        ibZoomout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scaleX = scaleX -0.2f;
                scaleY=scaleY-0.2f;
                graphView.invalidate();
            }
        });

        ibRotate=(ImageButton)findViewById(R.id.ibRotate);
        ibRotate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                angle=angle+20;
                graphView.invalidate();
            }
        });

// ibGray=(ImageButton)findViewById(R.id.ibDarker);
        ibBright=(ImageButton)findViewById(R.id.ibFlush);
        ibDar=(ImageButton)findViewById(R.id.ibBlur);
        ibEmbos=(ImageButton)findViewById(R.id.ibEmbos);

        ibBright.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                color=color+0.2f;
                graphView.invalidate();
            }
        });

        ibDar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                color=color-0.2f;
                graphView.invalidate();
            }
        });

        ibBlur.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//채도 배수로 쓰일 전역변수 satur: 1이 기본
//0~1이면 채도가 낮게 보이고, 1 이상이면 채도가 높게 보임
//0으로 하면 회색조 이미지
//클릭 시마다 1, 0을 번갈아 함
                if (blur == false) blur=true;
                else blur=false;
                graphView.invalidate();
            }
        });

        ibEmbos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//채도 배수로 쓰일 전역변수 satur: 1이 기본
//0~1이면 채도가 낮게 보이고, 1 이상이면 채도가 높게 보임
//0으로 하면 회색조 이미지
//클릭 시마다 1, 0을 번갈아 함
                if (embos == false) embos=true;
                else embos=false;
                graphView.invalidate();
            }
        });
    }

    private static class MyGraphicView extends View {
        public MyGraphicView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

//화면(뷰)의 중앙 구하고, 전역변수에 설정된 값으로 캔버스 축척 설정
            int cenX=this.getWidth()/2;
            int cenY=this.getHeight()/2;
            canvas.scale(scaleX, scaleY, cenX, cenY);
            canvas.rotate(angle, cenX, cenY);

            Paint paint=new Paint();
//rgb 각 색상의 배율, 알파, 밝기
            float[] array= { color, 0, 0, 0, 0,
                    0, color, 0, 0, 0,
                    0, 0, color, 0, 0,
                    0, 0, 0, 1, 0};
            ColorMatrix cm= new ColorMatrix(array);
//채도값 0일 때, 채도 0으로 해 회색영상(setSaturation)
//1일땐 실행 안함. 이때, setSaturation()메소드 실행시
//앞의 컬러 매트릭스 값이 무시된다.
            if(blur==true) {
                BlurMaskFilter bMask;
                bMask=new BlurMaskFilter(30, BlurMaskFilter.Blur.NORMAL);
                paint.setMaskFilter(bMask);
            }
            if(embos==true) {
                EmbossMaskFilter eMask;
                eMask=new EmbossMaskFilter(new float[] { 3, 3, 3}, 0.5f,
                        5, 10);
                paint.setMaskFilter(eMask);
            }
            paint.setColorFilter(new ColorMatrixColorFilter(cm));

            Bitmap picture= BitmapFactory.decodeResource(getResources(),
                    R.drawable.lena256);
            int picX=(this.getWidth()-picture.getWidth())/2;
            int picY=(this.getWidth()-picture.getWidth())/2;

//본래의 darwBitmap의 마지막 패러미터를 null을 paint로 변경
            canvas.drawBitmap(picture, picX, picY, paint);

            picture.recycle();
        }
    }
}