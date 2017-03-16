package cs3714.hw1.data;

/**
 * Created by kevincianfarini on 3/15/17.
 */

public class Team {

    private String teamname;
    private int rank_today, rank_total;
    private long steps, steps_total, step_goal;

    public Team(String teamname, int rank_today, long steps, long steps_total, int rank_total, long step_goal) {
        this.teamname = teamname;
        this.rank_today = rank_today;
        this.steps = steps;
        this.steps_total = steps_total;
        this.step_goal = step_goal;
    }


    public String getTeamname() {
        return teamname;
    }

    public int getRank_today() {
        return rank_today;
    }

    public int getRank_total() {
        return rank_total;
    }

    public long getSteps() {
        return steps;
    }

    public long getSteps_total() {
        return steps_total;
    }

    public long getStep_goal() {
        return step_goal;
    }
}
