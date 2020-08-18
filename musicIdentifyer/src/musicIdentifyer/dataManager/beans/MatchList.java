package musicIdentifyer.dataManager.beans;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Lambda on 2016/11/13.
 */
public class MatchList{
    ArrayList<Match> matchlist = new ArrayList<Match>();

    public void add(Match match)
    {
        boolean exist = false;
        for(int i = 0;i<matchlist.size();i++)
        {
            if(matchlist.get(i).offset == match.offset) {
                matchlist.get(i).times += 1;
                exist = true;
                break;
            }
        }
        if(exist == false) {
            match.times++;
            matchlist.add(match);
        }
    }

    public int getScore()
    {
        int score = 0;
        for(int i = 0;i<matchlist.size();i++)
        {
            //System.out.println("offset "+matchlist.get(i).offset+"times: "+matchlist.get(i).times);
            if(score<matchlist.get(i).times)
                score = matchlist.get(i).times;
        }
        return score;
    }

    public void empty()
    {
        matchlist.clear();
    }

    public void printHistogram(int threshold)//打印直方图
    {
        Collections.sort(matchlist);
        System.out.print("峰值 :");
        for(int i = 0;i<matchlist.size();i++)
        {
            System.out.printf("%4d",matchlist.get(i).times);
            System.out.print('\t');
        }
        System.out.println();
        System.out.print("offset:");
        for(int i = 0;i<matchlist.size();i++)
        {
            System.out.printf("%4d",matchlist.get(i).offset);
            System.out.print('\t');
        }
        System.out.println();
    }

}
