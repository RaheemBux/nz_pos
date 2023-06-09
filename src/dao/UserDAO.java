/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author HP
 */
import java.util.List;
import model.User;

public interface UserDAO {

    // Create
    boolean addUser(User user);

    // Read
    User getUserById(int userId);

    List<User> getAllUsers();

    // Update
    boolean updateUser(User user);

    // Delete
    boolean deleteUser(int userId);

    User login(String email, String password);
}
