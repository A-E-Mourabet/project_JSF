package Model;

import dao.UserDao;
import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.SessionScoped;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserBain implements Serializable {
    private User selectedUser;

    private  static boolean editMode = false;
    private  static int editedId = 0;

    private UserDao userDao=new UserDao();


    private int currentPage = 1;
    private int pageSize = 3;
    private int totalPageCount;






}
