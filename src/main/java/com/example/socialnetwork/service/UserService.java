package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.repository.base.Repository;
import com.example.socialnetwork.result.Result;
import com.example.socialnetwork.result.ResultError;
import com.example.socialnetwork.result.ResultSuccess;
import com.example.socialnetwork.service.base.Service;

import java.io.IOException;

@SuppressWarnings("unchecked")
public class UserService extends Service<User> {

    public UserService(Repository<User> repository) {
        super(repository);
    }

    public Result get(String email, String password) {
        Result result = repository.getAll();
        if (result.isSuccess()) {
            Iterable<User> userListResul = ((ResultSuccess<Iterable<User>>) result).getData();
            for (User user : userListResul) {
                if (email.equals(user.getEmail()) && password.equals(user.getPassword())) {
                    return new ResultSuccess<>(user);
                }
            }
            return new ResultError("Not found!");
        } else {
            return result;
        }
    }

    /**
     * Add new user
     * @param firstName - user's first name
     * @param lastName - user's last name
     * @param email - user's email
     * @param password - user's password
     * @return success status
     * @throws IOException if com.example.demo.repository is FileRepository and fails saving data to file
     */
    public Result add(String firstName, String lastName, String email, String password) throws IOException {
        User lastEntry = repository.getLastEntry();
        Long lastId;
        if (lastEntry != null) {
            lastId = lastEntry.getId();
        } else {
            lastId = 0L;
        }
        User user = new User(lastId + 1, firstName, lastName, email, password);
        return repository.add(user);
    }

    /**
     * Update user's first name
     * @param id - the id of user to be updated
     * @param firstName - new first name
     * @return success status
     * @throws IOException if com.example.demo.repository is FileRepository and fails saving data to file
     */
    public Result updateFirstName(Long id, String firstName) throws IOException {
        // Get user
        Result result = repository.get(id);
        // If com.example.demo.result is success (user exists)
        if (result.isSuccess()) {
            User user = ((ResultSuccess<User>) result).getData();
            // Update first name
            user.setFirstName(firstName);
            return repository.update(user);
        }
        return result;
    }

    /**
     * Update user's last name
     * @param id - the id of user to be updated
     * @param lastName - new last name
     * @return success status
     * @throws IOException if com.example.demo.repository is FileRepository and fails saving data to file
     */
    public Result updateLastName(Long id, String lastName) throws IOException {
        // Get user
        Result result = repository.get(id);
        // If com.example.demo.result is success (user exists)
        if (result.isSuccess()) {
            User user = ((ResultSuccess<User>) result).getData();
            // Update last name
            user.setLastName(lastName);
            return repository.update(user);
        }
        return result;
    }

    /**
     * Update user's email
     * @param id - the id of user to be updated
     * @param email - new email
     * @return success status
     * @throws IOException if com.example.demo.repository is FileRepository and fails saving data to file
     */
    public Result updateEmail(Long id, String email) throws IOException {
        // Get user
        Result result = repository.get(id);
        // If com.example.demo.result is success (user exists)
        if (result.isSuccess()) {
            User user = ((ResultSuccess<User>) result).getData();
            // Update email
            user.setEmail(email);
            return repository.update(user);
        }
        return result;
    }

    /**
     * Update user's password
     * @param id - the id of user to be updated
     * @param password - new password
     * @return success status
     * @throws IOException if com.example.demo.repository is FileRepository and fails saving data to file
     */
    public Result updatePassword(Long id, String password) throws IOException {
        // Get user
        Result result = repository.get(id);
        // If com.example.demo.result is success (user exists)
        if (result.isSuccess()) {
            User user = ((ResultSuccess<User>) result).getData();
            // Update password
            user.setPassword(password);
            return repository.update(user);
        }
        return result;
    }
}
