package Model;

import dao.UserDao;
import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.RequestScoped;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@ManagedBean
@RequestScoped

public class User {

    private int id;
    private String nom;
    private String prenom;

    private String email;
    private int age;
    private User selectedUser;

    private  static boolean editMode = false;
    private  static int editedId = 0;

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

    public static void setEditMode(boolean editMode) {
        User.editMode = editMode;
    }

    public static int getEditedId() {
        return editedId;
    }

    public static void setEditedId(int editedId) {
        User.editedId = editedId;
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

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
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
        System.out.println("id : " + id );
        User.setEditedId(id);
        User.setEditMode(true);
        selectedUser=userDao.selectUserbyid(id);
    }

    public boolean selectedId(User userA){

        System.out.println("selected id : "+userA.getId());
        System.out.println("edited id : "+User.getEditedId());
        //System.out.println(Objects.equals(this.id, User.getEditedId()));
        return Objects.equals(userA.getId(), User.getEditedId());
    }

    public boolean isEditMode() {
        return User.editMode;
    }

    public void saveChanges() throws SQLException {
        userDao.updateUser(selectedUser);
        User.setEditMode(false);
    }

    public void addUserForm() {
        // Afficher le formulaire d'ajout d'un nouvel utilisateur
    }
    public String insertUser(User user) throws SQLException {
        userDao.insertObject(user);
        return "table.xhtml";

    }



}
