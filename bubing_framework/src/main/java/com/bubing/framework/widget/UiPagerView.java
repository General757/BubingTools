package com.bubing.framework.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class UiPagerView extends AdapterView<ListAdapter>
{

	public interface PositionListener
	{
		public void onPositionChange(int position);
	}

	class DataObserver extends DataSetObserver
	{
		public void onChanged()
		{
			dataChanged = true;
			requestLayout();
		}

		public void onInvalidated()
		{
			dataChanged = true;
			requestLayout();
		}
	}

	PositionListener		listener;

	boolean					autoTurn	= false;

	Object lockObject;

	Scroller mScroller;

	int						mTouchSlop;

	int						maxumFling;

	int						minumFling;

	int						pointer_id;

	float					touchFromX;

	int						slideFromX;

	boolean					allow_slop;

	VelocityTracker ev_tracker;

	ListAdapter listAdapter;

	LinkedList<View> activeslist;

	LinkedList<View> recyclelist;

	LinkedList<View> destroylist;

	HashMap<View, String> positionmap;

	boolean					dataChanged;

	DataObserver			dataObserver;

	TurnRunnable			turnRunnable;

	public UiPagerView(Context context)
	{
		this(context, null);
	}

	public UiPagerView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public UiPagerView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		lockObject = new Object();
		mScroller = new Scroller(context);

		dataObserver = new DataObserver();

		activeslist = new LinkedList<View>();
		recyclelist = new LinkedList<View>();
		destroylist = new LinkedList<View>();
		positionmap = new HashMap<View, String>();

		ViewConfiguration conf = ViewConfiguration.get(context);
		mTouchSlop = conf.getScaledTouchSlop();
		maxumFling = conf.getScaledMaximumFlingVelocity();
		minumFling = conf.getScaledMinimumFlingVelocity();
	}

	@Override
	public ListAdapter getAdapter()
	{
		return null;
	}

	@Override
	public View getSelectedView()
	{
		return null;
	}

	public void setPositionListener(PositionListener listener)
	{
		this.listener = listener;
	}

	@Override
	public void setAdapter(ListAdapter adapter)
	{
		if (listAdapter != null)
		{
			listAdapter.unregisterDataSetObserver(dataObserver);
		}

		listAdapter = adapter;

		if (listAdapter != null)
		{
			listAdapter.registerDataSetObserver(dataObserver);
		}

		dataChanged = true;
		requestLayout();
	}

	public void startAutoTurn()
	{
		autoTurn = true;
		if (turnRunnable != null)
		{
			removeCallbacks(turnRunnable);
		}
		turnRunnable = new TurnRunnable();
		postDelayed(turnRunnable, 2000);
	}

	@Override
	public void setSelection(int position)
	{

	}

	@Override
	public void computeScroll()
	{
		if (mScroller.computeScrollOffset())
		{
			int scrollX = mScroller.getCurrX();
			scrollTo(scrollX, 0);
			refreshlist();
			invalidate();

			if (mScroller.isFinished())
			{
				int width = getMeasuredWidth();
				int position = scrollX / width;
				int count = getCount();

				if (position > -1 && position < count && listener != null)
				{
					listener.onPositionChange(position);
				}

				if (scrollX % width == 0 && autoTurn && turnRunnable == null)
				{
					turnRunnable = new TurnRunnable();
					postDelayed(turnRunnable, 3000);
				}
			}

		}
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY)
		{
			throw new RuntimeException(getClass().getName() + " width and height must exactly");
		}

		setMeasuredDimension(widthSize, heightSize);

		checkScroll();

		int count = getChildCount();

		if (dataChanged)
		{
			removeChild();
		}

		refreshlist();

		if (count == 0 && getChildCount() > 0 && listener != null)
		{
			listener.onPositionChange(0);
		}
	}

	private void checkScroll()
	{
		int now = mScroller.isFinished() ? getScrollX() : mScroller.getFinalX();
		int max = getMeasuredWidth() * (getCount() - 1);
		max = Math.max(0, max);

		if (now > max)
		{
			mScroller.abortAnimation();
			scrollTo(max, 0);
		}
	}

	private void removeChild()
	{
		synchronized (lockObject)
		{
			View child = null;

			int count = activeslist.size();

			for (int index = count - 1; index >= 0; index--)
			{
				child = activeslist.get(index);
				removeChild(child);
			}
		}
	}

	private void removeChild(View child)
	{
		cleanupLayoutState(child);
		removeViewInLayout(child);

		positionmap.remove(child);
		activeslist.remove(child);
		recyclelist.add(child);
	}

	private void appendChild(int index)
	{
		View convertView = null;

		if (!recyclelist.isEmpty())
		{
			convertView = recyclelist.remove(0);
		}

		convertView = listAdapter.getView(index, convertView, this);
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();

		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		int paddingTop = getPaddingTop();
		int paddingBottom = getPaddingBottom();

		LayoutParams params = new LayoutParams(width - paddingLeft - paddingRight, height - paddingTop - paddingBottom);
		addViewInLayout(convertView, getChildCount(), params);
		positionmap.put(convertView, String.valueOf(index));
		activeslist.add(convertView);

		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width - paddingLeft - paddingRight, MeasureSpec.EXACTLY);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(height - paddingTop - paddingBottom, MeasureSpec.EXACTLY);

		convertView.measure(widthMeasureSpec, heightMeasureSpec);

		int left = width * index + paddingLeft;
		int right = width * (index + 1) - paddingRight;
		int top = paddingTop;
		int bottom = height - paddingBottom;

		convertView.layout(left, top, right, bottom);
	}

	private void refreshlist()
	{
		synchronized (lockObject)
		{
			int width = getMeasuredWidth();

			int scrollX = getScrollX();

			int count = getCount();

			int from = (scrollX - width) / width;
			from = Math.max(0, from);

			int stop = (scrollX + width) / width;
			stop = Math.min(count - 1, stop);

			Set<View> set = positionmap.keySet();
			for (View child : set)
			{
				String value = positionmap.get(child);
				int index = Integer.valueOf(value);

				if (index < from || index > stop)
				{
					destroylist.add(child);
				}
			}

			for (View child : destroylist)
			{
				removeChild(child);
			}

			destroylist.clear();

			for (int index = from; index <= stop; index++)
			{
				String value = String.valueOf(index);

				if (!positionmap.containsValue(value))
				{
					appendChild(index);
				}
			}
		}
	}

	public int getCount()
	{
		return listAdapter != null ? listAdapter.getCount() : 0;
	}

	public void onSecondaryPointerUp(MotionEvent ev)
	{
		final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		final int id = ev.getPointerId(pointerIndex);

		if (id == pointer_id)
		{
			if (ev_tracker != null)
			{
				ev_tracker.clear();
			}

			int newIndex = pointerIndex == 0 ? 1 : 0;
			pointer_id = ev.getPointerId(newIndex);
			touchFromX = ev.getX(newIndex);
			slideFromX = getScrollX();
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		if (turnRunnable != null)
		{
			removeCallbacks(turnRunnable);
			turnRunnable = null;
		}

		int action = ev.getActionMasked();
		boolean intercept = false;

		switch (action)
		{
			case MotionEvent.ACTION_DOWN:
			{
				allow_slop = false;
				if (!mScroller.isFinished())
				{
					intercept = true;
				}
				pointer_id = ev.getPointerId(0);
				slideFromX = getScrollX();
				touchFromX = ev.getX();
				break;
			}
			case MotionEvent.ACTION_MOVE:
			{
				int index = ev.findPointerIndex(pointer_id);
				int diffX = (int) (ev.getX(index) - touchFromX);

				if (Math.abs(diffX) > mTouchSlop)
				{
					intercept = true;
					allow_slop = true;

					ViewParent parent = getParent();
					if (parent != null)
					{
						parent.requestDisallowInterceptTouchEvent(true);
					}
				}
				break;
			}
			case MotionEvent.ACTION_POINTER_UP:
			{
				onSecondaryPointerUp(ev);
				break;
			}
		}

		return intercept;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if (turnRunnable != null)
		{
			removeCallbacks(turnRunnable);
			turnRunnable = null;
		}

		int action = ev.getActionMasked();

		if (ev_tracker == null)
		{
			ev_tracker = VelocityTracker.obtain();
		}

		ev_tracker.addMovement(ev);

		switch (action)
		{
			case MotionEvent.ACTION_DOWN:
			{
				allow_slop = false;

				if (!mScroller.isFinished())
				{
					mScroller.abortAnimation();
					allow_slop = true;
				}

				pointer_id = ev.getPointerId(0);
				slideFromX = getScrollX();
				touchFromX = ev.getX();
				break;
			}
			case MotionEvent.ACTION_MOVE:
			{
				int index = ev.findPointerIndex(pointer_id);
				int diffX = (int) (touchFromX - ev.getX(index));

				if (!allow_slop && Math.abs(diffX) > mTouchSlop)
				{
					allow_slop = true;
					ViewParent parent = getParent();

					if (parent != null)
					{
						parent.requestDisallowInterceptTouchEvent(true);
					}
				}

				if (allow_slop)
				{
					int scrollX = slideFromX + diffX;
					scrollTo(scrollX, 0);
					refreshlist();
				}
				break;
			}
			case MotionEvent.ACTION_POINTER_UP:
			{
				onSecondaryPointerUp(ev);
				break;
			}
			case MotionEvent.ACTION_CANCEL:
			{
				onCancleOrUp();
				break;
			}
			case MotionEvent.ACTION_UP:
			{
				onCancleOrUp();
				break;
			}
		}

		return true;
	}

	private void onCancleOrUp()
	{
		ev_tracker.computeCurrentVelocity(1000, maxumFling);
		int velocityX = (int) ev_tracker.getXVelocity();
		ev_tracker.recycle();
		ev_tracker = null;

		int width = getMeasuredWidth();
		int index = getScrollX() / width;

		int startX = getScrollX();
		int finalX = index * width;

		if (Math.abs(velocityX) > minumFling)
		{
			if (velocityX < 0)
			{
				index = startX >= finalX ? index + 1 : index;
			}
			else
			{
				index = startX <= finalX ? index - 1 : index;
			}
		}
		else
		{
			index = (startX + width / 2) / width;
		}

		index = Math.min(getCount() - 1, index);
		index = Math.max(0, index);

		scrollToDestination(index);
	}

	private void scrollToDestination(int index)
	{
		int width = getMeasuredWidth();
		int finalX = index * width;
		int startX = getScrollX();

		int duration = Math.abs(finalX - startX) * 2;
		duration = Math.min(duration, 280);

		if (startX != finalX)
		{
			mScroller.startScroll(startX, 0, finalX - startX, 0, duration);
			invalidate();
		}
	}

	class TurnRunnable implements Runnable
	{
		@Override
		public void run()
		{
			int scrollX = getScrollX();
			int width = getMeasuredWidth();

			if (width != 0)
			{
				int position = scrollX / width + 1;

				if (position >= getCount())
				{
					position = 0;
				}

				scrollToDestination(position);
			}
			turnRunnable = null;
		}
	}

	public void setPageIndex(int position)
	{
		scrollToDestination(position);
	}
}
