package Services;

import DTO.BaseDTO;
import DTO.OrderDTO;
import DTO.UpdateOrderDTO;
import DTO.VerifyOrderDTO;
import Model.Order;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class OrderService extends BaseService<Order>{

    public OrderService(String tableName) {
        super(tableName);
    }

    @Override
    public String create(Order model) {
        this.jdbi.useHandle(handle ->
                handle.createUpdate("INSERT INTO " + this.tableName + " (id, info, transID, username, country, city, district, address, phone, email, createdAt, price, userId) VALUES " +
                        "(:id, :info, :transID, :username, :country, :city, :district, :address, :phone, :email, :createdAt, :price, :userId)")
                        .bindBean(model).execute()
        );
        return model.getId();
    }

    @Override
    protected boolean update(String id, BaseDTO model) throws Exception {
        return false;
    }
    public ArrayList<Order> findOrdersUser(String id){
        return (ArrayList<Order>) this.jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM " + this.tableName + " WHERE userId = ? order by createdAt desc ").bind(0,id).mapToBean(Order.class).list();
        });
    }
    public ArrayList<Order> findOrdersUserByDetail(String id, String detail){
        String detailSearch = "%"+detail+"%";
        return (ArrayList<Order>) this.jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM " + this.tableName + " WHERE userId = ? and info like ?").bind(0,id).bind(1,detailSearch).mapToBean(Order.class).list();
        });
    }
    public Order findOrderUserById(String userId, String orderId){
        if(orderId.isEmpty()) {
            return null;
        }
        try {
            Order data;
            data = this.jdbi.withHandle(new HandleCallback<Order, Exception>() {
                public Order withHandle(Handle handle) throws Exception{
                    try {
                        return handle.createQuery(
                                        "SELECT * FROM " + tableName + " WHERE id = ? and userId = ?")
                                .bind(0, orderId).bind(1,userId)
                                .mapToBean(Order.class).first();
                    }catch (IllegalStateException exception) {
                        return null;
                    }

                }
            });

            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Order> findOrdersPrepayment(){
        return (ArrayList<Order>) this.jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM " + this.tableName + " WHERE transID is not null").mapToBean(Order.class).list();
        });
    }
    public ArrayList<Order> findOrdersPostPaid(){
        return (ArrayList<Order>) this.jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM " + this.tableName + " WHERE transID is null").mapToBean(Order.class).list();
        });
    }
    public ArrayList<Order> findOrders(String infoSearch){
        String infoSearchDetail = "%"+infoSearch+"%";
        return (ArrayList<Order>) this.jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM " + this.tableName + " WHERE id = ? or info like ?").bind(0, infoSearch).bind(1, infoSearchDetail).mapToBean(Order.class).list();
        });
    }
    public ArrayList<Order> findOrders(String infoSearch, boolean isPrepayment){
        String infoSearchDetail = "%"+infoSearch+"%";
        String condition;
        if(isPrepayment){
            condition = "is not null";
        }else{
            condition = "is null";
        }
        return (ArrayList<Order>) this.jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM " + this.tableName + " WHERE ( id = ? or info like ? ) and transID " + condition).bind(0, infoSearch).bind(1, infoSearchDetail).mapToBean(Order.class).list();
        });
    }

    public void verifyOrder(VerifyOrderDTO dto) {
      this.jdbi.useHandle(handle -> {
        handle.createUpdate("UPDATE " + this.tableName +
                " SET publicKey = :publicKey," +
                "status = 1," +
                "updatedAt = :updatedAt " +
                "WHERE id = :id"
        ).bind("id", dto.getOrderId()).bindBean(dto).execute();
      });
    }

    public void cancelOrder(String orderId) {
      this.jdbi.useHandle(handle -> {
        handle.createUpdate("UPDATE " + this.tableName +
                " SET status = 2," +
                "updatedAt = :updatedAt " +
                "WHERE id = :id"
        ).bind("id", orderId).bind("updatedAt", LocalDate.now()).execute();
      });
    }

    public void adminUpdateOrder(UpdateOrderDTO dto) {
      this.jdbi.useHandle(handle -> {
        handle.createUpdate("UPDATE " + this.tableName +
                " SET country = :country, " +
                "city = :city, " +
                "district = :district, " +
                "address = :address, " +
                "phone = :phone, " +
                "email = :email, " +
                "updatedAt = :updatedAt " +
                "WHERE id = :id"
        ).bindBean(dto).execute();
      });
    }
}
