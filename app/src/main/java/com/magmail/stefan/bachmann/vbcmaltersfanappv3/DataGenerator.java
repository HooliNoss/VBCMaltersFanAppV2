package com.magmail.stefan.bachmann.vbcmaltersfanappv3;

import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefan.bachmann on 16.11.2015.
 */
public class DataGenerator {

    private static List<Team> teamList = new ArrayList<Team>();

    public static Team[] getAllTeams() {
        if (teamList.size() == 0)
            generateTeams();

        return teamList.toArray(new Team[teamList.size()]);
    }

    public static Team getTeamByName(String name) {
        if (name == null)
            return null;

        Team ret = new Team();

        for(Team team : teamList){
            if (team.getM_Name().equals(name)){
                ret = team;
                break;
            }
        }
        return ret;
    }

    public static Team getTeamById(int id) {
        if (id == 0)
            return null;

        Team ret = new Team();

        for(Team team : teamList){
            if (team.getM_TeamId() == id)
            {
                ret = team;
                break;
            }
        }
        return ret;
    }

    public static void generateTeams()
    {
        Team damen1 = new Team();
        damen1.setM_Name("Damen 1");
        damen1.setM_League("3. Liga");
        damen1.setM_ImgSrc("damen1");
        damen1.setM_TeamId(24895);
        damen1.setM_GroupId(9069);
        teamList.add(damen1);

        Team damen2 = new Team();
        damen2.setM_Name("Damen 2");
        damen2.setM_League("5. Liga");
        damen2.setM_ImgSrc("damen2");
        damen2.setM_TeamId(25301);
        damen2.setM_GroupId(9075);
        teamList.add(damen2);

        Team herren1 = new Team();
        herren1.setM_Name("Herren 1");
        herren1.setM_League("1. Liga");
        herren1.setM_ImgSrc("herren1");
        herren1.setM_TeamId(25771);
        herren1.setM_GroupId(9120);
        teamList.add(herren1);

        Team herren2 = new Team();
        herren2.setM_Name("Herren 2");
        herren2.setM_League("2. Liga");
        herren2.setM_ImgSrc("herren2");
        herren2.setM_TeamId(25447);
        herren2.setM_GroupId(9084);
        teamList.add(herren2);

        Team herren3 = new Team();
        herren3.setM_Name("Herren 3");
        herren3.setM_League("4. Liga");
        herren3.setM_ImgSrc("herren3");
        herren3.setM_TeamId(25452);
        herren3.setM_GroupId(9087);
        teamList.add(herren3);

        Team juniorinnen1 = new Team();
        juniorinnen1.setM_Name("Juniorinnen 1");
        juniorinnen1.setM_League("2. Liga");
        juniorinnen1.setM_ImgSrc("juniorinnen1");
        juniorinnen1.setM_TeamId(25370);
        juniorinnen1.setM_GroupId(9078);
        teamList.add(juniorinnen1);

        Team juniorinnen2 = new Team();
        juniorinnen2.setM_Name("Juniorinnen 2");
        juniorinnen2.setM_League("3. Liga");
        juniorinnen2.setM_ImgSrc("juniorinnen2");
        juniorinnen2.setM_TeamId(25362);
        juniorinnen2.setM_GroupId(9082);
        teamList.add(juniorinnen2);

        Team juniorinnen3 = new Team();
        juniorinnen3.setM_Name("Juniorinnen 3");
        juniorinnen3.setM_League("4. Liga");
        juniorinnen3.setM_ImgSrc("juniorinnen3");
        juniorinnen3.setM_TeamId(25600);
        juniorinnen3.setM_GroupId(9083);
        teamList.add(juniorinnen3);

        Team junioren1 = new Team();
        junioren1.setM_Name("Junioren 1");
        junioren1.setM_League("1. Liga");
        junioren1.setM_ImgSrc("junioren1");
        junioren1.setM_TeamId(25986);
        junioren1.setM_GroupId(9185);
        teamList.add(junioren1);
    }
}
