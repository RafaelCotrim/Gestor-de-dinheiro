package est.money.mannager.api.services;

import est.money.mannager.api.dtos.UserDTO;
import est.money.mannager.api.dtos.UserForLogin;
import est.money.mannager.api.models.Category;
import est.money.mannager.api.models.User;
import est.money.mannager.api.repositories.CategoryRepository;
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
public class UserService extends BaseService<User, UserRepository>{


    public User update(Long id, User newUser) {

        return repo.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    return repo.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repo.save(newUser);
                });
    }

    public boolean existsByEmail(String email){
        return repo.existsByEmail(email);
    }
}
