package com.example.socialnetwork.ui;

import com.example.socialnetwork.domain.FriendshipRequest;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.result.Result;
import com.example.socialnetwork.result.ResultError;
import com.example.socialnetwork.result.ResultSuccess;
import com.example.socialnetwork.service.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@SuppressWarnings("unchecked")
public class NewUi {

    private final UserService userService;
    private final FriendshipService friendshipService;
    private final FriendshipRequestService friendshipRequestService;
    private final MessageService messageService;
    private final Chat chat;
    private User currentUser;

    public NewUi(UserService userService, FriendshipService friendshipService,
                 FriendshipRequestService friendshipRequestService, MessageService messageService, Chat chat) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.friendshipRequestService = friendshipRequestService;
        this.messageService = messageService;
        this.chat = chat;
    }

    public void showMenu() {
        while (true) {
            System.out.println("--------------------------------------------");
            System.out.println("1. Log in");
            System.out.println("2. Sign up");
            System.out.println("3. Show users");
            System.out.println("4. Show friendships for user");
            System.out.println("5. Show friendships for user from month");
            System.out.println("--------------------------------------------");

            Scanner s = new Scanner(System.in);
            System.out.println("Alege o optiune: ");
            String option = s.nextLine();

            switch (option) {
                case "1":
                    logIn();
                    break;
                case "2":
                    signUp();
                    break;
                case "3":
                    showUsers();
                    break;
                case "4":
                    showFriendshipsForUser();
                    break;
                case "5":
                    showFriendshipsForUserFromMonth();
                    break;
                default:
                    return;
            }
        }

    }


    private void showLoggedInUserMenu() {
        while (true) {
            System.out.println("--------------------------------------------");
            System.out.println("1. Show friend requests");
            System.out.println("2. Send friend request");
            System.out.println("3. Show messages");
            System.out.println("4. Send message");
            System.out.println("--------------------------------------------");

            Scanner s = new Scanner(System.in);
            System.out.println("Alege o optiune: ");
            String option = s.nextLine();

            switch (option) {
                case "1":
                    //showFriendRequests();
                    break;
                case "2":
                    sendFriendRequest();
                    break;
                case "3":
                    //showMessagesFromTwo();
                    break;
                case "4":
                    sendMessage();
                    break;
                default:
                    return;
            }
        }
    }
//
//    private void showMessagesFromTwo() {
//        try {
//            Scanner s = new Scanner(System.in);
//            System.out.println("firstUserId: ");
//            Long firstUserId = Long.valueOf(s.nextLine());
//            System.out.println("secondUserId: ");
//            Long seconfUserId = Long.valueOf(s.nextLine());
//            Result result = friendshipService.getAllForUser(id);
//            showAllFriends(result);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }

    private void logIn() {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("Email: ");
            String email = s.nextLine();
            System.out.println("Password: ");
            String password = s.nextLine();
            Result loginResult = userService.get(email, password);
            if (loginResult.isSuccess()) {
                this.currentUser = ((ResultSuccess<User>) loginResult).getData();
                System.out.println("Hello, " + this.currentUser.getFirstName());
                showLoggedInUserMenu();
            } else {
                System.out.println("Incorrect username or password!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void signUp() {
        try {
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
                System.out.println("Successful registered!");
            } else {
                System.out.println(((ResultError) add).getError());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

    private void showFriendshipsForUser() {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("id: ");
            Long id = Long.valueOf(s.nextLine());
            Result result = friendshipService.getAllForUser(id);
            showAllFriends(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void showFriendshipsForUserFromMonth() {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("id: ");
            Long id = Long.valueOf(s.nextLine());
            System.out.println("friends from month: ");
            Integer month = Integer.valueOf(s.nextLine());
            Result result = friendshipService.getAllForUserFromMonth(id, month);
            showAllFriends(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void showAllFriends(Result result) {
        if (result.isSuccess()) {
            List<List<String>> friendships = ((ResultSuccess<List<List<String>>>) result).getData();
            for (List<String> friendship : friendships) {
                System.out.println(friendship.get(0) + " | " + friendship.get(1) + " | " + friendship.get(2));
            }
        }
    }

    private void sendFriendRequest() {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("To: ");
            Long to = s.nextLong();
            Result result = friendshipRequestService.sendRequest(currentUser.getId(), to);
            if (result.isSuccess()) {
                System.out.println("ok");
            } else {
                System.out.println(((ResultError) result).getError());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendMessage() {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("To: ");
            Long to = Long.valueOf(s.nextLine());
            System.out.println("Text: ");
            String messageText = s.nextLine();
            System.out.println("Is reply? (y/n): ");
            String isReply = s.nextLine();
            while (!Objects.equals(isReply, "y") && !Objects.equals(isReply, "n")) {
                System.out.println("Is reply? (y/n): ");
                isReply = s.nextLine();
            }
            if (Objects.equals(isReply, "y")) {
                addMessage(messageText, currentUser.getId(), LocalDateTime.now(), to.intValue());
            } else {
                addMessage(messageText, currentUser.getId(), LocalDateTime.now(), 0);

            }
            Result result = chat.sendMessage(to, messageService.getLastMessageId());
            if (result.isSuccess()) {
                System.out.println("ok");
            } else {
                System.out.println(((ResultError) result).getError());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void addMessage(String messageText, Long from, LocalDateTime date, int reply) {
        Result resultMessage = null;
        try {
            resultMessage = messageService.add(messageText, from, date, reply);
            if (resultMessage.isSuccess()) {
                System.out.println("ok");
            } else {
                System.out.println(((ResultError) resultMessage).getError());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    private void showFriendRequests() {
        try {
            Result result = friendshipRequestService.getRequestsForUser(currentUser.getId());
            if (result.isSuccess()) {
                HashMap<Long, User> requests = ((ResultSuccess<HashMap<Long, User>>) result).getData();
                for (Long requestId : requests.keySet()) {
                    System.out.println("id: " + requestId + " New friend request from: "
                            + requests.get(requestId).getFirstName() + " " + requests.get(requestId).getLastName());
                }
                // answer friend requests
                if (!requests.isEmpty()) {
                    System.out.println("Confirm requests? Y/N");
                    Scanner s = new Scanner(System.in);
                    String response = s.nextLine();
                    if (response.equals("y") || response.equals("Y")) {
                        answerFriendRequest(requests);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }*/

    private void answerFriendRequest(Map<Long, User> requests) {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("id: ");
            Long id = Long.valueOf(s.nextLine());
            System.out.println("Accept Request? Y/N");
            String response = s.nextLine();
            String status;
            if (response.equals("y") || response.equals("Y")) {
                status = FriendshipRequest.APPROVED;
            } else if (response.equals("n") || response.equals("N")) {
                status = FriendshipRequest.REJECTED;
            } else {
                status = FriendshipRequest.PENDING;
            }
            if (requests.get(id) != null) {
                Result result = friendshipRequestService.updateStatus(id, status);
                if (result.isSuccess()) {
                    if (status.equals(FriendshipRequest.APPROVED)) {
                        Result addFriendshipResult = friendshipService.add(requests.get(id).getId(), currentUser.getId(), LocalDateTime.now());
                        if (addFriendshipResult.isSuccess()) {
                            System.out.println("Ok");
                        } else {
                            System.out.println(((ResultError) addFriendshipResult).getError());
                        }
                    }
                } else {
                    System.out.println(((ResultError) result).getError());
                }
            } else {
                System.out.println("Not in requests list!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
