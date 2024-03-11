package dao;

import Model.NewUser;
import Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

public class UserDao {


    private String jdbcURL = "jdbc:mysql://localhost:3306/project_jsf";
    private String jdbcUsername = "root";
    private String jdbcPasswd = "";



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
////////////////::modified the user to new User

    public boolean insertObject(User user) throws SQLException {
        Connection conx = getConnection();
        boolean find = true;

        // Assurer que l'email n'existe pas déjà
        PreparedStatement ps = conx.prepareStatement(select_all);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            if (user.getEmail().equals(rs.getString("email"))) {
                find = false;
                break;
            }
        }

        // Vérifier le format de l'email
        if (find) {
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(user.getEmail());

            if (!matcher.matches()) {
                // Email invalide, afficher un message d'erreur
                throw new IllegalArgumentException("Invalid email format");
            }

            try {
                PreparedStatement psins = conx.prepareStatement(Insert_object);
                psins.setString(1, user.getNom());
                psins.setString(2, user.getPrenom());
                psins.setString(3, user.getEmail());
                psins.setInt(4, user.getAge());
                psins.executeUpdate();
                return true;
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de l'insertion de l'utilisateur", e);
            }
        } else {
            // L'email existe déjà, afficher un message d'erreur
            throw new IllegalArgumentException("Email already exists");
        }
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
    //update object
    public boolean updateUser(User user) throws SQLException{
        boolean upd;
        Connection conx=getConnection();
        boolean find = true;
        // Assurer que l'email n'est pas vide
        if (user.getEmail().isEmpty()) {
            FacesMessage errorMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email cannot be empty", null);
            FacesContext.getCurrentInstance().addMessage(null, errorMessage);
            return false;
        }

        // Assurer que l'email a un format valide
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(user.getEmail());

        if (!matcher.matches()) {
            // Email invalide, afficher un message d'erreur
            FacesMessage errorMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email format", null);
            FacesContext.getCurrentInstance().addMessage(null, errorMessage);
            return false;
        }

        // Vérifier si l'email existe déjà
        PreparedStatement ps = conx.prepareStatement(select_all);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            if (user.getEmail().equals(rs.getString("email")) && user.getId() != rs.getInt("id")) {
                // Email déjà utilisé par un autre utilisateur, afficher un message d'erreur
                FacesMessage errorMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email already exists", null);
                FacesContext.getCurrentInstance().addMessage(null, errorMessage);
                return false;
            }
        }

        try {
            // Effectuer la mise à jour de l'utilisateur si tout est valide
            ps=conx.prepareStatement(update_object);
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


    //pagination
    public int getTotalUserCount() {
        String query = "SELECT COUNT(*) FROM user";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<User> getUsersByPage(int pageNumber, int pageSize) {
        // Calculate offset based on page number and page size
        int offset = (pageNumber - 1) * pageSize;
        // Use SQL query with LIMIT and OFFSET to fetch the subset of users
        String query = "SELECT * FROM user LIMIT ? OFFSET ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pageSize);
            statement.setInt(2, offset);
            ResultSet resultSet = statement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                // Populate User objects from ResultSet
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("email"),
                        resultSet.getInt("age")
                );
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions
            return null;
        }
    }


}
