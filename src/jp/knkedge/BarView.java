package jp.knkedge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class BarView extends View {
	DailyWork work;

	public BarView(Context context) {
		super(context);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public void onDraw(Canvas canvas) {
		Paint p = new Paint();
		p.setColor(Color.RED);
		int mRight = getRight();
		int mLeft = getLeft();
		int mTop = getTop();
		int mBottom = getBottom();
		canvas.drawRect(mLeft, mTop, mRight, mBottom, p);
	}

	public void setItem(DailyWork work) {
		this.work = work;
	}
}
