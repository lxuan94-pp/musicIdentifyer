package musicIdentifyer.fingerPrint;

import musicIdentifyer.dataManager.DataManager;
import musicIdentifyer.fileReader.MusicFileReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Charles on 2016/11/11.
 */

//该类用来录取指纹并存入数据库
public class Inputer {
    String filePath;
    int id;
    public Inputer(String filePath,int id) throws IOException
    {
        this.filePath=filePath;
        this.id=id;
        initial();//在初始化过程中完成信息录入
    }
    private void initial() throws IOException {
        /*
        * 录入指纹
        * */


        //读取单首音乐
        MusicFileReader myFileReader=new MusicFileReader(filePath);
        //指纹读取
        FingerPrintReader myFingerPrintReader=new FingerPrintReader(id);

        //将整个时域信息转换成一帧一帧来读取
        double[] allDomain=myFileReader.getTimeDomain();
        System.out.println("id为"+id+"的歌已读取完毕，等待采集指纹");
        for (int i=0;i<(allDomain.length/4096);i++)
        {
            double [] one=new double[4096];
            for (int j=0;j<4096;j++)
            {
                one[j]=allDomain[i*4096+j];
            }
            myFingerPrintReader.append(FFT.fft(one));//先将时域信息转换为频域信息再进行指纹读取
        }
        ArrayList<ShazamHash> Hashes=myFingerPrintReader.combineHash();
        System.out.println("id为"+id+"的歌已采集完信息指纹，等待存入数据库");

        //将指纹信息录入到数据库中
        DataManager myDataManager=new DataManager();
        myDataManager.saveHash(Hashes);
        System.out.println("id为"+id+"的歌已经录入数据库");
        myDataManager.closeDataManager();//关闭数据库

    }

    public static int inputSongName(String songName)
    {
        DataManager myDataManager=new DataManager();
        myDataManager.saveSongInfo(songName);
        int id=myDataManager.getSongId(songName);
        myDataManager.closeDataManager();//关闭数据库
        return  id;
    }

    public static void main(String[]args) throws IOException
    {
        //示例，一首歌一个对象
        //int songID=inputSongName("Ashes Remain - On My Own.wav");
        //Inputer myInputer=new Inputer("X:\\BaiduYunDownload\\MusicDatabase\\Ashes Remain - On My Own.wav",songID);

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入读入歌曲的文件夹目录,其中分隔符请用双反斜杠：");
        String musicFileFolder = scanner.nextLine();

        File file = new File(musicFileFolder);
        String music[];
        music = file.list();
        for(int i = 0;i<music.length;i++)
        {
            String musicLocation = musicFileFolder + "\\"+music[i];
            int songID=inputSongName(music[i].replace("'","\\'"));
            if(songID == -1)
                break;
            System.out.println("录入歌曲:"+musicLocation);
            Inputer myInputer=new Inputer(musicLocation,songID);
        }

        DataManager myDataManager=new DataManager();
        myDataManager.setHash();
        System.out.println("聚集索引建立完毕");
        myDataManager.closeDataManager();//关闭数据库

    }
}
