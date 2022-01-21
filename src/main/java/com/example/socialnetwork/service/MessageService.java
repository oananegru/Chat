package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Message;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.UserMessages;
import com.example.socialnetwork.repository.base.Repository;
import com.example.socialnetwork.result.Result;
import com.example.socialnetwork.result.ResultError;
import com.example.socialnetwork.result.ResultSuccess;
import com.example.socialnetwork.service.base.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MessageService extends Service<Message> {

    private final Repository<User> userRepository;
    private final Repository<UserMessages> userMessagesRepository;

    public MessageService(Repository<Message> repository, Repository<User> userRepository,
                          Repository<UserMessages> userMessagesRepository) {
        super(repository);
        this.userRepository = userRepository;
        this.userMessagesRepository = userMessagesRepository;

    }

    public Long getLastMessageId(){
        return repository.getLastEntry().getId();
    }

    /**
     * Add a new message
     * @param message - text of message
     * @param from - from who is message
     * @param dateTime - date of sending
     * @param reply - the id of replier or 0 if is first message
     * @return success status
     * @throws IOException if repository is FileRepository and fails saving data to file
     */
    public Result add(String message, Long from, LocalDateTime dateTime, int reply) throws IOException {
        String date = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
        Message lastEntry = repository.getLastEntry();
        Long lastId;
        if (lastEntry != null) {
            lastId = lastEntry.getId();
        } else {
            lastId = 0L;
        }

        Message m = new Message(lastId + 1, message, from, date, reply);
        return repository.add(m);
    }

    /**
     * Delete message for specified user
     * @param mess_id - id for message
     * @return success status
     * @throws IOException
     */
    public Result delete(Long mess_id) throws IOException {
        Result foundMessageResult = get(mess_id);
        if (foundMessageResult.isSuccess()) {
            Message message = ((ResultSuccess<Message>) foundMessageResult).getData();
            repository.delete(message.getId());
            return new ResultSuccess<>();
        } else {
            return foundMessageResult;
        }
    }


    public Result updateMessage(Long id, String message) throws IOException {
        // Get message
        Result result = repository.get(id);
        // If result is success (message exists)
        if (result.isSuccess()) {
            Message message1 = ((ResultSuccess<Message>) result).getData();
            // Update password
            message1.setMessage(message);
            return repository.update(message1);
        }
        return result;
    }

    /**
     * Deletes all messages that contain a specified user id
     * @param userId - the id of user
     * @return success status
     * @throws IOException
     */
    public Result deleteAllForUser(Long userId) throws IOException {
        Result result = repository.getAll();
        Result deleteResult = null;
        if (result.isSuccess()) {
            Iterable<Message> messages = ((ResultSuccess<Iterable<Message>>) result).getData();
            for (Message message : messages) {
                if (Objects.equals(userId, message.getFrom()) ) {
                    deleteResult = repository.delete(message.getId());
                }
            }
            return deleteResult;
        } else {
            return result;
        }
    }

    /**
     * Chek if users exists and if other same message between same users already exists
     * @param idMessage - the id of message
     * @return Result success if message can be added, Result error otherwise
     */
    private Result canBeAdded(Long idMessage) {
        if (!repository.get(idMessage).isSuccess()) {
            return new ResultError("A message with id " + idMessage + " does not exist!");
        }

        if (get(idMessage).isSuccess()) {
            return new ResultError("Message already exists!");
        }
        return new ResultSuccess<>();
    }

//    /**
//     * Returns all messages from two specified users
//     * @param firstUserId - id of first user
//     * @param secondUserId - id of second user
//     * @return success status
//     */
//    public Result getAllMessagesForTwoUser(Long firstUserId, Long secondUserId) {
//        if (firstUserId == null) {
//            return new ResultError("Id must be not null!");
//        }
//        if (secondUserId == null) {
//            return new ResultError("Id must be not null!");
//        }
//        if (!userRepository.get(firstUserId).isSuccess() || !userRepository.get(secondUserId).isSuccess())
//            return new ResultError("This user(s) does not exists!");
//        Result messageResult = repository.getAll();
//        Result userMessageResult = userMessagesRepository.getAll();
//        if (messageResult.isSuccess()) {
//            List<Message> messages = (List<Message>) ((ResultSuccess<Iterable<Message>>) messageResult).getData();
//            // Stream operation - filter, map
//            List<List<String>> userMessages = messages.stream()
//                    .filter(s -> Objects.equals(firstUserId, s.getFrom()) || Objects.equals(secondUserId, s.getFrom()))
//                    .map(x -> {
//                        Long receptorId;
//                        if (Objects.equals(x.getFrom(), firstUserId)) {
//                            receptorId = secondUserId;
//                        } else {
//                            receptorId = firstUserId;
//                        }
//
//                        Result receptorResult = userRepository.get(receptorId);
//                        if (receptorResult.isSuccess()) {
//                            User receptor = ((ResultSuccess<User>) receptorResult).getData();
//                            return List.of(friend.getFirstName(), friend.getLastName(), x.getDate());
//                        }
//                        return List.of("", "", x.getDate());
//                    })
//                    .toList();
//            return new ResultSuccess<>(userFriendships);
//        }
//        return result;
//    }
}
