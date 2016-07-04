package org.zywx.wbpalmstar.plugin.uexSearchBarView;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.plugin.uexSearchBarView.ESearchBarViewDataModel.ListViewModel;
import org.zywx.wbpalmstar.plugin.uexSearchBarView.ESearchBarViewDataModel.SearchBarModel;

public class ESearchBarViewUtils {

    public static final int SEARCHBAR_MSG_CODE_OPEN = 0;
    public static final int SEARCHBAR_MSG_CODE_CLOSE = 1;
    public static final int SEARCHBAR_MSG_CODE_SETVIEWSTYLE = 2;
    public static final int SEARCHBAR_MSG_CODE_CLEARHISTORY = 3;

    public static final String SEARCHBAR_PARAMS_KEY_X = "x";
    public static final String SEARCHBAR_PARAMS_KEY_Y = "y";
    public static final String SEARCHBAR_PARAMS_KEY_W = "w";
    public static final String SEARCHBAR_PARAMS_KEY_H = "h";

    public static final String SEARCHBAR_PARAMS_KEY_SEARCHBAR = "searchBar";
    public static final String SEARCHBAR_PARAMS_KEY_SEARCHBAR_PLACEHODERTEXT = "placehoderText";
    public static final String SEARCHBAR_PARAMS_KEY_SEARCHBAR_TEXTCOLOR = "textColor";
    public static final String SEARCHBAR_PARAMS_KEY_SEARCHBAR_INPUTBGCOLOR = "inputBgColor";
    public static final String SEARCHBAR_PARAMS_KEY_SEARCHBAR_CANCELBUTTONTEXTCOLOR = "cancelButtonTextColor";
    public static final String SEARCHBAR_PARAMS_KEY_LISTVIEW = "listView";
    public static final String SEARCHBAR_PARAMS_KEY_LISTVIEW_BGCOLOR = "bgColor";
    public static final String SEARCHBAR_PARAMS_KEY_LISTVIEW_SEPARATORLINECOLOR = "separatorLineColor";
    public static final String SEARCHBAR_PARAMS_KEY_LISTVIEW_ITEMTEXTCOLOR = "itemTextColor";
    public static final String SEARCHBAR_PARAMS_KEY_LISTVIEW_CLEARHISTORYBUTTONTEXTCOROR = "clearHistoryButtonTextCoror";

    public static final String SEARCHBAR_MSG_CODE_FUNCTION = "function";
    public static final String SEARCHBAR_MSG_CODE_ACTIVITY = "activityId";
    public static final String SEARCHBAR_MSG_CODE_OBJ = "obj";
    public static final String SEARCHBAR_MSG_CODE_MODEL = "model";
    public static final String SEARCHBAR_MSG_CODE_STORAGE = "storage";
    public static final String RESULT_INDEX = "index";
    public static final String RESULT_KEYWORD = "keyword";


    public static ESearchBarViewDataModel parseJson2Model(String[] params) {
        ESearchBarViewDataModel model = new ESearchBarViewDataModel();
        try {
            JSONObject obj = new JSONObject(params[0]);
            if (obj.has(SEARCHBAR_PARAMS_KEY_SEARCHBAR)) {
                JSONObject searchOBJ = obj.getJSONObject(SEARCHBAR_PARAMS_KEY_SEARCHBAR);
                SearchBarModel searchBar = model.new SearchBarModel();
                if (searchOBJ.has(SEARCHBAR_PARAMS_KEY_SEARCHBAR_PLACEHODERTEXT)) {
                    searchBar.setPlacehoderText(searchOBJ.getString(SEARCHBAR_PARAMS_KEY_SEARCHBAR_PLACEHODERTEXT));
                }
                if (searchOBJ.has(SEARCHBAR_PARAMS_KEY_SEARCHBAR_TEXTCOLOR)) {
                    searchBar.setTextColor(searchOBJ.getString(SEARCHBAR_PARAMS_KEY_SEARCHBAR_TEXTCOLOR));
                }
                if (searchOBJ.has(SEARCHBAR_PARAMS_KEY_SEARCHBAR_INPUTBGCOLOR)) {
                    searchBar.setInputBgColor(searchOBJ.getString(SEARCHBAR_PARAMS_KEY_SEARCHBAR_INPUTBGCOLOR));
                }
                if (searchOBJ.has(SEARCHBAR_PARAMS_KEY_SEARCHBAR_CANCELBUTTONTEXTCOLOR)) {
                    searchBar.setCancelButtonTextColor(searchOBJ.getString(SEARCHBAR_PARAMS_KEY_SEARCHBAR_CANCELBUTTONTEXTCOLOR));
                }
                model.setSearchBar(searchBar);
            }
            if (obj.has(SEARCHBAR_PARAMS_KEY_LISTVIEW)) {
                JSONObject listOBJ = obj.getJSONObject(SEARCHBAR_PARAMS_KEY_LISTVIEW);
                ListViewModel listView = model.new ListViewModel();
                if (listOBJ.has(SEARCHBAR_PARAMS_KEY_LISTVIEW_BGCOLOR)) {
                    listView.setBgColor(listOBJ.getString(SEARCHBAR_PARAMS_KEY_LISTVIEW_BGCOLOR));
                }
                if (listOBJ.has(SEARCHBAR_PARAMS_KEY_LISTVIEW_SEPARATORLINECOLOR)) {
                    listView.setSeparatorLineColor(listOBJ.getString(SEARCHBAR_PARAMS_KEY_LISTVIEW_SEPARATORLINECOLOR));
                }
                if (listOBJ.has(SEARCHBAR_PARAMS_KEY_LISTVIEW_ITEMTEXTCOLOR)) {
                    listView.setItemTextColor(listOBJ.getString(SEARCHBAR_PARAMS_KEY_LISTVIEW_ITEMTEXTCOLOR));
                }
                if (listOBJ.has(SEARCHBAR_PARAMS_KEY_LISTVIEW_CLEARHISTORYBUTTONTEXTCOROR)) {
                    listView.setClearHistoryButtonTextCoror(listOBJ.getString(SEARCHBAR_PARAMS_KEY_LISTVIEW_CLEARHISTORYBUTTONTEXTCOROR));
                }
                model.setListView(listView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

}
