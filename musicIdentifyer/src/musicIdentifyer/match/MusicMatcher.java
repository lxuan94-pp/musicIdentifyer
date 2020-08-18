package musicIdentifyer.match;
import musicIdentifyer.dataManager.DataManager;
import musicIdentifyer.dataManager.beans.Match;
import musicIdentifyer.fileReader.MusicFileReader;
import musicIdentifyer.fingerPrint.FFT;
import musicIdentifyer.fingerPrint.FingerPrintReader;
import musicIdentifyer.fingerPrint.ShazamHash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Charles on 2016/10/29.
 */
public class MusicMatcher {
    String filePath;//待匹配的文件地址
    Match[] topFive;//匹配成功的前五名

    //传入一个地址即可进行匹配
    public  MusicMatcher(String filePath) throws IOException {
        this.filePath=filePath;
        topFive=top();

    }

    private void result()
    {
        for(Match match:topFive)
        {
            DataManager myDataManager=new DataManager();
            String name = myDataManager.getSongName(match.id);
            myDataManager.closeDataManager();//关闭数据库
            System.out.println("匹配歌曲id:"+match.id+"匹配歌曲名称:"+name+" 打分:"+match.times);
        }
    }



    private Match[] top() throws IOException
    {
        /*
        * 录入指纹
        * */

        //读取单首音乐
        MusicFileReader myFileReader=new MusicFileReader(filePath);

        //指纹读取
        FingerPrintReader myFingerPrintReader=new FingerPrintReader();

        //将整个时域信息转换成一帧一帧来读取
        double[] allDomain=myFileReader.getTimeDomain();
        System.out.println("需要匹配的歌已读取完毕，等待采集指纹");
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
        System.out.println("需要匹配的歌已采集完信息指纹，等待匹配");

        //将指纹信息与数据库信息相匹配
        DataManager myDataManager=new DataManager();
        System.out.println("匹配开始 时间:"+myDataManager.printTime());
        topFive = myDataManager.match(Hashes);
        System.out.println("匹配结束 时间"+myDataManager.printTime());
        myDataManager.closeDataManager();//关闭数据库

        return topFive;

    }
    public static void main(String[]args) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入匹配歌曲的地址,其中分隔符请用双反斜杠：");
        String music = scanner.nextLine();

        //String music = "X:\\BaiduYunDownload\\MusicDatabase\\Kobe Bryant,Tyra Banks - K.O.B.E_01.wav";
     
        MusicMatcher musicMatcher = new MusicMatcher(music);
        System.out.println("开始匹配歌曲"+music);
        System.out.println("匹配歌曲指纹录入完毕；");
        musicMatcher.result();
    }
}
