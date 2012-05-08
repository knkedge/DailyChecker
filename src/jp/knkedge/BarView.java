package jp.knkedge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class BarView extends View {
	DailyWork work;

	public BarView(Context context) {
		super(context);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public BarView(Context context, AttributeSet attr) {
		super(context, attr);
	}

	public void onDraw(Canvas canvas) {
		Paint p = new Paint();
		p.setColor(Color.LTGRAY);
		int mRight = getRight();
		int mLeft = getLeft();
		int mTop = getTop();
		int mBottom = getBottom();
		float tRight = (float)mRight-mLeft-3;
		float tLeft = 3f;
		float tTop = 5f;
		float tBottom = (float)mBottom-mTop-5;
		// CanvasへのDrawは、"Canvas内での座標"で指定する
		canvas.drawRoundRect(new RectF(tLeft, tTop, tRight, tBottom), 2f, 2f, p);
		float perRight = tLeft + (tRight-tLeft) * perExp();
		Paint perp = new Paint();
		perp.setColor(Color.YELLOW);
		canvas.drawRoundRect(new RectF(tLeft, tTop, perRight, tBottom), 2f, 2f, perp);
	}

	private float perExp() {
		if (work == null) {
			return 0f;
		}
		float exp = (float)work.getExp();
		float need = (float)DailyWork.getNeccessaryExp(work.getLevel()+1);
		return exp/need;
	}

	public void setItem(DailyWork work) {
		this.work = work;
	}
}
