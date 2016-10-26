package org.zywx.wbpalmstar.plugin.uexSearchBarView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fred on 16/10/24.
 */
public class ESearchBarViewWithSuggestion extends FrameLayout implements View.OnClickListener {
    private View view;
    private ImageView bt;
    private ListView lv;
    private ImageView separator;
    private EUExSearchBarView eSearchBarView;
    //用于显示的数据
    private List<String> dataList;
    private HistoryAdapter adapter;
    private EditText et;
    private ImageView del;
    private ESearchBarViewDataModel model;
    private LayoutInflater inflater;
    private LinearLayout searchBar;
    private Context mContext;
    private JSONArray suggestionList;

    public ESearchBarViewWithSuggestion(Context context, EUExSearchBarView base,
                                  ESearchBarViewDataModel data) {
        super(context);
        this.mContext = context;
        this.eSearchBarView = base;
        this.model = data;
        suggestionList = this.model.suggestionList;
        dataList = new ArrayList<String>();
        init();
    }

    private void init() {
        inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(EUExUtil.getResLayoutID("plugin_uexsearchbarview_suggestion"),
                this, true);
        initView(view);

        adapter = new HistoryAdapter(mContext, dataList, eSearchBarView);
        if(model != null && model.getListView() != null && model.getListView().getItemTextColor() != null) {
            adapter.setItemColor(model.getListView().getItemTextColor());
        }
        lv.setAdapter(adapter);
    }
    private void initView(View view) {
        searchBar = (LinearLayout) findViewById(EUExUtil.getResIdID("plugin_uexsearchbarview_rl_searchbar"));
        et = (EditText) findViewById(EUExUtil.getResIdID("plugin_uexsearchbarview_rl_searchbar_et"));
        del = (ImageView) findViewById(EUExUtil.getResIdID("plugin_uexsearchbarview_rl_searchbar_iv"));
        bt = (ImageView) findViewById(EUExUtil.getResIdID("plugin_uexsearchbarview_rl_bt_search"));
        separator = (ImageView) findViewById(EUExUtil.getResIdID("plugin_uexsearchbarview_iv_separator"));
        lv = (ListView) findViewById(EUExUtil.getResIdID("plugin_uexsearchbarview_ll_lv"));


        if(model != null) {
            if(model.getSearchBar() != null) {
                if(model.getSearchBar().getPlacehoderText() != null) {
                    et.setHint(model.getSearchBar().getPlacehoderText());
                }
                if(model.getSearchBar().getTextColor() != null) {
                    et.setTextColor(Color.parseColor(model.getSearchBar().getTextColor()));
                }
                if(model.getSearchBar().getInputBgColor() != null) {
                    searchBar.setBackgroundColor(Color.parseColor(model.getSearchBar().inputBgColor));
                }
            }
            if(model.getListView() != null) {
                if(model.getListView().getBgColor() != null) {
                    lv.setBackgroundColor(Color.parseColor(model.getListView().getBgColor()));
                }
                if(model.getListView().getSeparatorLineColor() != null) {
                    lv.setDivider(new ColorDrawable(Color.parseColor(model.getListView().getSeparatorLineColor())));
                    lv.setDividerHeight(1);
                }
            }
        }

        et.addTextChangedListener(textWatcher);
        del.setOnClickListener(this);
        bt.setOnClickListener(this);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s != null && !"".equalsIgnoreCase(et.getText().toString().trim())) {
                del.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s == null || "".equalsIgnoreCase(s.toString().trim())) {
                del.setVisibility(View.GONE);
            }
            dataList.clear();
            if (!TextUtils.isEmpty(s)) {
                String text = s.toString();
                int length = suggestionList.length();
                for (int i = 0; i < suggestionList.length(); i ++) {
                    String str = null;
                    try {
                        str = suggestionList.getString(i);
                        if (str.startsWith(text)) {
                            dataList.add(str);
                        }
                        if (dataList.size() >= model.suggestionCount)  break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            adapter.refreshData(dataList);
        }
    };


    @Override
    public void onClick(View v) {
        if(v.getId() == EUExUtil.getResIdID("plugin_uexsearchbarview_rl_bt_search")) {
            String keyword = et.getText().toString().trim();
            if(keyword != null && !"".equalsIgnoreCase(keyword)) {
                et.setText("");
                et.setHint("请输入搜索词");
                et.setHintTextColor(Color.GRAY);
                eSearchBarView.onActionSearch(keyword);
                InputMethodManager im = (InputMethodManager) mContext.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }
        if(v.getId() == EUExUtil.getResIdID("plugin_uexsearchbarview_rl_searchbar_iv")) {
            et.setText("");
            et.setHint("请输入搜索词");
            InputMethodManager im = (InputMethodManager) mContext.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(v.getWindowToken(), 0);
            et.setHintTextColor(Color.GRAY);
            del.setVisibility(View.GONE);
        }
    }

    public void onItemClick(String keyword) {
        et.setText(keyword);
        et.setSelection(keyword.length());
        InputMethodManager im = (InputMethodManager) mContext.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
