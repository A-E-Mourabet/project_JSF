package Beans;

import Model.User;
import dao.UserDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserBean {

    private static List<User> usersList;
    private static List<User> addList = new ArrayList<>() ;
    private static List<User> deleteList = new ArrayList<>();
    private static List<User> updateList = new ArrayList<>();
    private User editedUser = new User();
    private boolean editMode = false;
    private boolean newUserForm = false;
    private static int editedUserId = 0;

    private UserDao userDao=new UserDao();


    //for pagination
    private int currentPage = 1;
    private int pageSize = 3;
    private int totalPageCount;




    public UserBean() {
        refreshUsersList();
        updateTotalPageCount();
    }


    public List<User> getUsersList() {
        return usersList;
    }

    public List<User> getAddList() {
        return addList;
    }

    public List<User> getDeleteList() {
        return deleteList;
    }

    public List<User> getUpdateList() {
        return updateList;
    }

    public User getEditedUser() {
        return editedUser;
    }

    public void setEditedUser(User editedUser) {
        this.editedUser = editedUser;
    }

    public boolean isSelectedId(int userId) {
        return Objects.equals(editedUser.getId(), userId);
    }

    public boolean isEditMode() {
        return editMode;
    }
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isNewUserForm() {
        return newUserForm;
    }
    public void setNewUserForm(boolean newUserForm) {
        this.newUserForm = newUserForm;
    }
    public int getEditedUserId() {
        return editedUserId;
    }

    public void setEditedUserId(int editedUserId) {
        this.editedUserId = editedUserId;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
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

    //methodes
    public void deleteUser(int id) {
        User userToDelete = getUserById(id);
        if (userToDelete != null) {
            int indexToDelete = getUserIndexById(id);
            if (indexToDelete != -1) {
                deleteList.add(userToDelete);
                addList.removeIf(user -> Objects.equals(user.getId(), id));
                updateList.removeIf(user -> Objects.equals(user.getId(), id));
                usersList.remove(indexToDelete);
                updateTotalPageCount();
            }
        }
    }
    public void insertUser() {
        usersList.add(editedUser);
        addList.add(editedUser);
        setNewUserForm(false);
        updateTotalPageCount();
    }

    public void editUser(int id) {
        if (editMode) {
            // Save changes to the in-memory list
            int index = getUserIndexById(id);
            if (index != -1) {
                User UserAdded= new User(editedUser.getId(), editedUser.getNom(),editedUser.getPrenom(), editedUser.getEmail(), editedUser.getAge());
                updateList.add(UserAdded);
                usersList.set(index,UserAdded);
            }
            setEditMode(false);
        } else {
            setEditedUser(getUserById(id));
            setEditMode(true);
        }
    }

    public void saveChanges() {
        try {
            // Apply insert actions
            for (User newUser : addList) {
                if (newUser != null) {
                    userDao.insertObject(newUser);
                }
            }

            // Apply update actions
            for (User updatedUser : updateList) {
                if (updatedUser != null) {
                    userDao.updateUser(updatedUser);
                }
            }

            // Apply delete actions

            for (User deletedUser : deleteList) {
                if (deletedUser != null) {
                    userDao.deleteUser(deletedUser.getId());
                }
            }
            // Clear the lists after changes are applied
            addList.clear();
            deleteList.clear();
            updateList.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public String userForm() {
        if (newUserForm) {
            setNewUserForm(false);
        } else {
            setNewUserForm(true);
            setEditedUser(new User());
        }
        return "table.xhtml";
    }


    private void refreshUsersList() {
        usersList = userDao.selectallusers();
        updateTotalPageCount();
    }

    private User getUserById(int id) {
        for (User user : usersList) {
            if (Objects.equals(user.getId(), id)) {
                return user;
            }
        }
        return null;
    }

    private int getUserIndexById(int id) {
        for (int i = 0; i < usersList.size(); i++) {
            if (Objects.equals(usersList.get(i).getId(), id)) {
                return i;
            }
        }
        return -1;
    }


    //pagination methodes utilities
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
