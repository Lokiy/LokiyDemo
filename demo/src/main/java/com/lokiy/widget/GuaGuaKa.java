package com.lokiy.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lokiy.demo.R;

public class GuaGuaKa extends View {

	private final float mZoomSize;
	private final int mResId;
	/**
	 * 绘制线条的Paint,即用户手指绘制Path
	 */
	private Paint mOutterPaint = new Paint();
	/**
	 * 记录用户绘制的Path
	 */
	private Path mPath = new Path();
	/**
	 * 内存中创建的Canvas
	 */
	private Canvas mCanvas;
	/**
	 * mCanvas绘制内容在其上
	 */
	private Bitmap mBitmap;

	/**
	 * ------------------------以下是奖区的一些变量
	 */
	// private Bitmap mBackBitmap;
	private boolean isComplete;

//	private Paint mBackPint = new Paint();
//	private Rect mTextBound = new Rect();

	private int mLastX;
	private int mLastY;
	/**
	 * 统计擦除区域任务
	 */
	private Runnable mRunnable = new MyDismissRunnable();

	public GuaGuaKa(Context context) {
		this(context, null);
	}

	public GuaGuaKa(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GuaGuaKa(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GuaGuaKa);
		mZoomSize = a.getFloat(R.styleable.GuaGuaKa_zoomSize, 0f);
		mResId = a.getResourceId(R.styleable.GuaGuaKa_src, R.mipmap.ggk_cover);
		a.recycle();
		init();
	}

	private void init() {
		mPath = new Path();
		// mBackBitmap = BitmapFactory.decodeResource(getResources(),
		// R.drawable.t2);
		setUpOutPaint();
//		setUpBackPaint();

	}

	/**
	 * 设置画笔的一些参数
	 */
	private void setUpOutPaint() {
		// 设置画笔
		// mOutterPaint.setAlpha(0);
		mOutterPaint.setColor(Color.parseColor("#c0c0c0"));
		mOutterPaint.setAntiAlias(true);
		mOutterPaint.setDither(true);
		mOutterPaint.setStyle(Style.STROKE);
		mOutterPaint.setStrokeJoin(Paint.Join.ROUND); // 圆角
		mOutterPaint.setStrokeCap(Paint.Cap.ROUND); // 圆角
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// 告诉父view，我的事件自己处理(解决ViewPager嵌套引起子ViewPager无法触摸问题)
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}

//	/**
//	 * 初始化canvas的绘制用的画笔
//	 */
//	private void setUpBackPaint() {
//		mBackPint.setStyle(Style.FILL);
//		mBackPint.setTextScaleX(2f);
//		mBackPint.setColor(Color.DKGRAY);
//		mBackPint.setTextSize(32);
////		mBackPint.getTextBounds(mText, 0, mText.length(), mTextBound);
//	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				mLastX = x;
				mLastY = y;
				mPath.moveTo(mLastX, mLastY);
				break;
			case MotionEvent.ACTION_MOVE:

				int dx = Math.abs(x - mLastX);
				int dy = Math.abs(y - mLastY);

				if (dx > 3 || dy > 3)
					mPath.lineTo(x, y);

				mLastX = x;
				mLastY = y;
				break;
			case MotionEvent.ACTION_UP:
				new Thread(mRunnable).start();
				break;
		}

		invalidate();
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// canvas.drawBitmap(mBackBitmap, 0, 0, null);
		// 绘制奖项
//		canvas.drawText(mText, getWidth() / 2 - mTextBound.width() / 2, getHeight() / 2 + mTextBound.height() / 2, mBackPint);
		if (!isComplete) {
			drawPath();
			canvas.drawBitmap(mBitmap, 0, 0, null);
		} else {
			setVisibility(GONE);
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mZoomSize != 0 && getVisibility() != GONE) {
			int w;
			int h;
			if (mZoomSize > 0) {
				w = MeasureSpec.getSize(widthMeasureSpec);
				h = (int) (w * mZoomSize);
			} else {
				h = MeasureSpec.getSize(heightMeasureSpec);
				w = (int) (h * mZoomSize * -1);
			}
			setMeasuredDimension(w, h);
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}

		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		if (width > 0 && height > 0 && mBitmap == null) {
			// 初始化bitmap
			mBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);

			// 绘制遮盖层
			// mCanvas.drawColor(Color.parseColor("#c0c0c0"));
			mOutterPaint.setStyle(Style.FILL);
			mCanvas.drawRoundRect(new RectF(0, 0, width, height), 30, 30, mOutterPaint);
			mCanvas.drawBitmap(BitmapFactory.decodeResource(getResources(), mResId), null, new RectF(0, 0, width, height), null);
			// 设置画笔宽度
			mOutterPaint.setStrokeWidth(getResources().getDisplayMetrics().density * 30);
		}
	}

	/**
	 * 绘制线条
	 */
	private void drawPath() {
		mOutterPaint.setStyle(Style.STROKE);
		mOutterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		mCanvas.drawPath(mPath, mOutterPaint);
	}

	private class MyDismissRunnable implements Runnable {
		private int[] mPixels;

		@Override
		public void run() {

			int w = getWidth();
			int h = getHeight();

			float wipeArea = 0;
			float totalArea = w * h;

			Bitmap bitmap = mBitmap;

			mPixels = new int[w * h];

			/**
			 * 拿到所有的像素信息
			 */
			bitmap.getPixels(mPixels, 0, w, 0, 0, w, h);

			/**
			 * 遍历统计擦除的区域
			 */
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					int index = i + j * w;
					if (mPixels[index] == 0) {
						wipeArea++;
					}
				}
			}

			/**
			 * 根据所占百分比，进行一些操作
			 */
			if (wipeArea > 0 && totalArea > 0) {
				int percent = (int) (wipeArea * 100 / totalArea);

				if (percent > 50) {
					isComplete = true;
					postInvalidate();
				}
			}
		}

	}
}
