package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.FriendshipRequest;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.repository.base.Repository;
import com.example.socialnetwork.result.Result;
import com.example.socialnetwork.result.ResultSuccess;
import com.example.socialnetwork.service.base.Service;

import java.io.IOException;
import java.util.*;

@SuppressWarnings("unchecked")
public class FriendshipRequestService extends Service<FriendshipRequest> {

    private final Repository<User> userRepository;

    public FriendshipRequestService(Repository<FriendshipRequest> repository, Repository<User> userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    public Result get(Long from, Long to) {
        Result resultList = repository.getAll();
        if (resultList.isSuccess()) {
            List<FriendshipRequest> friendshipRequests = ((ResultSuccess<List<FriendshipRequest>>) resultList).getData();
            for (FriendshipRequest friendshipRequest : friendshipRequests) {
                if (Objects.equals(friendshipRequest.getFromUserId(), from) && Objects.equals(friendshipRequest.getToUserId(), to)) {
                    return new ResultSuccess<>(friendshipRequest);
                }
            }
        }
        return null;
    }

    /**
     * Send friend request
     * @param fromUserId - the id of user who sends request
     * @param toUserId - the id of user who receives request
     * @return Result status
     * @throws IOException if com.example.demo.repository is FileRepository and fails saving data to file
     */
    public Result sendRequest(Long fromUserId, Long toUserId) throws IOException {
        FriendshipRequest lastEntry = repository.getLastEntry();
        Long lastId;
        if (lastEntry != null) {
            lastId = lastEntry.getId();
        } else {
            lastId = 0L;
        }
        FriendshipRequest friendshipRequest = new FriendshipRequest(lastId + 1, fromUserId, toUserId);
        return repository.add(friendshipRequest);
    }

    /**
     * Update Friendhip request status
     * @param id - The id of friendship request to be updated
     * @param status - new status
     * @return Result status
     * @throws IOException if com.example.demo.repository is FileRepository and fails saving data to file
     */
    public Result updateStatus(Long id, String status) throws IOException {
        Result result = repository.get(id);
        if (result.isSuccess()) {
            FriendshipRequest friendshipRequest = ((ResultSuccess<FriendshipRequest>) result).getData();
            // update status
            friendshipRequest.setStatus(status);
            return repository.update(friendshipRequest);
        }
        return result;
    }

    /**
     * Get requests sent to a specified user
     * @param id the id of user
     * @return Result status
     */
    public Result getRequestsForUser(Long id) {
        Result result = repository.getAll();
        if (result.isSuccess()) {
            Iterable<FriendshipRequest> friendshipRequests = ((ResultSuccess<Iterable<FriendshipRequest>>) result).getData();
            // Map<Long, User> requests = new HashMap<>();
            List<User> users = new ArrayList<>();
            for (FriendshipRequest friendshipRequest : friendshipRequests) {
                if (friendshipRequest.getStatus().equals(FriendshipRequest.PENDING) && Objects.equals(friendshipRequest.getToUserId(), id)) {
                    Result userResult = userRepository.get(friendshipRequest.getFromUserId());
                    if (result.isSuccess()) {
                        User user = ((ResultSuccess<User>) userResult).getData();
                        users.add(user);
                    }
                }
            }
            return new ResultSuccess<>(users);
        }
        return result;
    }

    public Result getUserSentRequests(Long id) {
        Result result = repository.getAll();
        if (result.isSuccess()) {
            Iterable<FriendshipRequest> friendshipRequests = ((ResultSuccess<Iterable<FriendshipRequest>>) result).getData();
            List<User> users = new ArrayList<>();
            for (FriendshipRequest friendshipRequest : friendshipRequests) {
                if (friendshipRequest.getStatus().equals(FriendshipRequest.PENDING) && Objects.equals(friendshipRequest.getFromUserId(), id)) {
                    Result userResult = userRepository.get(friendshipRequest.getToUserId());
                    if (result.isSuccess()) {
                        User user = ((ResultSuccess<User>) userResult).getData();
                        users.add(user);
                    }
                }
            }
            return new ResultSuccess<>(users);
        }
        return result;
    }
}
