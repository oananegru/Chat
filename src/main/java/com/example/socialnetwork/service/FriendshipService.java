package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.repository.base.Repository;
import com.example.socialnetwork.result.Result;
import com.example.socialnetwork.result.ResultError;
import com.example.socialnetwork.result.ResultSuccess;
import com.example.socialnetwork.service.base.Service;
import com.example.socialnetwork.utils.Graph;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class FriendshipService extends Service<Friendship> {

    private final Repository<User> userRepository;

    public FriendshipService(Repository<Friendship> repository, Repository<User> userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    /**
     * Search a friendship between 2 specific users
     * @param idUser1 - the id of user 1
     * @param idUser2 - the id of user 2
     * @return Result success with friendship if exists, com.example.demo.result error otherwise
     */
    public Result get(Long idUser1, Long idUser2) {
        Result result = repository.getAll();
        if (result.isSuccess()) {
            Iterable<Friendship> friendships = ((ResultSuccess<Iterable<Friendship>>) result).getData();
            Friendship friendship = search(idUser1, idUser2, friendships);
            if (friendship != null) {
                return new ResultSuccess<>(friendship);
            } else {
                return new ResultError("A related friendship does not exists!");
            }
        }
        return new ResultError("Something went wrong!");
    }

    /**
     * Add new friendship
     * @param idUser1 - the id of user 1
     * @param idUser2 - the id of user 2
     * @param dateTime - date
     * @return success status
     * @throws IOException if com.example.demo.repository is FileRepository and fails saving data to file
     */
    public Result add(Long idUser1, Long idUser2, LocalDateTime dateTime) throws IOException {
        String date = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
        Friendship lastEntry = repository.getLastEntry();
        Long lastId;
        if (lastEntry != null) {
            lastId = lastEntry.getId();
        } else {
            lastId = 0L;
        }
        Result cba = canBeAdded(idUser1, idUser2);
        if (cba.isSuccess()){
            Friendship friendship = new Friendship(lastId + 1, idUser1, idUser2, date);
            return repository.add(friendship);
        } else {
            return cba;
        }
    }

    /**
     * Delete friendship for specified users
     * @param idUser1 - id for user 1
     * @param idUser2 - id for user 2
     * @return success status
     * @throws IOException
     */
    public Result delete(Long idUser1, Long idUser2) throws IOException {
        Result foundFriendshipResult = get(idUser1, idUser2);
        if (foundFriendshipResult.isSuccess()) {
            Friendship friendship = ((ResultSuccess<Friendship>) foundFriendshipResult).getData();
            repository.delete(friendship.getId());
            return new ResultSuccess<>();
        } else {
            return foundFriendshipResult;
        }
    }

    /**
     * Returns all friendships for a specified user
     * @param userId - id of user
     * @return success status
     */
    public Result getAllForUser(Long userId) {
        if (userId == null) {
            return new ResultError("Id must be not null!");
        }
        Result result = repository.getAll();
        if (result.isSuccess()) {
            List<Friendship> friendships = (List<Friendship>) ((ResultSuccess<Iterable<Friendship>>) result).getData();
            // Stream operation - filter, map
            List<List<String>> userFriendships = friendships.stream()
                    .filter(s -> Objects.equals(userId, s.getIdUser1()) || Objects.equals(userId, s.getIdUser2()))
                    .map(x -> {
                        // Get friend as user
                        Long friendId;
                        if (Objects.equals(x.getIdUser1(), userId)) {
                            friendId = x.getIdUser2();
                        } else {
                            friendId = x.getIdUser1();
                        }
                        Result friendResult = userRepository.get(friendId);
                        // return com.example.demo.result
                        if (friendResult.isSuccess()) {
                            User friend = ((ResultSuccess<User>) friendResult).getData();
                            return List.of(friend.getFirstName(), friend.getLastName(), x.getDate());
                        }
                        return List.of("", "", x.getDate());
                    })
                    .toList();
            return new ResultSuccess<>(userFriendships);
        }
        return result;
    }

    public Result getFriendsForUser(Long id) {
        if (id == null) {
            return new ResultError("Id must be not null!");
        }
        Result result = repository.getAll();
        if (result.isSuccess()) {
            List<Friendship> friendships = (List<Friendship>) ((ResultSuccess<Iterable<Friendship>>) result).getData();
            // Stream operation - filter, map
            List<User> userFriendships = friendships.stream()
                    .filter(s -> Objects.equals(id, s.getIdUser1()) || Objects.equals(id, s.getIdUser2()))
                    .map(x -> {
                        // Get friend as user
                        Long friendId;
                        if (Objects.equals(x.getIdUser1(), id)) {
                            friendId = x.getIdUser2();
                        } else {
                            friendId = x.getIdUser1();
                        }
                        Result friendResult = userRepository.get(friendId);
                        // return com.example.demo.result
                        if (friendResult.isSuccess()) {
                            return ((ResultSuccess<User>) friendResult).getData();
                        }
                        return null;
                    })
                    .toList();
            return new ResultSuccess<>(userFriendships);
        }
        return result;
    }

    public Result getAllForUserFromMonth(Long userId, Integer month) {
        if (userId == null || month < 1 || month > 12) {
            return new ResultError("Id must be not null and month must be real!");
        }
        Result result = repository.getAll();
        if (result.isSuccess()) {
            List<Friendship> friendships = (List<Friendship>) ((ResultSuccess<Iterable<Friendship>>) result).getData();
            // Stream operation - filter, map
            List<List<String>> userFriendships = friendships.stream()
                    .filter(friendship -> ((Objects.equals(userId, friendship.getIdUser1()) ||
                            Objects.equals(userId, friendship.getIdUser2()))
                            && month.equals(Integer.valueOf(friendship.getDate().substring(5, 7)))))

                    .map(x -> {
                        // Get friend as user
                        Long friendId;
                        if (Objects.equals(x.getIdUser1(), userId)) {
                            friendId = x.getIdUser2();
                        } else {
                            friendId = x.getIdUser1();
                        }
                        Result friendResult = userRepository.get(friendId);
                        // return result
                        if (friendResult.isSuccess()) {
                            User friend = ((ResultSuccess<User>) friendResult).getData();
                            return List.of(friend.getFirstName(), friend.getLastName(), x.getDate());
                        }
                        return List.of("", "", x.getDate());
                    })
                    .toList();
            return new ResultSuccess<>(userFriendships);
        }
        return result;
    }

    /**
     * Deletes all friendships that contain a specified user id
     * @param userId - the id of user
     * @return success status
     * @throws IOException
     */
    public Result deleteAllForUser(Long userId) throws IOException {
        Result result = repository.getAll();
        Result deleteResult = null;
        if (result.isSuccess()) {
            Iterable<Friendship> friendships = ((ResultSuccess<Iterable<Friendship>>) result).getData();
            for (Friendship friendship : friendships) {
                if (Objects.equals(userId, friendship.getIdUser1()) || Objects.equals(userId, friendship.getIdUser2())) {
                    deleteResult = repository.delete(friendship.getId());
                }
            }
            return deleteResult;
        } else {
            return result;
        }
    }

    /**
     *
     * @return number of networks
     */
    public int getNetworksNumber() {
        Graph<Long> networkGraph = getNetworkGraph();
        networkGraph.dfs();
        return networkGraph.componentsNumber();
    }

    /**
     *
     * @return the biggest network
     */
    public Result getBiggestNetwork() {
        // Get network graph
        Graph<Long> networkGraph = getNetworkGraph();
        networkGraph.dfs();

        // Get the biggest component
        List<List<Long>> components = networkGraph.getComponents();
        List<Long> biggestComponent = new ArrayList<>();
        for (List<Long> component : components) {
            if (component.size() > biggestComponent.size()) {
                biggestComponent = component;
            }
        }

        // Create network's users list
        ArrayList<User> network = new ArrayList<>();
        for (Long id : biggestComponent) {
            Result result = userRepository.get(id);
            if (result.isSuccess()) {
                network.add(((ResultSuccess<User>) result).getData());
            } else {
                return result;
            }
        }

        return new ResultSuccess<>(network);
    }

    /**
     * Chek if users exists and if other friendship between same users already exists
     * @param idUser1 - the id of user 1
     * @param idUser2 - the id of user 2
     * @return Result success if friendship can be added, Result error otherwise
     */
    private Result canBeAdded(Long idUser1, Long idUser2) {
        if (!userRepository.get(idUser1).isSuccess()) {
            return new ResultError("A user with id " + idUser1 + " does not exist!");
        }
        if (!userRepository.get(idUser2).isSuccess()) {
            return new ResultError("A user with id " + idUser2 + " does not exist!");
        }
        if (get(idUser1, idUser2).isSuccess()) {
            return new ResultError("Friendship already exists!");
        }
        return new ResultSuccess<>();
    }

    /**
     * Search friendship in list
     * @param idUser1 - the id of user 1
     * @param idUser2 - the id of user 2
     * @param friendships - friendships list
     * @return searched friendship if exists, null otherwise
     */
    private Friendship search(Long idUser1, Long idUser2, Iterable<Friendship> friendships) {
        for (Friendship friendship : friendships) {
            if ((Objects.equals(friendship.getIdUser1(), idUser1) && Objects.equals(friendship.getIdUser2(), idUser2)) ||
                (Objects.equals(friendship.getIdUser1(), idUser2) && Objects.equals(friendship.getIdUser2(), idUser1))) {
                return friendship;
            }
        }
        return null;
    }

    /**
     *
     * @return network's graph
     */
    private Graph<Long> getNetworkGraph() {
        Graph<Long> networkGraph = new Graph<>();
        // Get users list
        Result getUsers = userRepository.getAll();
        if (getUsers.isSuccess()) {
            // Users list
            Iterable<User> users = ((ResultSuccess<Iterable<User>>) getUsers).getData();
            // Add nodes to graph
            for (User user : users) {
                networkGraph.addNode(user.getId());
            }
            // Get friendships list
            Result getFriendships = repository.getAll();
            if (getFriendships.isSuccess()) {
                // Friendships list
                Iterable<Friendship> friendships = ((ResultSuccess<Iterable<Friendship>>) getFriendships).getData();
                // Ad edges to graph
                for (Friendship friendship : friendships) {
                    networkGraph.addEdge(friendship.getIdUser1(), friendship.getIdUser2());
                }
            }
        }
        return networkGraph;
    }
}
