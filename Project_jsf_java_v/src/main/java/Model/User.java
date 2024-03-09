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


    private int currentPage = 1;
    private int pageSize = 3;
    private int totalPageCount;

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public User(){

        updateTotalPageCount();


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

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<User> getUsersList(){
        return userDao.getUsersByPage(currentPage, pageSize);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String deleteuser(int id) throws SQLException {

        userDao.deleteUser(id);
        updateTotalPageCount();
//        currentPage--;
        // Vérifie si la page actuelle est supérieure au nouveau nombre total de pages
//        if (currentPage > totalPageCount) {
//            // Réduire le numéro de la page actuelle pour rester sur la dernière page disponible
//            currentPage = totalPageCount;
//        }
        return "table.xhtml";



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

    public boolean selectedId(){

//        System.out.println("selected index : "+this.getId());
//        System.out.println("edited id : "+User.getEditedId());
        //System.out.println(Objects.equals(this.id, User.getEditedId()));
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

    public void addUserForm() {
        // Afficher le formulaire d'ajout d'un nouvel utilisateur
    }
    public String insertUser(User user) throws SQLException {

        userDao.insertObject(user);
        updateTotalPageCount();
        return "table.xhtml";

    }


    //pagination
    public void updateTotalPageCount() {
        // Calculate total page count based on total number of users and page size
        int totalUsers = userDao.getTotalUserCount();
//        totalPageCount = (int) Math.ceil((double) totalUsers / pageSize);
        setTotalPageCount((int) Math.ceil((double) totalUsers / pageSize));
    }
    public void nextPage() {
        if (currentPage < getTotalPageCount()) {
            currentPage++;
        }

    }

    public void previousPage() {

        if (currentPage > 1) {
            currentPage--;
        }
    }



}