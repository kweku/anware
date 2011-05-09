package net.hutspace.anware;

import net.hutspace.anware.core.Game;
import net.hutspace.anware.core.IllegalMove;
import net.hutspace.anware.core.NamNamGame;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class Board extends RelativeLayout {
	private GameActivity activity;
	private Game game;
	private static final String TAG = "Board";
	static final int below = RelativeLayout.BELOW;
	static final int leftOf = RelativeLayout.LEFT_OF;
	static final int rightOf = RelativeLayout.RIGHT_OF;
	static final int above = RelativeLayout.ABOVE;

	public Board(Context context) {
		super(context);
		init(context);
	}

	public Board(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public Board(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public void setGame(final Game game) {
		//this.game = game;
	}
	
	public int seeds(final int i) {
		return game.pit(i - 1);
	}
	
	public void move(final int i) {
		try {
			game.move(i - 1);
			invalidate();
		} catch (IllegalMove e) {
			startAnimation(AnimationUtils.loadAnimation(activity, R.anim.shake));
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		TView center = addCenter();
		drawTopPits(center.getId());
		drawBottomPits(center.getId());
		drawStores();
	}

	private void init(Context context) {
		Log.d(TAG, "init()");
		game = new NamNamGame();
		activity = (GameActivity) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
		setGravity(Gravity.CENTER);
		setWillNotDraw(false);
	}

	private RelativeLayout.LayoutParams cp() {
		final RelativeLayout.LayoutParams p = lp();
		p.addRule(RelativeLayout.CENTER_IN_PARENT);
		return p;
	}

	private void drawTopPits(final int cId) {
		addView(createPit(12, new int[][] {{above, cId}, {leftOf, 11}}));
		addView(createPit(11, new int[][] {{above, cId}, {leftOf, 10}}));
		addView(createPit(10, new int[][] {{above, cId}, {leftOf, cId}}));
		addView(createPit(9, new int[][] {{above, cId}, {rightOf, cId}}));
		addView(createPit(8, new int[][] {{above, cId}, {rightOf, 9}}));
		addView(createPit(7, new int[][] {{above, cId}, {rightOf, 8}}));
	}
	
	private void drawBottomPits(final int cId) {
		addView(createPit(1, new int[][] {{below, cId}, {leftOf, 2}}));
		addView(createPit(2, new int[][] {{below, cId}, {leftOf, 3}}));
		addView(createPit(3, new int[][] {{below, cId}, {leftOf, cId}}));
		addView(createPit(4, new int[][] {{below, cId}, {rightOf, cId}}));
		addView(createPit(5, new int[][] {{below, cId}, {rightOf, 4}}));
		addView(createPit(6, new int[][] {{below, cId}, {rightOf, 5}}));
	}

	private void drawStores() {
		addView(updatePit(createStore(1000), new int[][] {{rightOf, 6}}, cp()));
		addView(updatePit(createStore(2000), new int[][] {{leftOf, 12}}, cp()));
	}

	private TView addCenter() {
		TView center = new TView(getContext());
		center.setId(100);
		center.setLayoutParams(cp());
		addView(center);
		return center;
	}

	private Pit createPit(final int id) {
		final Context context = getContext();
		final Pit pit = new Pit(context);
		pit.setId(id);
		return pit;
	}

	private Pit createStore(final int id) {
		final Context context = getContext();
		final Pit pit = new Store(context);
		pit.setId(id);
		return pit;
	}
	
	private Pit createPit(final int id, int[][] rules) {
		return updatePit(createPit(id), rules, lp());
	}

	private Pit updatePit(final Pit pit, int[][] rules,
			final RelativeLayout.LayoutParams p) {
		for (int i = 0; i < rules.length; ++i)
			p.addRule(rules[i][0], rules[i][1]);
		pit.setLayoutParams(p);
		return pit;
	}

	private RelativeLayout.LayoutParams lp() {
		// float dpi = getResources().getDisplayMetrics().density;
		// return new RelativeLayout.LayoutParams((int)(Pit.W * dpi),
		// (int)(Pit.W * dpi));
		final int wc = LayoutParams.WRAP_CONTENT;
		return new RelativeLayout.LayoutParams(wc, wc);
	}
	
	private static class TView extends View {
		int width = 2;
		int height = 10;
		
		public TView(Context context) {
			super(context);
		}
		
		public TView(Context context, int w, int h) {
			super(context);
			width = w;
			height = h;
		}
		
		@Override
		public void onMeasure(int x, int y) {
			float dpi = getResources().getDisplayMetrics().density;
			setMeasuredDimension((int) (width * dpi), (int) (height * dpi));
		}

		@Override
		protected void onDraw(Canvas canvas) {
			paint.setColor(Color.DKGRAY);
			canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
		}

		private static Paint paint = new Paint();

	}

}

