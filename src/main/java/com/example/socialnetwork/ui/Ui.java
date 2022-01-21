package com.example.socialnetwork.ui;

import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.result.Result;
import com.example.socialnetwork.result.ResultError;
import com.example.socialnetwork.result.ResultSuccess;
import com.example.socialnetwork.service.FriendshipRequestService;
import com.example.socialnetwork.service.FriendshipService;
import com.example.socialnetwork.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("unchecked")
public class Ui {

    private final UserService userService;
    private final FriendshipService friendshipService;
    private final FriendshipRequestService friendshipRequestService;

    public Ui(UserService userService, FriendshipService friendshipService, FriendshipRequestService friendshipRequestService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.friendshipRequestService = friendshipRequestService;
    }

    public void showMenu() {
        do {
            System.out.println("----------------------------------------------------------------");
            System.out.println("1. Show users");
            System.out.println("2. Show friendships");
            System.out.println("3. Add User");
            System.out.println("4. Add Friendship");
            System.out.println("5. Delete user");
            System.out.println("6. Delete friendship");
            System.out.println("7. Update user");
            System.out.println("8. Show number of networks");
            System.out.println("9. Show networks number");
            System.out.println("10. send request");
            System.out.println("11. Update request status");
            System.out.println("----------------------------------------------------------------");
            System.out.print("Choose an option: ");
            Scanner s = new Scanner(System.in);
            String option = s.nextLine();

            switch (option) {
                case "1":
                    showUsers();
                    break;
                case "2":
                    showFriendships();
                    break;
                case "3":
                    addUser();
                    break;
                case "4":
                    addFriendship();
                    break;
                case "5":
                    deleteUser();
                    break;
                case "6":
                    deleteFriendship();
                    break;
                case "7":
                    updateUser();
                    break;
                case "8":
                    showNetworksNumber();
                    break;
                case "9":
                    showBiggestNetwork();
                    break;
                case "10":
                    sendRequest();
                    break;
                case "11":
                    updateRequestStatus();
                    break;
                case "12":
                    getFriendshipsForUser();
                    break;
                default:
                    return;
            }

        } while (true);
    }

    private void showUsers() {
        Result result = userService.getAll();
        if (result.isSuccess()) {
            Iterable<User> users = ((ResultSuccess<Iterable<User>>) result).getData();
            for (User user : users) {
                System.out.println(user);
            }
        } else {
            System.out.println(((ResultError) result).getError());
        }
    }

    private void showFriendships() {
        Result result = friendshipService.getAll();
        if (result.isSuccess()) {
            Iterable<Friendship> friendships = ((ResultSuccess<Iterable<Friendship>>) result).getData();
            for (Friendship friendship : friendships) {
                System.out.println(friendship);
            }
        }
    }

    private void addUser() {
        try {
            System.out.println("Add user");
            Scanner s = new Scanner(System.in);
            System.out.print("First name: ");
            String firstName = s.nextLine();
            System.out.print("Last name: ");
            String lastName = s.nextLine();
            System.out.print("Email: ");
            String email = s.nextLine();
            System.out.print("Password: ");
            String password = s.nextLine();
            Result add = userService.add(firstName, lastName, email, password);
            if (add.isSuccess()) {
                System.out.println("Successful added!");
            } else {
                System.out.println(((ResultError) add).getError());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addFriendship() {
        try {
            System.out.println("Ad friendship");
            Scanner s = new Scanner(System.in);
            System.out.print("From (user id): ");
            Long idUser1 = Long.valueOf(s.nextLine());
            System.out.print("To (user id): ");
            Long idUser2 = Long.valueOf(s.nextLine());
            Result add = friendshipService.add(idUser1, idUser2, LocalDateTime.now());
            if (add.isSuccess()) {
                System.out.println("Successful added!");
            } else {
                System.out.println(((ResultError) add).getError());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteUser() {
        try {
            System.out.println("Delete user");
            Scanner s = new Scanner(System.in);
            System.out.print("User id: ");
            Long id = Long.valueOf(s.nextLine());
            // Delete all friendships for user first
            Result deleteFriendshipsForUser = friendshipService.deleteAllForUser(id);
            // Then delete user
            Result delete = userService.delete(id);
            if (deleteFriendshipsForUser.isSuccess() && delete.isSuccess()) {
                System.out.println("Successful deleted!");
            } else {
                if (!delete.isSuccess()){
                    System.out.println(((ResultError) delete).getError());
                }
                if (!deleteFriendshipsForUser.isSuccess()) {
                    System.out.println(((ResultError) deleteFriendshipsForUser).getError());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteFriendship() {
        try {
            System.out.println("Delete friendship");
            Scanner s = new Scanner(System.in);
            System.out.print("Friendship id: ");
            Long id = Long.valueOf(s.nextLine());
            Result delete = friendshipService.delete(id);
            if (delete.isSuccess()) {
                System.out.println("Successful deleted!");
            } else {
                System.out.println(((ResultError) delete).getError());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateUser() {
        try {
            System.out.println("Update user");
            Scanner s = new Scanner(System.in);
            System.out.print("User id: ");
            Long id = Long.valueOf(s.nextLine());
            System.out.print("New first name: ");
            String firstName = s.nextLine();
            Result update = userService.updateFirstName(id, firstName);
            if (update.isSuccess()) {
                System.out.println("First name updated");
            } else {
                System.out.println(((ResultError) update).getError());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void showNetworksNumber() {
        try {
            System.out.println("Number of networks: " + friendshipService.getNetworksNumber());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void showBiggestNetwork() {
        try {
            System.out.println("The biggest network:");
            Result result = friendshipService.getBiggestNetwork();
            if (result.isSuccess()) {
                ArrayList<User> users = ((ResultSuccess<ArrayList<User>>) result).getData();
                for (User user : users) {
                    System.out.println(user);
                }
            } else {
                System.out.println(((ResultError) result).getError());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendRequest() {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("From: ");
            Long from = s.nextLong();
            System.out.println("To: ");
            Long to = s.nextLong();
            Result result = friendshipRequestService.sendRequest(from, to);
            if (result.isSuccess()) {
                System.out.println("ok");
            } else {
                System.out.println(((ResultError) result).getError());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateRequestStatus() {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("id: ");
            Long id = Long.valueOf(s.nextLine());
            System.out.println("status: ");
            String status = s.nextLine();
            Result result = friendshipRequestService.updateStatus(id, status);
            if (result.isSuccess()) {
                System.out.println("Ok");
            } else {
                System.out.println(((ResultError) result).getError());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void getFriendshipsForUser() {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("id: ");
            Long id = Long.valueOf(s.nextLine());
            Result result = friendshipService.getAllForUser(id);
            if (result.isSuccess()) {
                List<Friendship> friendships = ((ResultSuccess<List<Friendship>>) result).getData();
                for (Friendship friendship : friendships) {
                    System.out.println(friendship);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
