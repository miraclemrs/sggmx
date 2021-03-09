import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mx
 * 封装了针对于数据表的通用的操作
 * @create 2021-03-06-4:36 下午
 */
public class BaseDAO<T> {
    private Class<T> clazz = null;
    {
        //获取当前BaseDAO的子类继承的父类中的泛型
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = paramType.getActualTypeArguments();    //获取了父类的泛型
        clazz = (Class<T>)actualTypeArguments[0];   //泛型的第一个参数
    }
    //通用的增删改操作---version2。0（考虑上事务）
    public int update(Connection connection, String sql, Object ...args){//sql中占位符的个数应该与可变形参的长度一致
        //1.获取数据库的连接
        //2。预编译sql语句，返回PreparedStatement的实例
        java.sql.PreparedStatement ps = null;
        try{
            ps = connection.prepareStatement(sql);
            //3。填充占位符
            for(int i = 0;i < args.length;i++){
                ps.setObject(i + 1,args[i]);   //小心参数声明错误
            }
            return ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            JDBCutils.closeResource(null,ps);
        }
        return 0;
    }
    //获取一个实例
        public T getInstance(Connection connection,String sql,Object ...args){
            PreparedStatement ps = null;
            ResultSet resultSet = null;
            try{
                connection = JDBCutils.getConnection();
                ps = connection.prepareStatement(sql);
                for(int i = 0;i < args.length;i++){
                    ps.setObject(i + 1,args[i]);
                }
                resultSet = ps.executeQuery();
                //获取结果集的元数据
                ResultSetMetaData rsmd = resultSet.getMetaData();
                //获取列数
                int columnCount = rsmd.getColumnCount();
                if(resultSet.next()){
                    T t = clazz.newInstance();
                    for(int i = 0;i < columnCount;i++){
                        //获取每个列的列值
                        Object columnValue = resultSet.getObject(i + 1);
                        //获取每个列的列名
                        String columnName = rsmd.getColumnLabel(i + 1);
                        //通过反射，将对象指定名columnName的属性名的值赋给指定的值columnValue
                        Field field = clazz.getDeclaredField(columnName);
                        field.setAccessible(true);
                        field.set(t,columnValue);
                    }
                    return t;
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                JDBCutils.closeResource(null,ps,resultSet);
            }
            return null;
        }
        //获取一个列表
        public List<T> getForList(Connection connection, String sql, Object ...args){
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try{
            connection = JDBCutils.getConnection();
            ps = connection.prepareStatement(sql);
            for(int i = 0;i < args.length;i++){
                ps.setObject(i + 1,args[i]);
            }
            resultSet = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = resultSet.getMetaData();
            //获取列数
            int columnCount = rsmd.getColumnCount();
            //创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while(resultSet.next()){
                T t = clazz.newInstance();
                //处理结果集一行数据中的每一个列，给t对象指定的属性赋值
                for(int i = 0;i < columnCount;i++){
                    //获取每个列的列值
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取每个列的列名
                    String columnName = rsmd.getColumnLabel(i + 1);
                    //通过反射，将对象指定名columnName的属性名的值赋给指定的值columnValue
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                list.add(t);
            }
            return list;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            JDBCutils.closeResource(null,ps,resultSet);
        }
        return null;
    }
    //用于查询特殊值的通用的方法
    public <E> E getValue(Connection conn,String sql,Object...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = conn.prepareStatement(sql);
        for(int i = 0;i < args.length;i++){
            ps.setObject(i + 1,args[i]);
        }
        rs = ps.executeQuery();
        if(rs.next()){
            return (E) rs.getObject(1);
        }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            JDBCutils.closeResource(null,ps,rs);
        }
        return null;
    }
}
