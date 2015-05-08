package org.zywx.wbpalmstar.plugin.uexSearchBarView;

import java.util.List;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter {

	private Context context;
	private List<String> kws;
	private EUExSearchBarView eSearchBarView;
	private String keyword;
	private String itemColor;
	
	public HistoryAdapter(Context context, List<String> kws, EUExSearchBarView eSearchBarView) {
		this.context = context;
		this.kws = kws;
		this.eSearchBarView = eSearchBarView;
	}
	
	public void setItemColor(String itemColor) {
		this.itemColor = itemColor;
	}

	public void refreshData(List<String> kws) {
		this.kws = kws;
		notifyDataSetInvalidated();
	}
	
	@Override
	public int getCount() {
		return kws.size();
	}

	@Override
	public Object getItem(int position) {
		return kws.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if(convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, EUExUtil.getResLayoutID("plugin_uexsearchbarview_lv_item"), null);
			holder.tv = (TextView) convertView.findViewById(EUExUtil.getResIdID("plugin_uexsearchbarview_lv_item_tv"));
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		keyword = kws.get(position);
		holder.tv.setText(keyword);
		if(itemColor != null) {
			holder.tv.setTextColor(Color.parseColor(itemColor));
		}
		convertView.setBackgroundResource(EUExUtil.getResDrawableID("plugin_uexsearchbarview_lv_item_selector"));
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				keyword = kws.get(position);
				eSearchBarView.callback(position+"", keyword);
			}
		});
		return convertView;
	}
	
	public class Holder {
		public TextView tv;
	}

}
