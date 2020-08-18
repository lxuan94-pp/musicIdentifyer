package musicIdentifyer.dataManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Lambda on 2016/10/29.
 */
public class ConnectMysql {

    private String dbDriver="com.mysql.jdbc.Driver";
    private String dbUrl="jdbc:mysql://localhost:3306/db";//根据实际情况变化
    private String dbUser="root";
    private String dbPass="123456";
    private Connection conn=null;

    public Connection getConn()
    {
        try
        {
            Class.forName(dbDriver);
            System.out.println("加载数据库驱动成功");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("加载数据库驱动失败");
            e.printStackTrace();
        }
        try
        {
            conn = DriverManager.getConnection(dbUrl,dbUser,dbPass);//注意是三个参数
            System.out.println("创建数据库连接成功");
        }
        catch (SQLException e)
        {
            System.out.print(conn);
            System.out.println("创建数据库连接失败");
            conn = null;
            e.printStackTrace();
        }
        return conn;
    }

    public boolean closed()
    {
        try {
            if (conn != null) {
                conn.close();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("关闭con对象失败");
            e.printStackTrace();
        }
        return false;
    }

}
