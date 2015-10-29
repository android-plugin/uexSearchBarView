package org.zywx.wbpalmstar.plugin.uexSearchBarView;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import org.json.JSONObject;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;

public class EUExSearchBarView extends EUExBase{
	
	private String SCRIPT_HEADER = "javascript:";
	private String F_CALLBACK_NAME_ONITEMCLICK = "uexSearchBarView.onItemClick";
	private String F_CALLBACK_ON_SEARCH = "uexSearchBarView.onSearch";
	private ESearchBarViewDataModel model;
    private ESearchBarViewBaseView mSearchView;


	public EUExSearchBarView(Context context, EBrowserView eBrowserView) {
		super(context, eBrowserView);
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

            if (mSearchView != null) return;
            model = ESearchBarViewUtils.parseJson2Model(params);
            mSearchView = new ESearchBarViewBaseView(mContext, this, model);

			LayoutParams param = new LayoutParams((int) w, (int) h);
			param.topMargin = (int) y;
			param.leftMargin = (int) x;
			addView2CurrentWindow(mSearchView, param);
		} catch (Exception e) {
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
        switch (msg.what) {
            case ESearchBarViewUtils.SEARCHBAR_MSG_CODE_CLOSE:
                handleSearchBarClose();
                break;
            case ESearchBarViewUtils.SEARCHBAR_MSG_CODE_CLEARHISTORY:
                handleSearchBarClearHistory();
                break;
        }
	}

	private void handleSearchBarClearHistory() {
        if (mSearchView == null) return;
        mSearchView.clearHistory();
	}

	private void handleSearchBarClose() {
        if (mSearchView == null) return;
        mSearchView.clean();
		removeViewFromCurrentWindow(mSearchView);
        mSearchView = null;
	}

	/**
	 * 点击历史记录中的某一个item时，触发这个回调。
	 * @param index  该item在列表中的位置
	 * @param keyword 搜索关键字
	 */
	public void onItemClick(String index, String keyword) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ESearchBarViewUtils.RESULT_INDEX, index);
            jsonObject.put(ESearchBarViewUtils.RESULT_KEYWORD, keyword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String js = SCRIPT_HEADER + "if(" + F_CALLBACK_NAME_ONITEMCLICK +
                "){" +F_CALLBACK_NAME_ONITEMCLICK + "('" + jsonObject.toString() + "')}";
		onCallback(js);
	}

	/**
	 * 点击"搜索"按钮，或软键盘上的搜索图标时会触发这个回调函数，返回搜索的关键字。格式如下： {"keyword": "query"}
	 * @param keyword
	 */
	public void onActionSearch(String keyword) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(ESearchBarViewUtils.RESULT_KEYWORD, keyword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String js = SCRIPT_HEADER + "if(" + F_CALLBACK_ON_SEARCH +
				"){" +F_CALLBACK_ON_SEARCH + "('" + jsonObject.toString() + "')}";
		onCallback(js);
	}
}
