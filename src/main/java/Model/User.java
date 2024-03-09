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

    private static NewUser newUser = new NewUser();
    private static NewUser modifiedUser = new NewUser();
    private  static boolean editMode = false;
    private  static boolean NewUserForm = false;

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
    //setters and getters
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

    public NewUser getModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(NewUser modifiedUser) {
        User.modifiedUser = modifiedUser;
    }

    //methods...

    public static boolean isNewUserForm() {
        return NewUserForm;
    }

    public static void setNewUserForm(boolean newUserForm) {
        NewUserForm = newUserForm;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public  NewUser getNewUser() {
        return newUser;
    }

    public void setNewUser(NewUser newUser) {
        this.newUser = newUser;
    }

    //methods

    public void deleteuser(int id) throws SQLException {
        userDao.deleteUser(id);

    }

    public void edituser(int id) throws SQLException {
        //System.out.println("id : " + id );
        if(editMode){
            setEditMode(false);
            System.out.println(User.modifiedUser.getId());
            userDao.updateUser(new User(User.modifiedUser.getId() , User.modifiedUser.getNom() , User.modifiedUser.getPrenom() , User.modifiedUser.getEmail() , User.modifiedUser.getAge()));
            //User.setEditedId(0);
        }else{
            User.setEditedId(id);
            User.setEditMode(true);
        }
    }

    public boolean selectedId(){
        if(Objects.equals(this.getId(), User.getEditedId())) {
            modifiedUser.setId(this.getId());
            modifiedUser.setPrenom(this.getPrenom());
            modifiedUser.setNom(this.getNom());
            modifiedUser.setEmail(this.getEmail());
            modifiedUser.setAge(this.getAge());
        }
        return Objects.equals(this.getId(), User.getEditedId());
    }

    public boolean isEditMode() {
        return User.editMode;
    }

    public void saveChanges() throws SQLException {
        //userDao.updateUser(selectedUser);
        User.setEditMode(false);
        User.setEditedId(0);
    }

    public String UserForm() throws SQLException {
        if(User.NewUserForm) {
            User.setNewUserForm(false);
        }else{
            User.setNewUserForm(true);
        //userDao.insertObject(user);
        }
        return "table.xhtml";
    }

    public void insertUser()throws SQLException{
        userDao.insertObject(new User(newUser.getNom(), newUser.getPrenom(), newUser.getEmail(), newUser.getAge()));
        setNewUser(new NewUser());
        setNewUserForm(false);

    }



}
