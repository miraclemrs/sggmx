import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author mx
 * @create 2021-02-19-5:52 下午
 */
public class PreparedStatement {
    //向customers表中添加一条记录
    @Test
    public void testInsert() throws Exception {
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
        //System.out.println(connection);
        //4.预编译sql语句，返回PreparedStatement的实例
        String sql = "insert into customers(name,email,birth)values(?,?,?)";    //占位符
        java.sql.PreparedStatement ps = connection.prepareStatement(sql);
        //5.填充占位符
        ps.setString(1,"哪吒");
        ps.setString(2,"nezha@qq.com");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = sdf.parse("1998-02-02");
        ps.setString(3, "1993-01-01");
        //6.执行sql操作
        ps.execute();
        //7.资源的关闭
        connection.close();
    }
    //修改customers表的一条记录
    @Test
    public void testUpdate(){
        //1.获取数据库的连接
        Connection connection = null;
        java.sql.PreparedStatement preparedStatement = null;
        try{
            connection = JDBCutils.getConnection();
        //2。预编译SQL语句，返回PreparedStatement的实例
        String sql = "update customers set name = ? where id = ?";
        preparedStatement = connection.prepareStatement(sql);
        //3.填充占位符
        preparedStatement.setObject(1,"莫扎特");
        preparedStatement.setObject(2,18);
        //4。执行
        preparedStatement.execute();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
        //5。资源的关闭
        JDBCutils.closeResource(connection,preparedStatement);
        }
    }

    //通用的增删改操作
    public void update(Connection connection,String sql,Object ...args){//sql中占位符的个数应该与可变形参的长度一致
        //1.获取数据库的连接
        //2。预编译sql语句，返回PreparedStatement的实例
        java.sql.PreparedStatement ps = null;
        try{
            ps = connection.prepareStatement(sql);
        //3。填充占位符
        for(int i = 0;i < args.length;i++){
            ps.setObject(i + 1,args[i]);   //小心参数声明错误
        }
        ps.execute();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            JDBCutils.closeResource(null,ps);
        }
    }
    @Test
    public void testUpdateWith(){
        Connection connection = null;
        try{
            connection = JDBCutils.getConnection();
            System.out.println(connection.getAutoCommit());
            //取消自动提交
            connection.setAutoCommit(false);
            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            update(connection,sql1,"AA");
            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            update(connection,sql2,"BB");
            System.out.println("转账成功");
            //提交数据
            connection.commit();
        }catch(Exception e){
            e.printStackTrace();
            //回滚数据
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }finally{
            JDBCutils.closeResource(connection,null);
        }
    }



    //批量插入数据的操作，查询、更新、删除天然具有批量操作的效果,使用PreparedStatement
    @Test
    public void test2() throws Exception {
        Connection connection = null;
        java.sql.PreparedStatement ps = null;
        try{
            long start = System.currentTimeMillis();
        connection = JDBCutils.getConnection();
        String sql = "insert into goods(name)values(?)";
        ps = connection.prepareStatement(sql);
        for(int i = 1;i <= 20000;i++){
            ps.setObject(1,"name_" + i);
            ps.execute();
        }
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为:" + (end - start));
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            JDBCutils.closeResource(connection,ps);
        }
    }

}
