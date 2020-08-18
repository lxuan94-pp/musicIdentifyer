package musicIdentifyer.dataManager;

/**
 * Created by Lambda on 2016/10/29.
 */


/**test dataManager*/
public class Main_Data {

    public static void main(String args[]){
        DataOperation data = new DataOperation();
        data.conn();
        data.test();
        data.close();
    }
}
