package org.example.field_mask_demo.api;

import lombok.RequiredArgsConstructor;
import org.example.field_mask_demo.service.UserService;
import org.example.pb.User;
import org.example.rpc.CreateUserRequest;
import org.example.rpc.GetUserRequest;
import org.example.rpc.UpdateUserRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public User create(@RequestBody CreateUserRequest createUserRequest) {
        return userService.create(createUserRequest);
    }

    @PostMapping("/users/{id}")
    public User getById(@PathVariable String id, @RequestBody GetUserRequest getUserRequest) {
        return userService.getById(id, getUserRequest);
    }

    @PutMapping("/users/{id}")
    public User update(@PathVariable String id, @RequestBody UpdateUserRequest updateUserRequest) {
        return userService.update(id, updateUserRequest);
    }
}
