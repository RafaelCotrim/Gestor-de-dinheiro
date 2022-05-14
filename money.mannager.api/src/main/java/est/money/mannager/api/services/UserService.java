package est.money.mannager.api.services;

import est.money.mannager.api.dtos.UserDTO;
import est.money.mannager.api.dtos.UserForLogin;
import est.money.mannager.api.models.User;
import est.money.mannager.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll(){
        return (List<User>) userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User find(long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
    }

    public User update(Long id, User newUser) {

        return userRepository.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });
    }

    public void delete(Long id) {
        if(userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
            return;
        }

        throw new ResponseStatusException(NOT_FOUND, "User not found");

    }

    public boolean exists(long id){
        return userRepository.existsById(id);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
