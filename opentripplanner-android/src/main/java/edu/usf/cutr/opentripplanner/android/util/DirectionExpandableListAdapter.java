/*
 * Copyright 2012 University of South Florida
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package edu.usf.cutr.opentripplanner.android.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.usf.cutr.opentripplanner.android.R;
import edu.usf.cutr.opentripplanner.android.model.Direction;

/**
 * @author Khoa Tran
 */

public class DirectionExpandableListAdapter extends BaseExpandableListAdapter {

    Context context;

    int directionLayoutResourceId;

    int subDirectionLayoutResourceId;

    Direction data[] = null;

    public DirectionExpandableListAdapter(Context context, int directionLayoutResourceId,
            int subDirectionLayoutResourceId, Direction[] data) {
        this.directionLayoutResourceId = directionLayoutResourceId;
        this.subDirectionLayoutResourceId = subDirectionLayoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Direction> subDirections = data[groupPosition].getSubDirections();

        if (subDirections != null && !subDirections.isEmpty()) {
            return subDirections.get(childPosition);
        }

        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Direction> subDirections = data[groupPosition].getSubDirections();

        if (subDirections != null) {
            return subDirections.size();
        }

        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {

        View row = convertView;
        DirectionHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(subDirectionLayoutResourceId, parent, false);

            holder = new DirectionHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
            holder.txtDirection = (TextView) row.findViewById(R.id.directionText);

            row.setTag(holder);
        } else {
            holder = (DirectionHolder) row.getTag();
        }

        Direction dir = data[groupPosition];
        Direction subDirection = (Direction) getChild(groupPosition, childPosition);
        CharSequence text = subDirection == null ? "null here" : subDirection.getDirectionText();
        holder.txtDirection.setText(text);
        holder.imgIcon.setImageResource(subDirection.getIcon());

        return row;

//        return textView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return data.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {

        View row = convertView;
        DirectionHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(directionLayoutResourceId, parent, false);

            holder = new DirectionHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
            holder.noIconText = (TextView) row.findViewById(R.id.noIconText);
            holder.txtDirection = (TextView) row.findViewById(R.id.directionText);

            row.setTag(holder);
        } else {
            holder = (DirectionHolder) row.getTag();
        }

        Direction dir = data[groupPosition];

        if (!dir.isTransit()){
            holder.txtDirection.setText(dir.getDirectionIndex() + ". " + dir.getDirectionText());
            holder.imgIcon.setVisibility(View.VISIBLE);
            holder.imgIcon.setImageResource(dir.getIcon());
        }
        else {
            CharSequence textBeforeTime = dir.getDirectionIndex() + ". " +  dir.getService();
            CharSequence text;
            CharSequence time = dir.getOldTime();
            text = new SpannableString(textBeforeTime);
            if (dir.isRealTimeInfo()){
                if (dir.getNewTime() != null){
                    time = dir.getNewTime();
                }
            }
            text = TextUtils.concat(text, " ", time, "\n", dir.getPlaceAndHeadsign());

            if (!TextUtils.isEmpty(dir.getAgency())){
                text = TextUtils.concat(text, "\n", dir.getAgency());
            }
            if (!TextUtils.isEmpty(dir.getExtra())){
                SpannableString extraSpannableString = new SpannableString(dir.getExtra());
                extraSpannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0,
                        extraSpannableString.length(), 0);
                text = TextUtils.concat(text, "\n", extraSpannableString);
            }

            holder.txtDirection.setText(text);
            if (dir.getIcon() == -1){
                holder.imgIcon.setVisibility(View.INVISIBLE);
                holder.noIconText.setVisibility(View.VISIBLE);
            }
            else{
                holder.imgIcon.setVisibility(View.VISIBLE);
                holder.imgIcon.setImageResource(dir.getIcon());
                holder.noIconText.setVisibility(View.INVISIBLE);
            }
        }

        return row;

//        return textView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    static class DirectionHolder {

        ImageView imgIcon;

        TextView noIconText;

        TextView txtDirection;
    }
}