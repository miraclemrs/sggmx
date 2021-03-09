import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author mx
 * @create 2021-02-19-7:24 下午
 */
public class JDBCutils {
    //获取数据库连接
    public static Connection getConnection() throws Exception{
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pros = new Properties();
        pros.load(is);
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");
        //2.加载驱动
        Class.forName(driverClass);
        //3.获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }
    //关闭连接和Statement的操作
    public static void closeResource(Connection conn, Statement ps){
        try{
            if(ps != null)
                ps.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        try{
            if(conn != null)
                conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void closeResource(Connection conn, Statement ps, ResultSet rs){
        try{
            if(ps != null)
                ps.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        try{
            if(conn != null)
                conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        try{
            if(rs != null)
                rs.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
