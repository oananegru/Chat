package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Message;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.UserMessages;
import com.example.socialnetwork.repository.base.Repository;
import com.example.socialnetwork.result.Result;
import com.example.socialnetwork.result.ResultSuccess;
import com.example.socialnetwork.service.base.Service;

import java.io.IOException;
import java.util.*;

@SuppressWarnings("unchecked")
public class Chat extends Service<UserMessages> {


    Repository<User> userRepository;
    Repository<Message> messageRepository;

    public Chat(Repository<UserMessages> repository, Repository<User> userRepository, Repository<Message> messageRepository) {
        super(repository);
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    /**
     * Send message
     * @param userId - the id of user who sends message
     * @param messageId - the id of message to send
     * @return Result status
     * @throws IOException if repository is FileRepository and fails saving data to file
     */
    public Result sendMessage(Long userId, Long messageId) throws IOException {
        UserMessages lastEntry = repository.getLastEntry();
        Long lastId;
        if (lastEntry != null) {
            lastId = lastEntry.getId();
        } else {
            lastId = 0L;
        }
        UserMessages userMessages = new UserMessages(lastId + 1, userId, messageId);
        return repository.add(userMessages);
    }

    public Result getAllForUser(Long id) {
        Result result = repository.getAll();
        if (result.isSuccess()) {
            Map<Long, List<String>> entries = new HashMap<>();
            List<UserMessages> userMessages = ((ResultSuccess<List<UserMessages>>) result).getData();
            for (UserMessages userMessage : userMessages) {

                Result messageResult = messageRepository.get(userMessage.getMessageId());
                if (messageResult.isSuccess()) {
                    Message message = ((ResultSuccess<Message>) messageResult).getData();
                    Result userResult;
                    if (Objects.equals(userMessage.getUserId(), id) || Objects.equals(message.getFrom(), id)) {
                        if (Objects.equals(message.getFrom(), id)) {
                            userResult = userRepository.get(userMessage.getUserId());
                            if (userResult.isSuccess()) {
                                User user = ((ResultSuccess<User>) userResult).getData();
                                entries.put(user.getId(), List.of(user.getFirstName(), user. getLastName(), message.getMessage()));
                                entries.remove(message.getFrom());
                            }
                        } else {
                            userResult = userRepository.get(message.getFrom());
                            if (userResult.isSuccess()) {
                                User user = ((ResultSuccess<User>) userResult).getData();
                                entries.put(user.getId(), List.of(user.getFirstName(), user. getLastName(), message.getMessage()));
                                entries.remove(userMessage.getUserId());
                            }
                        }
                    }
                }
            }
            return new ResultSuccess<>(entries);
        }
        return result;
    }

    public Result getConversation(Long idUser1, Long idUser2) {
        Result result = repository.getAll();
        if (result.isSuccess()) {
            List<Message> entries = new ArrayList<>();
            List<UserMessages> userMessages = ((ResultSuccess<List<UserMessages>>) result).getData();
            for (UserMessages userMessage : userMessages) {
                Result userResult = userRepository.get(userMessage.getUserId());
                Result messageResult = messageRepository.get(userMessage.getMessageId());
                if (userResult.isSuccess() && messageResult.isSuccess()) {
                    User user = ((ResultSuccess<User>) userResult).getData();
                    Message message = ((ResultSuccess<Message>) messageResult).getData();
                    if ((Objects.equals(user.getId(), idUser1) && Objects.equals(message.getFrom(), idUser2)) ||
                        (Objects.equals(user.getId(), idUser2) && Objects.equals(message.getFrom(), idUser1))) {
                        entries.add(message);
                    }
                }
            }
            return new ResultSuccess<>(entries);
        }
        return result;
    }

}
