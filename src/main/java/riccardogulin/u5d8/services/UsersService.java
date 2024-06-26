package riccardogulin.u5d8.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import riccardogulin.u5d8.entities.User;
import riccardogulin.u5d8.exceptions.BadRequestException;
import riccardogulin.u5d8.exceptions.NotFoundException;
import riccardogulin.u5d8.repositories.UsersRepository;

import java.util.UUID;

@Service
public class UsersService {
	@Autowired
	private UsersRepository usersRepository;

	public Page<User> getUsers(int pageNumber, int pageSize, String sortBy) {
		if (pageSize > 100) pageSize = 100;
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
		return usersRepository.findAll(pageable);
	}

	public User save(User newUser) {
		// 1. Verifico se l'email è già in uso
		this.usersRepository.findByEmail(newUser.getEmail()).ifPresent(
				// 1.1 Se lo è triggero un errore
				user -> {
					throw new BadRequestException("L'email " + newUser.getEmail() + " è già in uso!");
				}
		);

		// 2. Altrimenti aggiungiamo l'avatarURL (ed eventuali altri campi server-generated)
		newUser.setAvatarURL("https://ui-avatars.com/api/?name=" + newUser.getName() + "+" + newUser.getSurname());

		// 3. Poi salviamo lo user
		return usersRepository.save(newUser);
	}

	public User findById(UUID userId) {
		return this.usersRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
	}

	public User findByIdAndUpdate(UUID userId, User modifiedUser) {
		User found = this.findById(userId);
		found.setName(modifiedUser.getName());
		found.setSurname(modifiedUser.getSurname());
		found.setEmail(modifiedUser.getEmail());
		found.setPassword(modifiedUser.getPassword());
		found.setAvatarURL("https://ui-avatars.com/api/?name=" + modifiedUser.getName() + "+" + modifiedUser.getSurname());
		return this.usersRepository.save(found);
	}

	public void findByIdAndDelete(UUID userId) {
		User found = this.findById(userId);
		this.usersRepository.delete(found);
	}
}
