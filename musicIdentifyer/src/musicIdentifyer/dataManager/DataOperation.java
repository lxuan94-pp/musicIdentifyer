package musicIdentifyer.dataManager;
import musicIdentifyer.dataManager.beans.Match;
import musicIdentifyer.dataManager.beans.Song;
import musicIdentifyer.dataManager.beans.Songfinger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Created by Lambda on 2016/10/29.
 */
public class DataOperation {

    private Connection con;

    public boolean conn()
    {
        ConnectMysql c=new ConnectMysql();
        con = c.getConn();
        if(con == null)
            return false;
        else
            return true;
    }

    public boolean close()
    {
        try {
            if (con != null) {
                con.close();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("关闭con对象失败");
            e.printStackTrace();
        }
        return false;
    }

    public String test()
    {//测试函数
        try {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultset = null;
            resultset = stmt.executeQuery("select now() as Date");

            resultset.last();
            int count = resultset.getRow();
            resultset.beforeFirst();

            if(count == 0)
                return null;
            int i = 0;
            while(resultset.next()){
                return resultset.getString("Date");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveSongInfo(String name)
    {//存储歌曲信息
        try {
            Statement stmt = con.createStatement();
            //System.out.println("insert into song values(null,"+"'"+name+"'"+")");
            int succ = stmt.executeUpdate("insert into song values(null,"+"'"+name+"'"+")");
            if(succ == 0)
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Song getSongInfoByName(String name)
    {//得到歌曲的信息
        Song song = new Song();
        try{
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultset = null;
            resultset = stmt.executeQuery("select * from song where name = "+"'"+name+"'");

            //获取记录个数
            resultset.last();
            int count = resultset.getRow();
            resultset.beforeFirst();
            //如果记录为0返回null
            if(count == 0)
                return null;

            resultset.next();
            song.setId(Integer.parseInt(resultset.getString("id")));
            song.setName(name);

        }catch (SQLException e){
            e.printStackTrace();
        }

        return song;
    }

    public int[] getSongIdList()
    {
        int[] list = null;
        try{
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultset = null;
            resultset = stmt.executeQuery("select id from song ");

            //获取记录个数
            resultset.last();
            int count = resultset.getRow();
            resultset.beforeFirst();
            //如果记录为0返回null
            if(count == 0)
                return null;

            list = new int[count];

            int i = 0;
            while (resultset.next()) {
                list[i] = Integer.parseInt(resultset.getString("id"));
                //循环数加一
                i++;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }



    public Song getSongInfoById(int id)
    {//得到歌曲的信息
        Song song = new Song();
        try{
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultset = null;
            resultset = stmt.executeQuery("select * from song where id = "+id);

            //获取记录个数
            resultset.last();
            int count = resultset.getRow();
            resultset.beforeFirst();
            //如果记录为0返回null
            if(count == 0)
                return null;

            resultset.next();
            song.setId(Integer.parseInt(resultset.getString("id")));
            song.setName(resultset.getString("name"));

        }catch (SQLException e){
            e.printStackTrace();
        }

        return song;
    }

    public boolean saveSongFinger(int song_id,int finger_id,int offset)
    {//存储歌曲指纹对照表
        try {
            Statement stmt = con.createStatement();
            //System.out.println("insert into songfinger values("+song_id+","+finger_id+","+offset+")");
            int succ = stmt.executeUpdate("insert into songfinger values("+song_id+","+finger_id+","+offset+")");
            if(succ == 0)
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public Songfinger[] getSongfingerBySong_id(int song_id)
    {//根据歌曲id反查歌曲指纹对照表
        Songfinger songfingers[]= null;

        try{
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultset = null;
            resultset = stmt.executeQuery("select * from songfinger where song_id = "+song_id);

            //获取记录个数
            resultset.last();
            int count = resultset.getRow();
            resultset.beforeFirst();
            //如果记录为0返回null
            if(count == 0)
                return null;

            songfingers = new Songfinger[count];

            int i = 0;
            while (resultset.next()) {
                songfingers[i] = new Songfinger();
                songfingers[i].setSong_id(song_id);
                songfingers[i].setFinger_id(Integer.parseInt(resultset.getString("finger_id")));
                songfingers[i].setOffset(Integer.parseInt(resultset.getString("offset")));
                //循环数加一
                i++;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return songfingers;
    }

    public Songfinger[] getSongfingerByFinger_id(int finger_id)
    {//通过finger_id反查歌曲指纹表
        Songfinger songfingers[]= null;

        try{
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultset = null;
            resultset = stmt.executeQuery("select * from songfinger where finger_id = "+finger_id);

            //获取记录个数
            resultset.last();
            int count = resultset.getRow();
            resultset.beforeFirst();
            //如果记录为0返回null
            if(count == 0)
                return null;

            songfingers = new Songfinger[count];

            int i = 0;
            while (resultset.next()) {
                songfingers[i] = new Songfinger();
                songfingers[i].setSong_id(Integer.parseInt(resultset.getString("song_id")));
                songfingers[i].setFinger_id(finger_id);
                songfingers[i].setOffset(Integer.parseInt(resultset.getString("offset")));
                //循环数加一
                i++;
            }
            resultset = null;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return songfingers;
    }

    public boolean setHash()
    {//建立聚集索引
        try {
            Statement stmt = con.createStatement();
            int succ = stmt.executeUpdate("create index ix_TB on songfinger(finger_id)");
            if(succ == 0)
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    public boolean saveMatch(int id, int offset)
    {//匹配过程
        Match match = new Match();
        try{
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultset = stmt.executeQuery("select * from matchsong where id = "+id+" AND offset = "+offset);

            //获取记录个数
            resultset.last();
            int count = resultset.getRow();
            resultset.beforeFirst();
            //如果记录为0
            if(count == 0)
            {
                try {
                    Statement stmt2 = con.createStatement();
                    int succ2 = stmt2.executeUpdate("insert into matchsong values("+id+","+offset+","+1+")");
                    if(succ2 == 0)
                        return false;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            }
            //如果不是0则设置times+1
            resultset.next();
            match.times = Integer.parseInt(resultset.getString("value")) + 1;
            try {
                Statement stmt2 = con.createStatement();
                int succ2 = stmt2.executeUpdate("update matchsong set value = "+match.times+" where id = "+id+" AND offset = "+offset);
                if(succ2 == 0)
                    return false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            resultset = null;
        }catch (SQLException e){
            e.printStackTrace();
        }

        return true;
    }

    public Match[] getScore()
    {
        Match matchs[] = null;

        try{
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultset = null;
            //System.out.println("select id,value,max(value) from matchsong group by id order by value desc");
            resultset = stmt.executeQuery("select id,max(value) from matchsong group by id order by max(value) desc");

            //获取记录个数
            resultset.last();
            int count = resultset.getRow();
            resultset.beforeFirst();
            //如果记录为0返回null
            if(count == 0)
                return null;

            matchs = new Match[5];
            for(int i = 0;i<5;i++)
                matchs[i] = new Match();

            int i = 0;
            while (resultset.next()&&i<5) {
                matchs[i].id = Integer.parseInt(resultset.getString("id"));
                matchs[i].times = Integer.parseInt(resultset.getString("max(value)"));
                //循环数加一
                i++;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return matchs;
    }

    public void emptyEverything()
    {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("delete from finger;");
            stmt.executeUpdate("delete from songfinger;");
            stmt.executeUpdate("delete from matchsong;");
            stmt.executeUpdate("delete from song");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void emptyMatchSong()
    {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("delete from matchsong;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void emptyWholeMySqlExceptSongList()
    {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("delete from finger;");
            stmt.executeUpdate("delete from songfinger;");
            stmt.executeUpdate("delete from matchsong;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
