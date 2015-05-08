package org.zywx.wbpalmstar.plugin.uexSearchBarView;

import java.io.Serializable;

import org.json.JSONObject;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

@SuppressWarnings({ "deprecation", "serial" })
public class EUExSearchBarView extends EUExBase implements Serializable {
	
	private LocalActivityManager mgr;
	private String SCRIPT_HEADER = "javascript:";
	private String F_CALLBACK_NAME_ONITEMCLICK = "uexSearchBarView.onItemClick";
	private ESearchBarViewDataModel model;


	public EUExSearchBarView(Context context, EBrowserView eBrowserView) {
		super(context, eBrowserView);
		mgr = ((ActivityGroup)mContext).getLocalActivityManager();
	}

	@Override
	protected boolean clean() {
		close(null);
		return false;
	}
	
	public void open(String[] params) {
		sendMessageInSearchBar(ESearchBarViewUtils.SEARCHBAR_MSG_CODE_OPEN, params);
	}
	
	public void close(String[] params) {
		sendMessageInSearchBar(ESearchBarViewUtils.SEARCHBAR_MSG_CODE_CLOSE, params);
	}
	
	public void setViewStyle(String[] params) {
		sendMessageInSearchBar(ESearchBarViewUtils.SEARCHBAR_MSG_CODE_SETVIEWSTYLE, params);
	}
	
	public void clearHistory(String[] params) {
		sendMessageInSearchBar(ESearchBarViewUtils.SEARCHBAR_MSG_CODE_CLEARHISTORY, params);
	}
	
	private void sendMessageInSearchBar(int msgCode, String[] params) {
		if(mHandler == null) {
			return;
		}
		Message msg = Message.obtain();
		msg.what = msgCode;
		msg.obj = this;
		Bundle bundle = new Bundle();
		bundle.putStringArray(ESearchBarViewUtils.SEARCHBAR_MSG_CODE_FUNCTION, params);
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}
	
	@Override
	public void onHandleMessage(Message msg) {
		if(msg.what == ESearchBarViewUtils.SEARCHBAR_MSG_CODE_OPEN) {
			handleSearchBarOpen(msg);
		}else if(msg.what == ESearchBarViewUtils.SEARCHBAR_MSG_CODE_SETVIEWSTYLE) {
			handleSearchBarSetViewStyle(msg);
		}else {
			handleMessageInSearchBar(msg);
		}
	}

	private void handleSearchBarOpen(Message msg) {
		String[] params = msg.getData().getStringArray(ESearchBarViewUtils.SEARCHBAR_MSG_CODE_FUNCTION);
		if(params == null || params.length == 0) {
			return;
		}
		try {
			JSONObject json = new JSONObject(params[0]);
			float x = Float.parseFloat(json.getString(ESearchBarViewUtils.SEARCHBAR_PARAMS_KEY_X));
			float y = Float.parseFloat(json.getString(ESearchBarViewUtils.SEARCHBAR_PARAMS_KEY_Y));
			float w = Float.parseFloat(json.getString(ESearchBarViewUtils.SEARCHBAR_PARAMS_KEY_W));
			float h = Float.parseFloat(json.getString(ESearchBarViewUtils.SEARCHBAR_PARAMS_KEY_H));

			Intent intent = new Intent(mContext, ESearchBarViewBaseActivity.class);
			intent.putExtra(ESearchBarViewUtils.SEARCHBAR_MSG_CODE_OBJ, this);
			if(model != null) {
				intent.putExtra(ESearchBarViewUtils.SEARCHBAR_MSG_CODE_MODEL, model);
			}
			String activityID = ESearchBarViewUtils.SEARCHBAR_MSG_CODE_ACTIVITY + this.hashCode();
			ESearchBarViewBaseActivity activity = (ESearchBarViewBaseActivity) mgr.getActivity(activityID);
			if(activity != null) {
				return;
			}
			Window window = mgr.startActivity(activityID, intent);
			View decorView = window.getDecorView();
			LayoutParams param = new LayoutParams((int) w, (int) h);
			param.topMargin = (int) x;
			param.leftMargin = (int) y;
			addView2CurrentWindow(decorView, param);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void handleSearchBarSetViewStyle(Message msg) {
		String[] params = msg.getData().getStringArray(ESearchBarViewUtils.SEARCHBAR_MSG_CODE_FUNCTION);
		if(params == null || params.length == 0) {
			return;
		}
		model = ESearchBarViewUtils.parseJson2Model(params);
	}
	
	private void addView2CurrentWindow(View child, RelativeLayout.LayoutParams parms) {
		int l = (int) (parms.leftMargin);
		int t = (int) (parms.topMargin);
		int w = parms.width;
		int h = parms.height;
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);
		lp.gravity = Gravity.NO_GRAVITY;
		lp.leftMargin = l;
		lp.topMargin = t;
		adptLayoutParams(parms, lp);
		mBrwView.addViewToCurrentWindow(child, lp);
	}

	private void handleMessageInSearchBar(Message msg) {
		String activityID = ESearchBarViewUtils.SEARCHBAR_MSG_CODE_ACTIVITY + this.hashCode();
		Activity activity = mgr.getActivity(activityID);
		if(activity != null && activity instanceof ESearchBarViewBaseActivity) {
			String[] params = msg.getData().getStringArray(ESearchBarViewUtils.SEARCHBAR_MSG_CODE_FUNCTION);
			ESearchBarViewBaseActivity eSearchBarViewBaseActivity = (ESearchBarViewBaseActivity) activity;
			switch (msg.what) {
			case ESearchBarViewUtils.SEARCHBAR_MSG_CODE_CLOSE:
				handleSearchBarClose(params, eSearchBarViewBaseActivity);
				break;
			case ESearchBarViewUtils.SEARCHBAR_MSG_CODE_CLEARHISTORY:
				handleSearchBarClearHistory(params, eSearchBarViewBaseActivity);
				break;
			}
		}
	}

	private void handleSearchBarClearHistory(String[] params,
			ESearchBarViewBaseActivity eSearchBarViewBaseActivity) {
		eSearchBarViewBaseActivity.clearHistory();
	}

	private void handleSearchBarClose(String[] params,
			ESearchBarViewBaseActivity eSearchBarViewBaseActivity) {
		View decorView = eSearchBarViewBaseActivity.getWindow().getDecorView();
		removeViewFromCurrentWindow(decorView);
		String activityID = ESearchBarViewUtils.SEARCHBAR_MSG_CODE_ACTIVITY + this.hashCode();
		mgr.destroyActivity(activityID, true);
	}
	
	public void callback(String index, String keyword) {
		String js = SCRIPT_HEADER + "if(" + F_CALLBACK_NAME_ONITEMCLICK + "){" +F_CALLBACK_NAME_ONITEMCLICK + "('" + index + "','" + keyword + "')}";
		onCallback(js);
	}

}