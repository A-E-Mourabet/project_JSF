package dao;

import Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {


    private String jdbcURL = "jdbc:mysql://localhost:3306/commerce";
    private String jdbcUsername = "root";
    private String jdbcPasswd = "root";

    private static final String Insert_object = "insert into user (nom,prenom,email,age) values (?,?,?,?)";
    private static final String select_object_by_id = "select * from user where id=?";
    private static final String select_all = "select * from user";
    private static final String delete_object = "delete from user where id=?";
    private static final String update_object = "update user set nom=? ,prenom=?,age=?,email=? where id=?";

    protected Connection getConnection() {
        Connection conx = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conx= DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPasswd);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return conx;


    }



    //insert object

    public boolean insertObject(User user )throws SQLException{
        Connection conx = getConnection();
        boolean find = true;






        //assurer que email et password n'est pas existe
        PreparedStatement ps = conx.prepareStatement(select_all);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            if (user.getEmail().equals(rs.getString("email")) ) {
                find = false;
                break;
            }


        }



        if(find){



            PreparedStatement psins = conx.prepareStatement(Insert_object);
            psins.setString(1, user.getNom());
            psins.setString(2, user.getPrenom());
            psins.setString(3, user.getEmail());

            psins.setInt(4, user.getAge());
            psins.executeUpdate();
            return true;

        }
        return false;














    }
    //select object by id


    public User selectUserbyid(int id){
        User user=null;
        try{
            Connection conx=getConnection();
            PreparedStatement ps=conx.prepareStatement(select_object_by_id);
            ps.setInt(1,id);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String nom=rs.getString("nom");
                String prenom=rs.getString("prenom");
                String email=rs.getString("email");

                int age=rs.getInt("age");
                user=new User(nom,prenom,email,age);
            }





        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;


    }





    //select all objects


    public List<User> selectallusers(){

        List<User> users=new ArrayList<>();
        try{
            Connection conx =getConnection();
            PreparedStatement ps= conx.prepareStatement(select_all);
            ResultSet rs= ps.executeQuery();

            while (rs.next()){
                User user = new User();
                int id=rs.getInt("id");
                String nom=rs.getString("nom");
                String prenom=rs.getString("prenom");
                String email=rs.getString("email");

                int age=rs.getInt("age");
                user.setNom(nom);
                user.setPrenom(prenom);
                user.setEmail(email);
                user.setAge(age);
                user.setId(id);

                users.add(user);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;

    }







    //delete object

    public boolean deleteUser(int id) throws SQLException {

        boolean rowdeleted;
        try {
            Connection conx=getConnection();
            PreparedStatement ps= conx.prepareStatement(delete_object);
            ps.setInt(1,id);
            rowdeleted=ps.executeUpdate() >0 ;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rowdeleted;
    }



    //update object
    public boolean updateUser(User user) throws SQLException{
        boolean upd;
        try{
            Connection conx=getConnection();
            PreparedStatement ps=conx.prepareStatement(update_object);
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setInt(3, user.getAge());
            ps.setString(4, user.getEmail());

            ps.setInt(5, user.getId());
            upd=ps.executeUpdate() >0;

        } catch (Exception e)   {
            throw new RuntimeException(e);
        }
        return upd;

    }








}
