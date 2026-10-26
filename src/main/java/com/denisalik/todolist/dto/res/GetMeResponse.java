package com.denisalik.todolist.dto.res;

import com.denisalik.todolist.entity.User;

public record GetMeResponse(String username, String email, Boolean admin, Boolean banned) {
    public static GetMeResponse fromUser(User user){
        return new GetMeResponse(user.getUsername(), user.getEmail(), user.isAdmin(), user.getBanned());
    }
}
