package com.example.auditor;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;

/**
 * Created by Wan Lin on 15/8/29.
 * To adapt score list view
 */
public class ScoreAdapter extends BaseAdapter {
    private ArrayList<Score> scores;
    private LayoutInflater scoreInflater;
    private SlidingTabActivity slidingTabActivity;
    private ScoreFileListPage scoreFileListPage;

    public ScoreAdapter(SlidingTabActivity slidingTabActivity, ArrayList<Score> scores, ScoreFileListPage fragment) {
        this.scores = scores;
        this.slidingTabActivity = slidingTabActivity;
        scoreInflater = LayoutInflater.from(slidingTabActivity);
        scoreFileListPage = fragment;
    }

    @Override
    public int getCount() {
        return scores.size();
    }

    @Override
    public Object getItem(int position) {
        return scores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return scores.get(position).getID();
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        // map to song layout
        final RelativeLayout songLayout = (RelativeLayout)scoreInflater.inflate(R.layout.file_item_view, parent, false);
        final TextView scoreTitle = (TextView)songLayout.findViewById(R.id.file_title);
        final ImageButton collapse = (ImageButton)songLayout.findViewById(R.id.collapse);
        final Score score = scores.get(position);
        final TextView scoreModDate = (TextView)songLayout.findViewById(R.id.file_mod_date);

        scoreTitle.setText(score.getTitle().substring(0, score.getTitle().length() - 4));
        DateFormat sdf = DateFormat.getDateTimeInstance();
        scoreModDate.setText(sdf.format(score.getLastModDate()));
        songLayout.setTag(position);

        collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(scoreInflater.getContext(), collapse);
                popupMenu.getMenuInflater().inflate(R.menu.score_file_popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().toString().equals(scoreFileListPage.getString(R.string.rename)))
                            scoreFileListPage.renameScore(score);
                        else if(item.getTitle().toString().equals(slidingTabActivity.getString(R.string.delete)))
                            scoreFileListPage.deleteScore(score);
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        return songLayout;
    }
}
