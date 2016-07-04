package org.zywx.wbpalmstar.plugin.uexSearchBarView;

import java.io.Serializable;

public class ESearchBarViewDataModel implements Serializable {

    public SearchBarModel searchBar;
    public ListViewModel listView;

    public ESearchBarViewDataModel() {
        super();
    }

    public ESearchBarViewDataModel(SearchBarModel searchBar,
                                   ListViewModel listView) {
        super();
        this.searchBar = searchBar;
        this.listView = listView;
    }

    public SearchBarModel getSearchBar() {
        return searchBar;
    }

    public void setSearchBar(SearchBarModel searchBar) {
        this.searchBar = searchBar;
    }

    public ListViewModel getListView() {
        return listView;
    }

    public void setListView(ListViewModel listView) {
        this.listView = listView;
    }

    public class SearchBarModel {
        public String placehoderText;
        public String textColor;
        public String inputBgColor;
        public String cancelButtonTextColor;

        public String getPlacehoderText() {
            return placehoderText;
        }

        public void setPlacehoderText(String placehoderText) {
            this.placehoderText = placehoderText;
        }

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }

        public String getInputBgColor() {
            return inputBgColor;
        }

        public void setInputBgColor(String inputBgColor) {
            this.inputBgColor = inputBgColor;
        }

        public String getCancelButtonTextColor() {
            return cancelButtonTextColor;
        }

        public void setCancelButtonTextColor(String cancelButtonTextColor) {
            this.cancelButtonTextColor = cancelButtonTextColor;
        }


    }

    public class ListViewModel {
        public String bgColor;
        public String separatorLineColor;
        public String itemTextColor;
        public String clearHistoryButtonTextCoror;

        public String getBgColor() {
            return bgColor;
        }

        public void setBgColor(String bgColor) {
            this.bgColor = bgColor;
        }

        public String getSeparatorLineColor() {
            return separatorLineColor;
        }

        public void setSeparatorLineColor(String separatorLineColor) {
            this.separatorLineColor = separatorLineColor;
        }

        public String getItemTextColor() {
            return itemTextColor;
        }

        public void setItemTextColor(String itemTextColor) {
            this.itemTextColor = itemTextColor;
        }

        public String getClearHistoryButtonTextCoror() {
            return clearHistoryButtonTextCoror;
        }

        public void setClearHistoryButtonTextCoror(String clearHistoryButtonTextCoror) {
            this.clearHistoryButtonTextCoror = clearHistoryButtonTextCoror;
        }


    }
}
