package com.learning.resource;

import static com.learning.constant.FileConstant.*;
import static com.learning.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static com.learning.constant.UserResourceConstant.*;
import static com.learning.constant.UserSeviceImplConstant.NO_USER_FOUND_BY_USERNAME;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learning.exception.ExceptionHandling;
import com.learning.exception.exceptions.EmailExistException;
import com.learning.exception.exceptions.EmailNotFoundException;
import com.learning.exception.exceptions.NotAnImageFileException;
import com.learning.exception.exceptions.UserNotFoundException;
import com.learning.exception.exceptions.UsernameExistException;
import com.learning.model.dto.UserDto;
import com.learning.model.entity.User;
import com.learning.model.rest.HttpResponse;
import com.learning.model.rest.UserLoginRequestModel;
import com.learning.model.rest.UserRegisterRequestModel;
import com.learning.model.rest.UserRegisterResponseModel;
import com.learning.security.UserPrincipal;
import com.learning.security.jwt.JwtTokenProvider;
import com.learning.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = { "/", "/user" })
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "This REST controller provide services to manage users in the User Tracker application") 
public class UserResource extends ExceptionHandling {

	private static final String USER_ID = "USER-ID";
	private final AuthenticationManager authenticationManager;
	private final IUserService userService;
	private final JwtTokenProvider jwtTokenProvider;
	private final Environment evironment;

	@GetMapping("/check")
	public String check() {
		return "work with port: " + evironment.getProperty("local.server.port") + " and token secret: "
				+ evironment.getProperty("jwt.jwtkeysecret") + " and public urls: "
				+ evironment.getProperty("public.url");
	}

	@PostMapping("/login")
	@Operation(summary = "Provides Login User To Generate New JWT")
	public ResponseEntity<User> login(@Valid @RequestBody UserLoginRequestModel user) {
		authenticate(user.getUsername(), user.getPassword());
		User loginUser = userService.findUserByUsername(user.getUsername());
		UserPrincipal userPrincipal = new UserPrincipal(loginUser);
		HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
		return new ResponseEntity<>(loginUser, jwtHeader, OK);
	}

	@PostMapping("/register")
	@Operation(summary = "Provides Registration Service New User")
	public ResponseEntity<HttpResponse> register(@Valid @RequestBody UserRegisterRequestModel userRequestModel)
			throws UserNotFoundException, UsernameExistException, EmailExistException, AddressException,
			MessagingException {
		UserDto userDto = new ModelMapper().map(userRequestModel, UserDto.class);
		UserRegisterResponseModel responseModel = new ModelMapper().map(userService.register(userDto),
				UserRegisterResponseModel.class);
		return response(CREATED, USER_CREATED_SUCCESSFULLY, Map.of("User Registred", responseModel));
	}

	@PostMapping("/add")
	@Operation(summary = "Provides Add New User In System")
	public ResponseEntity<User> addNewUser(@RequestParam("firstname") String firstname,
			@RequestParam("lastname") String lastname, @RequestParam("username") String username,
			@RequestParam("email") String email, @RequestParam("role") String role,
			@RequestParam("isActive") String isActive, @RequestParam("isNonLocked") String isNonLocked,
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
			throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
			NotAnImageFileException {
		User newUser = userService.addNewUser(firstname, lastname, username, email, role,
				Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
		return new ResponseEntity<>(newUser, CREATED);
	}

	@PutMapping("/update")
	@Operation(summary = "Provides Updating User Already in System")
	public ResponseEntity<User> update(@RequestParam("currentUsername") String currentUsername,
			@RequestParam("firstname") String firstname, @RequestParam("lastname") String lastname,
			@RequestParam("username") String username, @RequestParam("email") String email,
			@RequestParam("role") String role, @RequestParam("isActive") String isActive,
			@RequestParam("isNonLocked") String isNonLocked,
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
			throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
			NotAnImageFileException {
		User updatedUser = userService.updateUser(currentUsername, firstname, lastname, username, email, role,
				Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
		return new ResponseEntity<>(updatedUser, OK);
	}

	@GetMapping("/find/{username}")
	@Operation(summary = "This Path can be using to finding a user in system")
	public ResponseEntity<User> getUser(@PathVariable("username") String username) throws UserNotFoundException {
		User user = userService.findUserByUsername(username);
		if (user == null) {
			throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
		}
		return new ResponseEntity<>(user, OK);
	}

	@GetMapping("/list")
	@Operation(summary = "This path getting all user in system")
	public ResponseEntity<HttpResponse> getAllUsers() {
		List<User> users = userService.getUsers();
		return response(OK, "Users retrieved", Map.of("Users", users));
	}

	@GetMapping("/resetpassword/{email}")
	@Operation(summary = "This Method supportting to reset password")
	public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email)
			throws MessagingException, EmailNotFoundException {
		userService.resetPassword(email);
		return response(OK, EMAIL_SENT + email, null);
	}

	@DeleteMapping("/delete/{username}")
	@PreAuthorize("hasAnyAuthority('user:delete')")
//	@PreAuthorize("principal==#userid")
//	@PreAuthorize("denyAll")
	@Operation(summary = "Provides Deleting User Already exist in System")
	public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username)
			throws IOException, UserNotFoundException {
		userService.deleteUser(StringUtils.strip(username));
		return response(OK, USER_DELETED_SUCCESSFULLY, Map.of("User deleted", username));
	}

	@PutMapping("/updateProfileImage")
	@Operation(summary = "Provides Updating User image")
	public ResponseEntity<User> updateProfileImage(@RequestParam("username") String username,
			@RequestParam(value = "profileImage") MultipartFile profileImage) throws UserNotFoundException,
			UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
		User user = userService.updateProfileImage(username, profileImage);
		return new ResponseEntity<>(user, OK);
	}

	@Operation(summary = "This path getting user image by username and filename")
	@GetMapping(path = "/image/{username}/{filename}", produces = IMAGE_JPEG_VALUE)
	public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("filename") String filename)
			throws IOException {
		return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + filename));
	}

	@Operation(summary = "This path getting temporary user image")
	@GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
	public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
		URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try (InputStream inputStream = url.openStream()) {
			int bytesRead;
			byte[] chunk = new byte[1024];
			while ((bytesRead = inputStream.read(chunk)) > 0) {
				byteArrayOutputStream.write(chunk, 0, bytesRead);
			}
		}
		return byteArrayOutputStream.toByteArray();
	}

	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message, Map<?, ?> map) {
		return new ResponseEntity<>(new HttpResponse(null, httpStatus.value(), httpStatus,
				httpStatus.getReasonPhrase().toUpperCase(), message, null, map, null), httpStatus);
	}

	private HttpHeaders getJwtHeader(UserPrincipal user) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
		headers.add(USER_ID, user.getUserId());
		return headers;
	}

	private void authenticate(String username, String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}
}
