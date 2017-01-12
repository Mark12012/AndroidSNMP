package com.example.marek.zstv3;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listFolders; // lista z folderami
    private HashMap<String, List<String>> listChild; //zawartosc folderow

    public ListAdapter(Context context, List<String> listFolders,
                       HashMap<String, List<String>> listChild) {
        this.context = context;
        this.listFolders = listFolders;
        this.listChild = listChild;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listChild.get(this.listFolders.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String sf = sharedPrefs.getString("pref_key_child_font_list", "24");
        Float f = Float.parseFloat(sf);
        txtListChild.setTextSize(f);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listChild.get(this.listFolders.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listFolders.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listFolders.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }


        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String sf = sharedPrefs.getString("pref_key_font_list", "24");
        Float f = Float.parseFloat(sf);
        lblListHeader.setTextSize(f);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
