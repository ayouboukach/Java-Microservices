package com.learning.service.impl;


import static com.learning.constant.FileConstant.*;
import static com.learning.constant.UserSeviceImplConstant.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.MediaType.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.learning.enumeration.Role;
import com.learning.exception.exceptions.EmailExistException;
import com.learning.exception.exceptions.EmailNotFoundException;
import com.learning.exception.exceptions.NotAnImageFileException;
import com.learning.exception.exceptions.UserNotFoundException;
import com.learning.exception.exceptions.UsernameExistException;
import com.learning.model.dto.UserDto;
import com.learning.model.entity.User;
import com.learning.model.entity.UserRemoved;
import com.learning.repository.IUserRemovedRepository;
import com.learning.repository.IUserRepository;
import com.learning.security.UserPrincipal;
import com.learning.service.IRoleServiceClient;
import com.learning.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Qualifier("userDetailsService")
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService, UserDetailsService {

	private final IUserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final LoginAttemptService loginAttemptService;
//	private final EmailService emailService;
	private final IUserRemovedRepository userRemovedRepository;
	private final IRoleServiceClient roleServiceClient;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			log.error(NO_USER_FOUND_BY_USERNAME + username);
			throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
		} else {
			validateLoginAttempt(user);
			user.setLastLoginDateDisplay(user.getLastLoginDate());
			user.setLastLoginDate(new Date());
			userRepository.save(user);
			log.info("Before calling Role Microservice");
//			try {
			user.setRoles(roleServiceClient.getRole(user.getRoleid()));
//			} catch (FeignException e) {
//				// TODO: handle exception
//			}
			log.info("After calling Role Microservice");
			UserPrincipal userPrincipal = new UserPrincipal(user);
			log.info(FOUND_USER_BY_USERNAME + username);
			return userPrincipal;
		}
	}

	@Override
	public UserDto register(UserDto userDto) throws UserNotFoundException, UsernameExistException, EmailExistException,
			AddressException, MessagingException {

		validateNewUsernameAndEmail(EMPTY, userDto.getUsername(), userDto.getEmail());
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		User userEntity = mapper.map(userDto, User.class);
		userEntity.setUserId(generateUserId());
		String password = generatePassword();
		userEntity.setJoinDate(new Date());
		userEntity.setPassword(encodePassword(password));
		userEntity.setActive(true);
		userEntity.setNotLocked(true);
		userEntity.setRole(Role.ROLE_SUPER_ADMIN.name());
		userEntity.setAuthorities(Role.ROLE_SUPER_ADMIN.getAuthorities());
		userEntity.setProfileImageUrl(getTemporaryProfileImageUrl(userEntity.getUsername()));
//		emailService.sendNewPasswordEmail(firstname, password, email);
		userRepository.save(userEntity);
		log.info("New user password: " + password);
		UserDto userDtoReturned = mapper.map(userEntity, UserDto.class);
		return userDtoReturned;
	}

	@Override
	public List<User> getUsers() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	public User findUserByUsername(String username) {
		User user = userRepository.findByUsername(username);
//		user.setRoles(getRole(user.getRoleid()));
		return user;
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User addNewUser(String firstname, String lastname, String username, String email, String role,
			boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException,
			UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
		validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
		User user = new User();
		String password = generatePassword();
		user.setUserId(generateUserId());
		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setJoinDate(new Date());
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(encodePassword(password));
		user.setActive(isActive);
		user.setNotLocked(isNonLocked);
		user.setRole(getRoleEnumName(role).name());
		user.setAuthorities(getRoleEnumName(role).getAuthorities());
		user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
		userRepository.save(user);
		saveProfileImage(user, profileImage);
		log.info("New user password: " + password);
		return user;
	}

	@Override
	public User updateUser(String currentUsername, String newFirstname, String newLastname, String newUsername,
			String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage)
			throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
			NotAnImageFileException {
		User currentUser = validateNewUsernameAndEmail(currentUsername, newUsername, newEmail);
		currentUser.setFirstname(newFirstname);
		currentUser.setLastname(newLastname);
		currentUser.setUsername(newUsername);
		currentUser.setEmail(newEmail);
		currentUser.setActive(isActive);
		currentUser.setNotLocked(isNonLocked);
		currentUser.setRole(getRoleEnumName(role).name());
		currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
		userRepository.save(currentUser);
		saveProfileImage(currentUser, profileImage);
		return currentUser;
	}

	@Override
	public void deleteUser(String username) throws IOException, UserNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
		} else {
			UserRemoved userRemoved = new ModelMapper().map(user, UserRemoved.class);
			userRemoved.setIdBefore(user.getId());
			try {
				userRemovedRepository.save(userRemoved);
				userRepository.deleteByUsername(username);
				Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
				FileUtils.deleteDirectory(new File(userFolder.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void resetPassword(String email) throws MessagingException, EmailNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
		}
		String password = generatePassword();
		user.setPassword(encodePassword(password));
		userRepository.save(user);
//		emailService.sendNewPasswordEmail(user.getFirstname(), password, user.getEmail());
		log.info("New user password: " + password);
	}

	@Override
	public User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException,
			UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
		User user = validateNewUsernameAndEmail(username, null, null);
		saveProfileImage(user, profileImage);
		return user;
	}

	private void saveProfileImage(User user, MultipartFile profileImage) throws IOException, NotAnImageFileException {
		if (profileImage != null && user != null) {
			if (!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE)
					.contains(profileImage.getContentType())) {
				throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
			}
			Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
			if (!Files.exists(userFolder)) {
				Files.createDirectories(userFolder);
				log.info(DIRECTORY_CREATED + userFolder);
			}
			Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + DOT + JPG_EXTENSION));
			Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION),
					REPLACE_EXISTING);
			user.setProfileImageUrl(setProfileImageUrl(user.getUsername()));
			userRepository.save(user);
			log.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
		}
	}

	// Ayoub/booklibrary/user/ayouboukach/
	// Ayoub/booklibrary/user/image/ayouboukach/ayouboukach.jpg

	private String setProfileImageUrl(String username) {
		return ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(USER_IMAGE_PATH + username + FORWARD_SLASH + username + DOT + JPG_EXTENSION).toUriString();
	}

	private String getTemporaryProfileImageUrl(String username) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username)
				.toUriString();
	}

	private Role getRoleEnumName(String role) {
		return Role.valueOf(role.toUpperCase());
	}

	private String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	private String generatePassword() {
		return RandomStringUtils.randomAlphanumeric(10);
	}

	private String generateUserId() {
		return UUID.randomUUID().toString();
//		return RandomStringUtils.randomNumeric(10);
	}

	private void validateLoginAttempt(User user) {
		if (user.isNotLocked()) {
			if (loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
				user.setNotLocked(false);
			} else {
				user.setNotLocked(true);
			}
		} else {
			loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
		}
	}

	private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail)
			throws UserNotFoundException, UsernameExistException, EmailExistException {
		User userByNewUsername = findUserByUsername(newUsername);
		User userByNewEmail = findUserByEmail(newEmail);
		if (StringUtils.isNotBlank(currentUsername)) {
			User currentUser = findUserByUsername(currentUsername);
			if (currentUser == null) {
				throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
			}
			if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
				throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
			}
			if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
				throw new EmailExistException(EMAIL_ALREADY_EXISTS);
			}
			return currentUser;
		} else {
			if (userByNewUsername != null) {
				throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
			}
			if (userByNewEmail != null) {
				throw new EmailExistException(EMAIL_ALREADY_EXISTS);
			}
			return null;
		}
	}
}
