/**
 * Copyright (C) 2014 Luki(liulongke@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lokiy.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lokiy.demo.R;

public class ZoomRelativeLayout extends RelativeLayout {
	private float mZoomSize = 0;

	public ZoomRelativeLayout(Context context) {
		this(context, null);
	}

	public ZoomRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ZoomRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZoomRelativeLayout);
		mZoomSize = a.getFloat(R.styleable.ZoomRelativeLayout_zoomSize, 0f);
		a.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (mZoomSize != 0 && getVisibility() != GONE) {
			ViewGroup.LayoutParams layoutParams =  getLayoutParams();
			if (mZoomSize > 0) {
				int height = (int) (getMeasuredWidth() * mZoomSize);
				if (layoutParams.height != height) {
					layoutParams.height = height;
					requestLayout();
				}
			} else {
				int width = (int) (getMeasuredHeight() * -mZoomSize);
				if (layoutParams.width != width) {
					layoutParams.width = width;
					requestLayout();
				}
			}
		}
	}
}
