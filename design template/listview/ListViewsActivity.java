package com.csform.android.uiapptemplate;

import java.util.Random;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.csform.android.uiapptemplate.adapter.DefaultAdapter;
import com.csform.android.uiapptemplate.util.DummyContent;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;

public class ListViewsActivity extends AppCompatActivity {

	public static final String LIST_VIEW_OPTION = "com.csform.android.uiapptemplate.ListViewsActivity";

	private DynamicListView mDynamicListView;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_views);

		mDynamicListView = (DynamicListView) findViewById(R.id.dynamic_listview);

		String category = CategoriesListViewActivity.LIST_VIEW_OPTION_EXPANDABLE;
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(LIST_VIEW_OPTION)) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
				category = extras.getString(LIST_VIEW_OPTION,
						CategoriesListViewActivity.LIST_VIEW_OPTION_EXPANDABLE);
			} else {
				category = extras.getString(LIST_VIEW_OPTION);
			}
		}

		setUpListView(category);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setUpListView(String category) {
		if (category
				.equals(CategoriesListViewActivity.LIST_VIEW_OPTION_DRAG_AND_DROP)) {
			setUpDragAndDrop();
			Toast.makeText(this, "Long press an item to start dragging",
					Toast.LENGTH_SHORT).show();
		} else if (category
				.equals(CategoriesListViewActivity.LIST_VIEW_OPTION_SWIPE_TO_DISSMISS)) {
			setUpSwipeToDissmiss();
		} else if (category
				.equals(CategoriesListViewActivity.LIST_VIEW_OPTION_APPERANCE_ALPHA)) {
			appearanceAnimate(0);
		} else if (category
				.equals(CategoriesListViewActivity.LIST_VIEW_OPTION_APPERANCE_RANDOM)) {
			appearanceAnimate(new Random().nextInt(5));
		}
	}

	private void setUpDragAndDrop() {
		final DefaultAdapter adapter = new DefaultAdapter(this,
				DummyContent.getDummyModelList(), true);
		mDynamicListView.setAdapter(adapter);
		mDynamicListView.enableDragAndDrop();
		mDynamicListView.setDraggableManager(new TouchViewDraggableManager(
				R.id.icon));
		mDynamicListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						mDynamicListView.startDragging(position);
						return true;
					}
				});
	}

	private void setUpSwipeToDissmiss() {
		final DefaultAdapter adapter = new DefaultAdapter(this,
				DummyContent.getDummyModelList(), false);
		SimpleSwipeUndoAdapter swipeUndoAdapter = new SimpleSwipeUndoAdapter(
				adapter, this, new OnDismissCallback() {
					@Override
					public void onDismiss(@NonNull final ViewGroup listView,
							@NonNull final int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							adapter.remove(position);
						}
					}
				});
		swipeUndoAdapter.setAbsListView(mDynamicListView);
		mDynamicListView.setAdapter(swipeUndoAdapter);
		mDynamicListView.enableSimpleSwipeUndo();
	}

	private void appearanceAnimate(int key) {
		BaseAdapter adapter = new DefaultAdapter(this,
				DummyContent.getDummyModelList(), false);
		AnimationAdapter animAdapter;
		switch (key) {
		default:
		case 0:
			animAdapter = new AlphaInAnimationAdapter(adapter);
			break;
		case 1:
			animAdapter = new ScaleInAnimationAdapter(adapter);
			break;
		case 2:
			animAdapter = new SwingBottomInAnimationAdapter(adapter);
			break;
		case 3:
			animAdapter = new SwingLeftInAnimationAdapter(adapter);
			break;
		case 4:
			animAdapter = new SwingRightInAnimationAdapter(adapter);
			break;
		}
		animAdapter.setAbsListView(mDynamicListView);
		mDynamicListView.setAdapter(animAdapter);
	}
}
