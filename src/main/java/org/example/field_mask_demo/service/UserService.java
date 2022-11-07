package org.example.field_mask_demo.service;

import com.google.protobuf.util.FieldMaskUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.field_mask_demo.commons.exception.NotFoundException;
import org.example.field_mask_demo.commons.exception.ServerException;
import org.example.field_mask_demo.repo.UserRepo;
import org.example.pb.User;
import org.example.rpc.CreateUserRequest;
import org.example.rpc.GetUserRequest;
import org.example.rpc.UpdateUserRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public User create(CreateUserRequest createUserRequest) {
        log.debug("Creating user : {}", createUserRequest);
        User user = createUserRequest.getUser();
        String id = UUID.randomUUID().toString();
        user = user.toBuilder()
                .setId(id)
                .setCreated(System.currentTimeMillis())
                .setUpdated(System.currentTimeMillis())
                .build();
        if (userRepo.create(user)) {
            return userRepo.getById(id);
        }
        throw new ServerException("Failed to create user");
    }

    public User getById(String id, GetUserRequest getUserRequest) {
        log.debug("Getting user by id : {}", id);
        User byId = userRepo.getById(id);
        if (byId != null) {
            return FieldMaskUtil.trim(getUserRequest.getMask(), byId);
        }
        throw new NotFoundException("User not found");
    }

    public User update(String id, UpdateUserRequest updateUserRequest) {
        log.debug("Updating user by id : {} {}", id, updateUserRequest);
        User byId = userRepo.getById(id);
        if (byId == null) {
            throw new NotFoundException("User not found");
        }
        User.Builder destination = byId.toBuilder();
        FieldMaskUtil.merge(updateUserRequest.getMask(), updateUserRequest.getUser(), destination);
        if (userRepo.update(destination.build())) {
            return userRepo.getById(id);
        }
        throw new ServerException("Failed to update user");
    }
}
