package Model;

import dao.UserDao;
import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.RequestScoped;

import java.sql.SQLException;
import java.util.List;
@ManagedBean
@RequestScoped

public class User {

    private boolean editMode = false;
    private int id;
    private String nom;
    private String prenom;

    private String email;
    private int age;
    private User selectedUser;

    private UserDao userDao=new UserDao();


    public User(){

    }

    public User(int id, String nom, String prenom, String email, int age) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.age = age;
    }

    public User(String nom, String prenom, String email, int age) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<User> getUsersList(){
        return userDao.selectallusers();
    }
    public void deleteuser(int id) throws SQLException {
        userDao.deleteUser(id);



    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void edituser(int id) throws SQLException {
        selectedUser=userDao.selectUserbyid(id);
//        this.editMode = true;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void saveChanges() throws SQLException {
        userDao.updateUser(selectedUser);
//        this.editMode = false;
    }

    public void addUserForm() {
        // Afficher le formulaire d'ajout d'un nouvel utilisateur
    }
    public String insertUser(User user) throws SQLException {
        userDao.insertObject(user);
        return "table.xhtml";




    }



}
