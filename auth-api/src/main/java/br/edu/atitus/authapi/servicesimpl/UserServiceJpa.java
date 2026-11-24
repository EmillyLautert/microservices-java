package br.edu.atitus.authapi.servicesimpl;

import br.edu.atitus.authapi.components.Validator;
import br.edu.atitus.authapi.entities.UserEntity;
import br.edu.atitus.authapi.repositories.UserRepository;
import br.edu.atitus.authapi.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.regex.Pattern;

@Service
public class UserServiceJpa implements UserService {
    private static final Pattern PHONE_DIGITS = Pattern.compile("\\d{10,11}");
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserServiceJpa(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    private void validate(UserEntity user) throws Exception {
        if (user.getName() == null || user.getName().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome obrigatório");
        if (user.getEmail() == null || user.getEmail().isBlank() || !Validator.validateEmail(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail inválido");
        if (user.getPassword() == null || user.getPassword().length() < 8)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha deve ter pelo menos 8 caracteres");

        user.setName(user.getName().trim());
        user.setEmail(user.getEmail().trim().toLowerCase());
        if (user.getPhone() != null && !user.getPhone().isBlank()) {
            String phone = user.getPhone().trim();
            String digits = phone.replaceAll("\\D", "");
            if (!PHONE_DIGITS.matcher(digits).matches())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone inválido");
            user.setPhone(phone);
        } else {
            user.setPhone(null);
        }

        if (user.getId() != null) {
            if (userRepository.existsByEmailAndIdNot(user.getEmail(), user.getId()))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe usuário com este e-mail");
        } else {
            if (userRepository.existsByEmail(user.getEmail()))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe usuário com este e-mail");
        }
    }

    private void format(UserEntity user) throws Exception {
        user.setPassword(encoder.encode(user.getPassword()));
    }

    @Override
    public UserEntity save(UserEntity user) throws Exception {
        if (user == null)
            throw new Exception("Objeto nulo");
        validate(user);
        format(user);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com este e-mail"));
        return user;
    }
}
