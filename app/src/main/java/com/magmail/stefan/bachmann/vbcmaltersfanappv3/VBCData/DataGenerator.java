package com.magmail.stefan.bachmann.vbcmaltersfanappv3.VBCData;

import com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs.Club;
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

    public static Club getClub()
    {
        Club myClub = new Club();
        myClub.setM_ClubName("VBC Malters");
        myClub.setM_ClubId(911360);

        return myClub;
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

    public static String getImageStringByNewsTag(String newsTag)
    {
        if (newsTag == "Allgemein")
            return "placeholder";

        for(Team team : teamList){
            if (team.getM_Name().equals(newsTag))
                return team.getM_ImgSrc();
        }

        return "placeholder";
    }

    public static String[] getAllNewsTags()
    {
        String[] newsTags = {"Damen 1", "Damen 2","Damen 3", "Herren 1", "Herren 2", "Herren 3",
                             "Juniorinnen 1", "Juniorinnen 2", "Juniorinnen 3",
                             "Junioren 1", "Allgemein"};

        return newsTags;
    }

    private static void generateTeams()
    {
        Team damen1 = new Team();
        damen1.setM_Name("Damen 1");
        damen1.setM_League("2. Liga");
        damen1.setM_ImgSrc("damen1");
        damen1.setM_TeamId(27322);
        damen1.setM_GroupId(9673);
        teamList.add(damen1);

        Team damen2 = new Team();
        damen2.setM_Name("Damen 2");
        damen2.setM_League("5. Liga");
        damen2.setM_ImgSrc("damen2");
        damen2.setM_TeamId(31125);
        damen2.setM_GroupId(9682);
        teamList.add(damen2);

        Team damen3 = new Team();
        damen3.setM_Name("Damen 3");
        damen3.setM_League("5. Liga");
        damen3.setM_ImgSrc("damen3");
        damen3.setM_TeamId(28459);
        damen3.setM_GroupId(9681);
        teamList.add(damen3);

        Team herren1 = new Team();
        herren1.setM_Name("Herren 1");
        herren1.setM_League("NLB");
        herren1.setM_ImgSrc("herren1");
        herren1.setM_TeamId(29776);
        herren1.setM_GroupId(9820);
        teamList.add(herren1);

        Team herren2 = new Team();
        herren2.setM_Name("Herren 2");
        herren2.setM_League("2. Liga");
        herren2.setM_ImgSrc("herren2");
        herren2.setM_TeamId(28864);
        herren2.setM_GroupId(9684);
        teamList.add(herren2);

        Team herren3 = new Team();
        herren3.setM_Name("Herren 3");
        herren3.setM_League("3. Liga");
        herren3.setM_ImgSrc("herren3");
        herren3.setM_TeamId(28879);
        herren3.setM_GroupId(9686);
        teamList.add(herren3);

        Team juniorinnen1 = new Team();
        juniorinnen1.setM_Name("Juniorinnen 1");
        juniorinnen1.setM_League("2. Liga");
        juniorinnen1.setM_ImgSrc("juniorinnen1");
        juniorinnen1.setM_TeamId(28651);
        juniorinnen1.setM_GroupId(9689);
        teamList.add(juniorinnen1);

        Team juniorinnen2 = new Team();
        juniorinnen2.setM_Name("Juniorinnen 2");
        juniorinnen2.setM_League("4. Liga");
        juniorinnen2.setM_ImgSrc("juniorinnen2");
        juniorinnen2.setM_TeamId(29290);
        juniorinnen2.setM_GroupId(9694);
        teamList.add(juniorinnen2);

        Team juniorinnen3 = new Team();
        juniorinnen3.setM_Name("Juniorinnen 3");
        juniorinnen3.setM_League("4. Liga");
        juniorinnen3.setM_ImgSrc("juniorinnen3");
        juniorinnen3.setM_TeamId(28630);
        juniorinnen3.setM_GroupId(9695);
        teamList.add(juniorinnen3);

        Team junioren1 = new Team();
        junioren1.setM_Name("Junioren 1");
        junioren1.setM_League("1. Liga");
        junioren1.setM_ImgSrc("junioren1");
        junioren1.setM_TeamId(30325);
        junioren1.setM_GroupId(9788);
        teamList.add(junioren1);
    }
}
