import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;

/**
 * @author mx
 * @create 2021-03-06-8:58 下午
 */
public class CustomerDAOImplTest {
    private CustomerDAOImpl dao = new CustomerDAOImpl();

    @Test
    public void testInsert(){
        Connection connection = null;
        try{
            connection = JDBCutils.getConnection();
        Customer customer = new Customer(1, "于小飞", "xiaofei@126.com", new Date(1232142142L));
        dao.insert(connection,customer);
            System.out.println("添加成功");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            JDBCutils.closeResource(connection,null);
        }
    }
}
