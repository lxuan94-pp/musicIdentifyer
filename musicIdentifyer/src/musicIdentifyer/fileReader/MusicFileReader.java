package musicIdentifyer.fileReader;
import java.io.*;
/**
 * Created by Charles on 2016/10/29.
 */
public class MusicFileReader
{
    private String filePath;//文件地址
    private double[] timeDomain;//时域信息
    //构造函数
    public  MusicFileReader(String filePath)
    {
        this.filePath=filePath;
    }

    //获得时域信息
    public double[] getTimeDomain() throws IOException
    {
        convert();
        return timeDomain;
    }

    private int toInt(byte[] b) {
        int s = 0;
        int s0 = b[0] & 0xff;// 最低位
        int s1 = b[1] & 0xff;
        int s2 = b[2] & 0xff;
        int s3 = b[3] & 0xff;
        s3 <<= 24;
        s2 <<= 16;
        s1 <<= 8;
        s = s0 | s1 | s2 | s3;
        return s;
    }
    private short toShort(byte[] b) {
        return (short)((b[1] << 8) + (b[0] << 0));
    }

    private   byte[] read(RandomAccessFile rdf, int pos, int length) throws IOException
    {
        rdf.seek(pos);
        byte result[] = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = rdf.readByte();
        }
        return result;
    }

    //读取文件转换成byte数组
    private byte[]  readFile() throws IOException
    {
        //读取文件并进行判断
        File f = new File(filePath);
        RandomAccessFile rdf =new RandomAccessFile(f,"r");
        System.out.println("ChunkID: " + toInt(read(rdf, 0, 4))); // RIFF格式 = "RIFF" RIFF:  0x46464952 = 1179011410 故出现1179011410就是RIFF
        System.out.println("AudioFormat: " + toShort(read(rdf, 20, 2))); // 音频格式 1 = PCM
        System.out.println("NumChannels: " + toShort(read(rdf, 22, 2))); // 1 单声道 2 双声道
        System.out.println("SampleRate: " + toInt(read(rdf, 24, 4)));  // 采样率
        System.out.println("BitsPerSample: " + toShort(read(rdf, 34, 2)));  // 位深度
        rdf.close();
        //获取byte数组
        FileInputStream instream = new FileInputStream(new File(filePath));
        int length = instream.available();
        byte[] list = new byte[length];//创建byte数组
        instream.read(list);//读取数组
        return list;

    }


    //进行数组类型转换
    private void convert() throws IOException
    {
        byte[] theByte=readFile();
        timeDomain=new double[(theByte.length)/2];
        for (int i = 0; i < timeDomain.length ; i++)//不读前46位
        {
            byte bl = theByte[2 * i];
            byte bh = theByte[2 * i + 1];
            short s = (short) (((bh & 0x00FF) << 8 )| (bl & 0x00FF));
            timeDomain[i] = s / Math.pow(2.0,16);//模糊化，会增加匹配数量，但是相对大小不会改变
        }


    }

}
