package com.mycompany;

import com.mycompany.dao.impl.EmploymentHistoryDaoImpl;
import com.mycompany.dao.impl.UserSkillDaoImpl;
import com.mycompany.dao.inter.CountryDaoInter;
import com.mycompany.dao.impl.CountryDaoImpl;
import com.mycompany.dao.impl.SkillDaoImpl;
import com.mycompany.dao.impl.UserDaoImpl;
import com.mycompany.dao.inter.EmploymentHistoryDaoInter;
import com.mycompany.dao.inter.SkillDaoInter;
import com.mycompany.dao.inter.UserDaoInter;
import com.mycompany.dao.inter.UserSkillDaoInter;

/**
 *
 * @author azizg
 */
public class Context {
    public static UserDaoInter instanceUserDao(){
        return new UserDaoImpl();
    }
    
    public static CountryDaoInter instanceCountryDao(){
        return new CountryDaoImpl();
    }
    
    public static UserSkillDaoInter instanceUserSkillDao(){
        return new UserSkillDaoImpl();
    }
    
    public static EmploymentHistoryDaoInter instanceEmploymentHistoryDao(){
        return new EmploymentHistoryDaoImpl();
    }
     
    public static SkillDaoInter instanceSkillDao(){
        return new SkillDaoImpl();
    }
}
