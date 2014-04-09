package com.example.main_old_or_out;
//package com.example.main;
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.graphics.PointF;
//import android.os.Bundle;
//import android.util.FloatMath;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnTouchListener;
//import android.widget.ImageView;
//
//public class DetailPicture extends Activity {
//	private static final int NONE = 0;
//	private static final int DRAG = 1;
//	private static final int ZOOM = 2;
//	
//	private int mode = NONE;
//	private float oldDist;
//	private Matrix matrix = new Matrix();
//	private Matrix savedMatrix = new Matrix();
//	private PointF start = new PointF();
//	private PointF mid = new PointF();
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.detail_picture);
//		ImageView view = (ImageView) findViewById(R.id.image_view);
//		
//		Intent intent = getIntent();
//		Bundle data = intent.getExtras();
//		final byte[]  Image_picture = (byte[])data.getSerializable("Image_picture");
//		view.setImageBitmap(Bytes2Bimap(Image_picture));
//		
//		view.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				ImageView view = (ImageView) v;
//				switch (event.getAction() & MotionEvent.ACTION_MASK) {
//				case MotionEvent.ACTION_DOWN:
//					savedMatrix.set(matrix);
//					start.set(event.getX(), event.getY());
//					mode = DRAG;
//					break;
//				case MotionEvent.ACTION_UP:
//				case MotionEvent.ACTION_POINTER_UP:
//					mode = NONE;
//					break;
//				case MotionEvent.ACTION_POINTER_DOWN:
//					oldDist = spacing(event);
//					if (oldDist > 10f) {
//						savedMatrix.set(matrix);
//						midPoint(mid, event);
//						mode = ZOOM;
//					}
//					break;
//				case MotionEvent.ACTION_MOVE:
//					if (mode == DRAG) {
//						matrix.set(savedMatrix);
//						matrix.postTranslate(event.getX() - start.x, event.getY()
//								- start.y);
//					} else if (mode == ZOOM) {
//						float newDist = spacing(event);
//						if (newDist > 10f) {
//							matrix.set(savedMatrix);
//							float scale = newDist / oldDist;
//							matrix.postScale(scale, scale, mid.x, mid.y);
//						}
//					}
//					break;
//				}
//
//				view.setImageMatrix(matrix);
//				return true;
//			}
//			
//			private float spacing(MotionEvent event) {
//				float x = event.getX(0) - event.getX(1);
//				float y = event.getY(0) - event.getY(1);
//				return FloatMath.sqrt(x * x + y * y);
//			}
//
//			private void midPoint(PointF point, MotionEvent event) {
//				float x = event.getX(0) + event.getX(1);
//				float y = event.getY(0) + event.getY(1);
//				point.set(x / 2, y / 2);
//			}});
//	}
//	
//	public Bitmap Bytes2Bimap(byte[] b) {
//        if (b.length != 0) {
//            return BitmapFactory.decodeByteArray(b, 0, b.length);
//        } else {
//            return null;
//        }
//    }
//}