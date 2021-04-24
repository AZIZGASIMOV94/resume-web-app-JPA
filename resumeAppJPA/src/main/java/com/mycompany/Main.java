package com.mycompany;

import com.mycompany.bean.UserTable;
import com.mycompany.dao.inter.UserDaoInter;

/**
 *
 * @author azizg
 * 
 */
public class Main {

    public static void main(String[] args) throws Exception {
        //loosly coupling 
        UserDaoInter dao = Context.instanceUserDao();
        UserTable u = dao.getUserByEmail("test2@mail.ru");
        System.out.println(u);
//        UserTable u = dao.getById(1);
       // System.out.println(u.getNationality().getName());
//        System.out.println(u.getEmail());
//        System.out.println("result = "+ dao.deleteUser(12));
    }
}
