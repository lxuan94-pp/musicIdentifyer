package musicIdentifyer.dataManager.beans;


/**
 * Created by Lambda on 2016/11/7.
 */
public class Match implements Comparable<Match>{
    public int id;
    public int offset;
    public int times;

    public Match()
    {
        id = -1;
        offset = 0;
        times = 0;
    }

    @Override
    public int compareTo(Match m2)
    {
        if(this.offset>m2.offset)
            return -1;
        else if(this.offset<m2.offset)
            return 1;
        else
            return 0;
    }

}
