import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * @author mx
 * @create 2021-03-06-4:33 下午
 */
public class CustomerDAOImpl extends BaseDAO<Customer> implements CustomersDAO{

    @Override
    public void insert(Connection connection, Customer cust) {
        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        update(connection,sql,cust.getName(),cust.getEmail(),cust.getBirth());
    }

    @Override
    public void deleteById(Connection connection, int id) {
        String sql = "delete from customers where id = ?";
        update(connection,sql,id);
    }

    @Override
    public void update(Connection connection, Customer cust) {
        String sql = "update customers set name = ?,email = ?,birth = ? where id = ?";
        update(connection,sql,cust.getName(),cust.getEmail(),cust.getBirth(),cust.getId());
    }

    @Override
    public Customer getCustomerById(Connection connection, int id) {
        String sql = "select id,name,email,birth from customers where id = ?";
        Customer customer = getInstance(connection, sql, id);

        return customer;
    }

    @Override
    public List<Customer> getAll(Connection connection) {
        String sql = "select id,name,email,birth from customers";
        List<Customer> list = getForList(connection,sql);
        return list;
    }

    @Override
    public Long getCount(Connection connection) {
        String sql = "select count(*) from customers";
        return getValue(connection,sql);
    }

    @Override
    public Date getMaxBirth(Connection connection) {
        String sql = "select max(birth) from customers";
        return getValue(connection,sql);
    }
}
