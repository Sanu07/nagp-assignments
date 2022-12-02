package com.nagarro.service;

import java.util.List;

import com.nagarro.entity.User;

public interface UserService {

	User save(User user);
	List<User> getUsers();
}
