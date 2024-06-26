package riccardogulin.u5d8.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import riccardogulin.u5d8.entities.User;
import riccardogulin.u5d8.services.UsersService;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {
	@Autowired
	private UsersService usersService;

	// 1 . GET http://localhost:3001/users
	@GetMapping
	public Page<User> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
		return this.usersService.getUsers(page, size, sortBy);
	}

	// 2 . POST http://localhost:3001/users
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public User saveUser(@RequestBody User body) {
		return usersService.save(body);
	}

	// 3. GET http://localhost:3001/users/{userId}
	@GetMapping("/{userId}")
	public User findById(@PathVariable UUID userId) {
		return this.usersService.findById(userId);
	}

	// 4. PUT http://localhost:3001/users/{userId}
	@PutMapping("/{userId}")
	public User findByIdAndUpdate(@PathVariable UUID userId, @RequestBody User body) {
		return this.usersService.findByIdAndUpdate(userId, body);
	}


	// 5. DELETE http://localhost:3001/users/{userId}
	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void findByIdAndDelete(@PathVariable UUID userId) {
		this.usersService.findByIdAndDelete(userId);
	}


}
