package musicIdentifyer.fingerPrint;

/**
 * Created by Charles on 2016/11/8.
 */
public class BFPRT {

    /*BFPRT算法
    *
    * 先求通过递归求出递归中位数
    * 再以中位数为主元进行快排
    *
    * 只对较大的一边的进行以上两者的递归
    *
    * */



    //插入排序
   static private void insertSort(double [] data,int l,int r)
    {
        for (int i=l+1;i<=r;i++)
        {
            if (data[i-1]<data[i])
            {
                double num=data[i];
                int index=i;
                while (index>l  && data[index-1]<num )
                {
                    data[index]=data[index-1];
                    index--;
                }
                data[index]=num;
            }
        }
    }

    //中位数寻找
    static private double findMid(double [] data,int l,int r)
    {
        //范围相同
        if (l>=r)
        {
            return data[l];
        }

        //每五个求一次中位数
        int n=0;
        int i=l;
        for (;i<r-5;i+=5)
        {
            insertSort(data,i,i+4);
            n=i-l;
            swap(data,l+n/5,i+2);
        }

        //处理剩余元素
        int num=r-i+1;
        if (num>0)
        {
            insertSort(data,i,i+num-1);
            n+=5;
            swap(data,l+n/5,i+num/2);
            if (i==l)
            {
                swap(data,l+n/5,l);
            }
        }

        n/=5;
        if (n<=1)
        {
            return data[l];
        }

        return findMid(data,l,l+n);//递归寻找
    }
    //交换数据
    static private  void swap(double[] data,int i,int j)
    {
        double c;
        c=data[i];
        data[i]=data[j];
        data[j]=c;
    }

    //快速排序
    static private int partition(double[] data,int headId,int tailId)
    {
        swap(data,headId,tailId);//以中位数为主元
        int posSlow=headId-1;
        int posFast=headId;
        for (;posFast<tailId;posFast++)
        {
            if (data[posFast]>data[tailId])
            {
                posSlow++;
                swap(data,posFast,posSlow);
            }
        }
        posSlow++;
        swap(data,posSlow,tailId);
        return  posSlow;//返回主元的位置

    }

    //求前K个最大元素
    static public double[] BFPRT(double[] data,int headId,int tailId,int k)
    {
        if (headId>=tailId)
        {
            return data;
        }

        findMid(data,headId,tailId);//data数列的第一项为递归中位数
        int i=partition(data,headId,tailId);//进行快速排序
        int num=i-headId+1;//较大的数的数目

        if(num==k)//完成最值求解
        {
            return data;
        }
        else if (num>k)
        {
            return BFPRT(data,headId,i-1,k);
        }else
        {
            return BFPRT(data,i+1,tailId,k);
        }
    }
}
