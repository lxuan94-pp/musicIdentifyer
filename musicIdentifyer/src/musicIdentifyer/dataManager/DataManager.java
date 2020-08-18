package musicIdentifyer.dataManager;
import musicIdentifyer.dataManager.beans.Match;
import musicIdentifyer.dataManager.beans.MatchList;
import musicIdentifyer.dataManager.beans.Songfinger;
import musicIdentifyer.fingerPrint.ShazamHash;
import java.util.ArrayList;

/**
 * Created by Charles on 2016/10/29.
 */
public class DataManager {
    private DataOperation data = new DataOperation();

    public DataManager()
    {
        data.conn();
    }

    /*
    *调用完全部数据库操作后记得调用closeDataManager
    * 关闭数据流
     */
    public void closeDataManager()
    {
        data.close();
    }

    /*
    *显示时间函数
     */
    public String printTime()
    {
        return data.test();
    }

    /*
    *存入歌曲方法
    * 参数:歌曲的名字 String name
    * 返回:是否成功boolean
     */
    public boolean saveSongInfo(String name)
    {
        boolean succ = data.saveSongInfo(name);
        return  succ;
    }

    /*
    *通过歌曲名找到数据库中对应得id
    * 参数:歌曲的名字 String name
    * 返回: int id
     */
    public int getSongId(String name)
    {
        int id = data.getSongInfoByName(name).getId();
        return id;
    }


    /*
    *通过歌曲id找到对应歌曲
    * 参数:歌曲的id int
    * 返回：string name
     */

    public String getSongName(int id)
    {
        String name = data.getSongInfoById(id).getName();
        return name;
    }

    /*
    *获取song id list
     */
    public int[] getSongIdList()
    {
        int[] list = data.getSongIdList();
        return list;
    }

    /*
    *存入 hash ArrayList 方法
    * 参数:ArrayList<ShazamHash>
    * 返回:boolean 是否存入成功
     */
    public boolean saveHash(ArrayList<ShazamHash> hashArrayList)
    {
        for(ShazamHash hash:hashArrayList)
        {
            int finger_id = (hash.dt<<18) | (hash.f1<<9) | hash.f2;
            //存入songfinger对照表
            if(!data.saveSongFinger(hash.id,finger_id,hash.offset))
                return false;
        }
        return true;
    }

    /*
    *匹配方法：传入一首歌的ShazamHash Arraylist并在数据库中打分
    * 参数:ArrayList<ShazamHash>
    * 返回: 无
    * 传出打分情况见下一个函数
     */
    public Match[] match(ArrayList<ShazamHash> hashArrayList)
    {
        //获取数据库中歌曲库情况
        int[] list = data.getSongIdList();//数据库歌曲id表
        int[] score = new int[list.length];//每首歌的打分情况
        int[] finger_ids = new int[hashArrayList.size()];//分析hash中的finger_id
        int[] dt = new int[hashArrayList.size()];//分析hash中的dt
        MatchList matchList = new MatchList();

        int i = 0;
        for(ShazamHash hash:hashArrayList) {
            finger_ids[i] = (hash.dt<<18) | (hash.f1<<9) | hash.f2;
            dt[i] = hash.offset;
            i++;
        }


        System.out.println("数据库中需要对比的歌曲共"+list.length+"个；");
        for(int j = 0;j<list.length;j++)
        {
            System.out.println("开始对比 id= "+list[j]+" 的歌");
            Songfinger[] songfingers= data.getSongfingerBySong_id(list[j]);
            for(int k = 0;k<hashArrayList.size();k++)//截取片段指纹
            {
                for(int l = 0;l<songfingers.length;l++)//源数据库中歌曲指纹
                {
                    if(songfingers[l].getFinger_id() == finger_ids[k])
                    {
                        Match match = new Match();
                        match.times = 0;
                        match.offset = songfingers[l].getOffset() - dt[k];
                        matchList.add(match);
                    }
                }
            }
            score[j] = matchList.getScore();
            matchList.printHistogram(hashArrayList.size());
            matchList.empty();
            System.out.println("打分:"+score[j]);
            songfingers = null;
        }

        //取出前五名
        int[] topFive = new int[5];
        int[] topFiveId = new int[5];
        for(int m = 0;m<5;m++)
        {
            topFive[m] = 0;
            topFiveId[m] = 0;
        }
        for(int m = 0;m<score.length;m++)
        {
            int tempScore = score[m];
            int tempId = list[m];

            int ex = tempScore;
            int ex2 = tempId;
            for(int x = 0;x<5;x++)
            {
                if(topFive[x]<ex)
                {
                    int temp = topFive[x];
                    topFive[x] = ex;
                    ex = temp;

                    temp = topFiveId[x];
                    topFiveId[x] = ex2;
                    ex2 = temp;
                }
            }

        }

        Match[] matches2 = new Match[5];
        for(int m = 0;m<5;m++)
        {
            matches2[m] = new Match();
            matches2[m].id = topFiveId[m];
            matches2[m].times = topFive[m];
        }

        return matches2;
    }

    /*
    *建立聚集索引
     */
    public void setHash()
    {
        data.setHash();
    }

    /*
    *获取匹配打分前五名的id和相应分数
    * 参数:无
    * 返回:一个Match的数组，数组中存储了匹配前五名歌曲的id和打分，定义见beans包中的Match类
     */
    public Match[] getTopFive()
    {
        Match matchs[] = null;

        matchs = data.getScore();

        return matchs;
    }

    /*
    *清空所有表
     */
    public void emptyEverything()
    {
        data.emptyEverything();
    }

    /*
    *清空除了song表以外所有表
     */
    public void emptyEverythingExceptSonglist()
    {
        data.emptyWholeMySqlExceptSongList();
    }

    /*
    *清空match表
     */
    public void emptyMatchList()
    {
        data.emptyMatchSong();
    }


}
