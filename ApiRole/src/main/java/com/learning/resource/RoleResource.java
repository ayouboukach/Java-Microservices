package com.learning.resource;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.model.entity.Role;
import com.learning.service.IRoleService;

@RestController
@RequestMapping(path = { "/role" })
@CrossOrigin(origins = "*")
public class RoleResource {

	private IRoleService roleService;

	@Autowired
	public RoleResource(IRoleService roleService) {
		this.roleService = roleService;
	}

	@GetMapping("/list")
	public ResponseEntity<List<Role>> getAllRoles() {
		List<Role> roles = roleService.getAllRoles();
		return new ResponseEntity<>(roles, OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Role> getRoleById(@PathVariable(value = "id") String id) {
		Role role = roleService.getRole(id);
		return new ResponseEntity<>(role, OK);
	}

	@PostMapping("/add")
	public ResponseEntity<Role> register(@RequestBody Role role) {
//		if(role.get)
		Role roleSaved = roleService.addNewRole(role);
		return new ResponseEntity<>(roleSaved, CREATED);
	}
	
	@PostMapping("/addall")
	public ResponseEntity<List<Role>> register(@RequestBody List<Role> roles) {
//		if(role.get)
		List<Role> roleSaved = roleService.addNewRoles(roles);
		return new ResponseEntity<>(roleSaved, CREATED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Role> update(@RequestBody Role role) {
		Role roleUpdated = roleService.UpdateRole(role);
		return new ResponseEntity<>(roleUpdated, CREATED);
	}

}
