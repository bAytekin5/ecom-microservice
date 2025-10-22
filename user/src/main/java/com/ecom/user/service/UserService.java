package com.ecom.user.service;

import com.ecom.user.dto.UserMapper;
import com.ecom.user.dto.UserRequest;
import com.ecom.user.dto.UserResponse;
import com.ecom.user.model.User;
import com.ecom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

//    private List<User> users = new ArrayList<User>();
//    private Long nextId = 1L;

    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToUserResponse)
                .collect(Collectors.toList());
//        return users;
    }

    public User addUser(UserRequest request) {
        User user = new User();
        userMapper.updateUserFromRequest(user, request);
        return userRepository.save(user);
//        user.setId(nextId++);
//        users.add(user);
    }


    public Optional<UserResponse> fetchUser(Long id) {
        return userRepository.findById(id)
                .map(userMapper::mapToUserResponse);
//        return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    public User updateUser(Long id, UserRequest updatedUserRequest) {


        return userRepository.findById(id)
                .map(existingUser -> {
                    userMapper.updateUserFromRequest(existingUser, updatedUserRequest);
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));

//        return users.stream().filter(u -> u.getId() == id)
//                .findFirst()
//                .map(existingUser -> {
//                    existingUser.setFirstName(updatedUser.getFirstName());
//                    existingUser.setLastName(updatedUser.getLastName());
//                    return true;
//                }).orElse(false);
    }
}
