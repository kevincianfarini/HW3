package cs3714.hw1.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cs3714.hw1.R;
import cs3714.hw1.data.Team;
import cs3714.hw1.data.TeamMember;

/**
 * Created by Andrey on 2/16/2017.
 */
//HW3
public class TeamRankFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String TAG_TEAM_RANK_FRAGMENT = "dashboard_fragment";
    private List<TeamMember> members;
    private TextView myGoal, mySteps, allSteps, allGoal;
    private Button next, today, previous, all;
    ProgressBar teamInfoBar;
    private ArrayList<Team> teams;
    private ListView teamsList;
    private TeamArrayAdapter teamListAdapter;

    public TeamRankFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public static TeamRankFragment newInstance(String param1, String param2) {
        return new TeamRankFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team_rank, container, false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        teams = new ArrayList<>();
        teams.add(new Team("Team 1",1,10000,170000,1,50000));
        teams.add(new Team("Team 2",4,5000,160000,2,50000));
        teams.add(new Team("Team 3",3,6000,150000,3,50000));
        teams.add(new Team("Team 4",2,8000,140000,4,50000));
        teams.add(new Team("Team 5",7,2000,130000,5,50000));
        teams.add(new Team("Team 6",5,4500,120000,6,50000));
        teams.add(new Team("Team 7",6,2300,110000,7,50000));
        teamsList = (ListView) view.findViewById(R.id.teams);
        teamsList.setOnItemClickListener(this);
        teamListAdapter = new TeamArrayAdapter(getActivity(), R.layout.team_list_view_item, teams);
        teamsList.setAdapter(teamListAdapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO track user interaction
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Rankings");
        alertDialog.setMessage("Rank today: " + teams.get(position).getRank_today() + "\n"+ "Overall rank: " + teams.get(position).getRank_total());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private class TeamArrayAdapter extends ArrayAdapter<Team> {
        private final Context context;
        private final ArrayList<Team> teams;
        private int id;

        public TeamArrayAdapter(Context context, int id, ArrayList<Team> teams) {
            super(context, id, teams);
            this.context = context;
            this.teams = teams;
            this.id = id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.team_list_view_item, parent, false);
            TextView teamName = (TextView) rowView.findViewById(R.id.teamName);
            TextView todayPercentage = (TextView) rowView.findViewById(R.id.todayPercentage);
            TextView overallPercentage = (TextView) rowView.findViewById(R.id.overallPercentage);
            ProgressBar todayBar = (ProgressBar)rowView.findViewById(R.id.todayBar);
            ProgressBar overallBar = (ProgressBar)rowView.findViewById(R.id.overallBar);
            teamName.setText(teams.get(position).getTeamname());
            todayPercentage.setText("Today:"+ Math.round(((float) teams.get(position).getSteps() /
                    (float)teams.get(position).getStep_goal() * 100)) + "% of goal at " + teams.get(position).getSteps() + " steps");
            overallPercentage.setText("Overall:" + Math.round(teams.get(position).getSteps_total() /
                    teams.get(position).getStep_goal()*100/8/7) + "% of goal at " + teams.get(position).getSteps_total() + " steps");
            todayBar.setProgress(Math.round(((float)teams.get(position).getSteps()/(float)teams.get(position).getStep_goal()*100)));
            overallBar.setProgress(Math.round((teams.get(position).getSteps_total()/teams.get(position).getStep_goal()*100/8/7)));
            if (position % 2 == 1) {
                rowView.setBackgroundColor(getResources().getColor(R.color.table_data_row_odd));
            } else {
                rowView.setBackgroundColor(getResources().getColor(R.color.table_data_row_even));
            }
            return rowView;

        }
    }

}
