package org.zywx.wbpalmstar.plugin.uexSearchBarView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

import java.util.ArrayList;
import java.util.List;

public class ESearchBarViewBaseView extends FrameLayout
        implements OnClickListener, TextWatcher {

    private View view;
    private ImageView bt;
    private ListView lv;
    private ImageView separator;
    private ImageView historyBg;
    private TextView tv;
    private EUExSearchBarView eSearchBarView;
    private List<String> kws;
    private HistoryAdapter adapter;
    private EditText et;
    private ImageView del;
    private ESearchBarViewDataModel model;
    private LayoutInflater inflater;
    private LinearLayout searchBar;
    private Context mContext;

    public ESearchBarViewBaseView(Context context, EUExSearchBarView base,
                                  ESearchBarViewDataModel data) {
        super(context);
        this.mContext = context;
        this.eSearchBarView = base;
        this.model = data;
        init();
    }

    private void init() {
        inflater = LayoutInflater.from(mContext);
        inflater.inflate(EUExUtil.getResLayoutID("plugin_uexsearchbarview_main"),
                this, true);
        initView(view);
        initData();
        changeView();
        adapter = new HistoryAdapter(mContext, kws, eSearchBarView);
        if (model != null && model.getListView() != null && model.getListView().getItemTextColor() != null) {
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
        historyBg = (ImageView) findViewById(EUExUtil.getResIdID("plugin_uexsearchbarview_ll_iv_bg"));
        tv = (TextView) findViewById(EUExUtil.getResIdID("plugin_uexsearchbarview_ll_tv"));

        View header = inflater.inflate(EUExUtil.getResLayoutID("plugin_uexsearchbarview_lv_header"), null);
        View footer = inflater.inflate(EUExUtil.getResLayoutID("plugin_uexsearchbarview_lv_footer"), null);
        footer.findViewById(EUExUtil.getResIdID("plugin_uexsearchbarview_lv_footer_iv")).setOnClickListener(this);

        lv.addHeaderView(header, null, false);
        lv.addFooterView(footer);

        if (model != null) {
            if (model.getSearchBar() != null) {
                if (model.getSearchBar().getPlacehoderText() != null) {
                    et.setHint(model.getSearchBar().getPlacehoderText());
                }
                if (model.getSearchBar().getTextColor() != null) {
                    et.setTextColor(Color.parseColor(model.getSearchBar().getTextColor()));
                }
                if (model.getSearchBar().getInputBgColor() != null) {
                    searchBar.setBackgroundColor(Color.parseColor(model.getSearchBar().inputBgColor));
                }
            }
            if (model.getListView() != null) {
                if (model.getListView().getBgColor() != null) {
                    lv.setBackgroundColor(Color.parseColor(model.getListView().getBgColor()));
                }
                if (model.getListView().getSeparatorLineColor() != null) {
                    lv.setDivider(new ColorDrawable(Color.parseColor(model.getListView().getSeparatorLineColor())));
                    lv.setDividerHeight(1);
                }
            }
        }

        et.addTextChangedListener(this);
        et.setOnEditorActionListener(editorActionListener);
        del.setOnClickListener(this);
        bt.setOnClickListener(this);
    }

    private void initData() {
        if (kws == null)
            kws = new ArrayList<String>();
        SharedPreferences sp = mContext.getSharedPreferences(
                ESearchBarViewUtils.SEARCHBAR_MSG_CODE_STORAGE, Activity.MODE_PRIVATE);
        for (int i = 0; i < sp.getAll().size(); i++) {
            kws.add(sp.getString("p" + i, ""));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == EUExUtil.getResIdID("plugin_uexsearchbarview_rl_bt_search")) {
            String keyword = et.getText().toString().trim();
            if (keyword != null && !"".equalsIgnoreCase(keyword)) {
                et.setText("");
                et.setHint("请输入搜索词");
                et.setHintTextColor(Color.GRAY);
                eSearchBarView.onActionSearch(keyword);
                InputMethodManager im = (InputMethodManager) mContext.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if (kws.contains(keyword)) {
                    return;
                }
                cacheKeywords(keyword);
            }
        }
        if (v.getId() == EUExUtil.getResIdID("plugin_uexsearchbarview_rl_searchbar_iv")) {
            et.setText("");
            et.setHint("请输入搜索词");
            InputMethodManager im = (InputMethodManager) mContext.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(v.getWindowToken(), 0);
            et.setHintTextColor(Color.GRAY);
            del.setVisibility(View.GONE);
        }
        if (v.getId() == EUExUtil.getResIdID("plugin_uexsearchbarview_lv_footer_iv")) {
            clearHistory();
        }
    }

    private boolean changeView() {
        if (kws.size() > 0) {
            separator.setVisibility(View.GONE);
            historyBg.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            return true;
        } else {
            separator.setVisibility(View.VISIBLE);
            historyBg.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            return false;
        }
    }

    public void clearHistory() {
        if (kws != null) {
            kws.clear();
            adapter.refreshData(kws);
            SharedPreferences sp = mContext.getSharedPreferences(
                    ESearchBarViewUtils.SEARCHBAR_MSG_CODE_STORAGE, Activity.MODE_PRIVATE);
            sp.edit().clear().commit();
            changeView();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null && !"".equalsIgnoreCase(et.getText().toString().trim())) {
            del.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == null || "".equalsIgnoreCase(s.toString().trim())) {
            del.setVisibility(View.GONE);
        }
    }

    public void clean() {
        hideSoftInput();
    }

    private void hideSoftInput() {
        InputMethodManager im = (InputMethodManager) mContext.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (im.isActive()) {
            if (view != null) {
                im.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String keyword = et.getText().toString();
                eSearchBarView.onActionSearch(et.getText().toString());
                if (kws.contains(keyword)) {
                    return false;
                }
                cacheKeywords(keyword);
            }
            return false;
        }
    };

    private void cacheKeywords(String keyword) {
        kws.add(keyword);
        SharedPreferences sp = mContext.getSharedPreferences(
                ESearchBarViewUtils.SEARCHBAR_MSG_CODE_STORAGE, Activity.MODE_PRIVATE);
        Editor edit = sp.edit();
        for (int i = 0; i < kws.size(); i++) {
            edit.putString("p" + i, kws.get(i));
        }
        edit.commit();
        changeView();
        del.setVisibility(View.GONE);
        adapter.refreshData(kws);
    }
}
