package com.learning.resource;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.model.entity.Privilege;
import com.learning.service.IPrivilegeService;

@RestController
@RequestMapping(path = { "/privilege" })
@CrossOrigin(origins = "*")
public class PrivilegeResource {

	private IPrivilegeService privilegeService;

	@Autowired
	public PrivilegeResource(IPrivilegeService privilegeService) {
		this.privilegeService = privilegeService;
	}
	
	@GetMapping("/list")
	public ResponseEntity<List<Privilege>> getAllRoles() {
		List<Privilege> privileges = privilegeService.getAllPrivileges();
		return new ResponseEntity<>(privileges, OK);
	}

	@PostMapping("/add")
	public ResponseEntity<Privilege> add(@RequestBody Privilege privilege) {
		Privilege privilegeSaved = privilegeService.addNewPrivilege(privilege);
		return new ResponseEntity<>(privilegeSaved, CREATED);
	}
	
	@PostMapping("/addlist")
	public ResponseEntity<List<Privilege>> Addlist(@RequestBody List<Privilege> privileges) {
		List<Privilege> privilegesSaved = privilegeService.addList(privileges);
		return new ResponseEntity<>(privilegesSaved, CREATED);
	}

}
