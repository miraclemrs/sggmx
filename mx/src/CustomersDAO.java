import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * @author mx
 * 此接口用于规范针对于customers 表的常用操作
 * * @create 2021-03-05-11:10 下午
 */
public interface CustomersDAO {
    /**
    * @author mx
    * @Description 将cust对象添加到数据库中
    * @Date 4:06 下午 2021/3/6
    * @Param
    * @return
    */
    void insert(Connection connection,Customer cust);
    /**
    * @author mx
    * @Description 针对指定的id，删除表中的一条记录
    * @Date 4:16 下午 2021/3/6
    * @Param [connection, id]
    * @return void
    */
    void deleteById(Connection connection,int id);
    /**
    * @author mx
    * @Description 针对内存中的cust对象，去修改数据表中指定的记录
    * @Date 4:20 下午 2021/3/6
    * @Param [connection, id, customer]
    * @return void
    */
    void update(Connection connection,Customer customer);
    /**
    * @author mx
    * @Description 针对指定的id查询得到对应的Customer对象
    * @Date 4:25 下午 2021/3/6
    * @Param [connection, id]
    * @return void
    */
    Customer getCustomerById(Connection connection,int id);
    /**
    * @author mx
    * @Description 查询表中的所有记录构成的集合
    * @Date 4:28 下午 2021/3/6
    * @Param [connection]
    * @return java.util.List<Customer>
    */
    List<Customer> getAll(Connection connection);
    /**
    * @author mx
    * @Description 返回数据表中的数据的条目数
    * @Date 4:28 下午 2021/3/6
    * @Param
    * @return
    */
    Long getCount(Connection connection);
    /**
    * @author mx
    * @Description 返回数据表中最大的生日
    * @Date 4:29 下午 2021/3/6
    * @Param
    * @return
    */
    Date getMaxBirth(Connection connection);
    /**
    * @author mx
    * @Description
    * @Date 4:30 下午 2021/3/6
    * @Param
    * @return
    */

}
