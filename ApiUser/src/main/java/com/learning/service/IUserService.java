package com.learning.service;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.web.multipart.MultipartFile;

import com.learning.exception.exceptions.EmailExistException;
import com.learning.exception.exceptions.EmailNotFoundException;
import com.learning.exception.exceptions.NotAnImageFileException;
import com.learning.exception.exceptions.UserNotFoundException;
import com.learning.exception.exceptions.UsernameExistException;
import com.learning.model.dto.UserDto;
import com.learning.model.entity.User;

public interface IUserService {

	UserDto register(UserDto userDto) throws UserNotFoundException,
			UsernameExistException, EmailExistException, AddressException, MessagingException;
	
	//Important note
	//UserDto register(UserDto userDto);

	List<User> getUsers();

	User findUserByUsername(String username);

	User findUserByEmail(String email);

	User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked,
			boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException,
			EmailExistException, IOException, NotAnImageFileException;

	void deleteUser(String username) throws IOException, UserNotFoundException;

	void resetPassword(String email) throws MessagingException, EmailNotFoundException;

	User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException,
			UsernameExistException, EmailExistException, IOException, NotAnImageFileException;

	User updateUser(String currentUsername, String newFirstname, String newLastname, String newUsername,
			String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage)
			throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
			NotAnImageFileException;
}
