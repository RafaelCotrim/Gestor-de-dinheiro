package est.money.mannager.api.services;

import est.money.mannager.api.dtos.UserDTO;
import est.money.mannager.api.dtos.UserForLogin;
import est.money.mannager.api.dtos.UserForRegister;
import est.money.mannager.api.models.User;
import est.money.mannager.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import static org.springframework.http.HttpStatus.*;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO login(UserForLogin ufl){

        User u = userRepository.findByEmail(ufl.getEmail()).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));

        byte[] hash = hash(ufl.getPassword(), u.getSalt());

        if(Arrays.equals(hash, u.getHashPass())){
            return new UserDTO(u.getId(), u.getName(), u.getEmail(), u.isAdmin());
        }

        throw new ResponseStatusException(BAD_REQUEST);
    }

    public UserDTO register(UserForRegister ufr){

        ufr.email = ufr.email.toLowerCase();

        if(!ufr.password.equals(ufr.confirmation)){
            throw new ResponseStatusException(BAD_REQUEST, "Password and confirmation password do not match");
        }

        if(userRepository.existsByEmail(ufr.email)){
            throw new ResponseStatusException(BAD_REQUEST, "Email already in use");
        }

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        byte[] hash = hash(ufr.password, salt);

        User u = new User(ufr.name, ufr.email, salt, hash, false);

        User savedUser = userRepository.save(u);
        return new UserDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), u.isAdmin());
    }

    public byte[] hash(String password, byte[] salt){

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);

        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR);
        }

        byte[] hash;

        try {
            hash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR);
        }

        return hash;
    }
}
