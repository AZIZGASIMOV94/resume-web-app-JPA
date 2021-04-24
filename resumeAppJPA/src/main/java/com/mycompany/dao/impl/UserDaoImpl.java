package com.mycompany.dao.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.mycompany.bean.UserTable;
import com.mycompany.dao.inter.AbstractDAO;
import com.mycompany.dao.inter.UserDaoInter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class UserDaoImpl extends AbstractDAO implements UserDaoInter {
    
    
   /* private UserTable getUser(ResultSet resultSet) throws Exception{
        int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String profileDesc = resultSet.getString("profile_desc");
                String address = resultSet.getString("address");
                Date birthdate = resultSet.getDate("birthdate");
                int birthplace = resultSet.getInt("birthplace_id");
                int nationality = resultSet.getInt("nationality_id");
                String birthplaceStr = resultSet.getString("birthplace");
                String nationalityStr = resultSet.getString("nationality");
                return new UserTable(id,name,surname,email,phone,profileDesc,address,birthdate,
                                new CountryTable(birthplace,birthplaceStr,null),
                                new CountryTable(nationality,null,nationalityStr));
    }

    private UserTable getLoggedInUser(ResultSet resultSet) throws Exception{
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String surname = resultSet.getString("surname");
        String email = resultSet.getString("email");
        String phone = resultSet.getString("phone");
        String password = resultSet.getString("user_password");
        String profileDesc = resultSet.getString("profile_desc");
        String address = resultSet.getString("address");
        Date birthdate = resultSet.getDate("birthdate");
        int birthplace = resultSet.getInt("birthplace_id");
        int nationality = resultSet.getInt("nationality_id");

        UserTable user = new UserTable(id,name,surname,email,phone,profileDesc,address,birthdate,null,null);
        user.setPassword(password);
        return user;
    }*/

    @Override
    public List<UserTable> getAllUsers() {
        List<UserTable> res = new ArrayList<>();
        try( Connection con = dbConnect();) {
            Statement statement = con.createStatement();
            statement.execute("select "
                                + "u.*, "
                                + "n.nationality as nationality, "
                                + "c.name as birthplace "
                                + "FROM user_table u "
                                + "LEFT join country_table n on u.nationality_id = n.id "
                                + "LEFT JOIN country_table c on u.birthplace_id = c.id");
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()){
              // UserTable u = getUser(resultSet);
              // res.add(u);
            }
            //close connection
           // con.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return res;
    } 
    
    /**
     * This function written utilizing JPQL 
     * instead of tables we are working with classes/entities 
     * check:
     * @param jpql 
     * @return 
     */
    @Override
    public List<UserTable> searchUsers(String name, String surname, Integer nationalityId) {
        EntityManager em = entityManage();
        String jpql = "SELECT u FROM UserTable u WHERE 1=1";

        List<UserTable> res = new ArrayList<>();

        if(name != null && !name.trim().isEmpty()){
            jpql += " and u.name=:name ";
        }
        if(surname != null && !surname.trim().isEmpty()){

            jpql += " and u.surname=:surname ";
        }
        if(nationalityId != null ){
            jpql += " and u.nationality.id=:nid ";
        }

        Query query = em.createQuery(jpql, UserTable.class);
        
        if(name != null && !name.trim().isEmpty()){
              query.setParameter("name", name);
        }
        if(surname != null &&!surname.trim().isEmpty()){
            query.setParameter("surname", surname);
        }
        if(nationalityId != null ){
            query.setParameter("nid", nationalityId);
        }
        
        List<UserTable> userList = query.getResultList();
        
        return userList;
    }
    
    
    /*
      This function written utilizing JPQL function 
    */
//    @Override
//    public UserTable getUserByEmailAndPass(String email, String password) {
//        EntityManager em = entityManage(); 
//        Query query = em.createQuery("SELECT u FROM UserTable u WHERE u.email=:e AND u.password=:p",UserTable.class);
//        query.setParameter("e", email);
//        query.setParameter("p", password);
//        
//        List<UserTable> userList = query.getResultList();
//        if(userList.size()==1){
//            return userList.get(0);
//        }
//        return null;
//    }
    
    /**
     * This function written utilizing JPQL, Criteria Builder
     * @param email
     * @param password
     * @return 
     */
    @Override
    public UserTable getUserByEmailAndPass(String email, String password) {
        EntityManager em = entityManage(); 
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserTable> q1 = cb.createQuery(UserTable.class);
        Root<UserTable> userRoot = q1.from(UserTable.class);
        CriteriaQuery<UserTable> q2 = q1
                .where(cb.equal(userRoot.get("email"), email),cb.equal(userRoot.get("password"), password));
      
        Query query = em.createQuery(q2);
        
        List<UserTable>userList = query.getResultList();
        if(userList.size()==1){
            return userList.get(0);
        }
        return null;
    }
    
    /*
       Using Native Sql Query in JPA function 
    */
    @Override
    public UserTable getUserByEmail(String email){
        EntityManager em = entityManage(); 
        
        Query query = em.createNativeQuery("SELECT * FROM user_table WHERE email = ?",UserTable.class);
        query.setParameter(1, email);
        List<UserTable>userList = query.getResultList();
        if(userList.size()==1){
            return userList.get(0);
        }
        return null;
    }
    
    
    /*
       Using Named Query check the head of entities
    */
//    @Override
//    public UserTable getUserByEmail(String email){
//        EntityManager em = entityManage(); 
//        
//        Query query = em.createNamedQuery("UserTable.findByEmail",UserTable.class);
//        query.setParameter("email", email);
//        List<UserTable>userList = query.getResultList();
//        if(userList.size()==1){
//            return userList.get(0);
//        }
//        return null;
//    }
    
    
    
    /*
       This function is written utilizing Criteria Builder
    */
//    @Override
//    public UserTable getUserByEmail(String email){
//        EntityManager em = entityManage(); 
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<UserTable> q1 = cb.createQuery(UserTable.class);
//        Root<UserTable> userRoot = q1.from(UserTable.class);
//        CriteriaQuery<UserTable> q2 = q1
//                .where(cb.equal(userRoot.get("email"), email));
//        
//        Query query = em.createQuery(q2);
//       
//        List<UserTable>userList = query.getResultList();
//        if(userList.size()==1){
//            return userList.get(0);
//        }
//        return null;
//    }
      
    /**
     * this function is written using JPQL
     * @param 
     * @return 
     */
//    @Override
//    public UserTable getUserByEmail(String email){
//        EntityManager em = entityManage(); 
//        Query query = em.createQuery("SELECT u FROM UserTable u WHERE u.email=:e",UserTable.class);
//        query.setParameter("e", email);
//        List<UserTable> userList = query.getResultList();
//        if(userList.size()==1){
//            return userList.get(0);
//        }
//        return null;
//    }

    @Override
    public UserTable getById(int userId){
        EntityManager em = entityManage();
        UserTable user = em.find(UserTable.class, userId);
        
        em.close();
        return user;
    }
    
    @Override
    public boolean updateUser(UserTable u) {
        EntityManager em = entityManage();
        
        em.getTransaction().begin();//open transaction
        em.merge(u);
        em.getTransaction().commit();
        
        em.close();
        return true;
    }

    @Override
    public boolean deleteUser(int id) {
        EntityManager em = entityManage();
        UserTable user = em.find(UserTable.class, id);
        
        em.getTransaction().begin();//open transaction
        em.remove(user);
        em.getTransaction().commit();
        
        em.close();
        return true;
    }

    private BCrypt.Hasher crypt = BCrypt.withDefaults();

    @Override
    public boolean addUser(UserTable u) {
        u.setPassword(crypt.hashToString(4, u.getPassword().toCharArray()));
        EntityManager em = entityManage();
        
        em.getTransaction().begin();//open transaction
        em.persist(u);
        em.getTransaction().commit();
        
        em.close();
        return true;
    }

    public static void main(String[] args) {
        UserTable u2 = new UserTable(23, "test2", "testov2","test2@mail.ru", "123456", "+994555955858");
        new UserDaoImpl().addUser(u2);
        System.out.println("added");
    }
}
