package est.money.mannager.api.services;

import est.money.mannager.api.dtos.StatisticsDto;
import est.money.mannager.api.dtos.UserForUpdate;
import est.money.mannager.api.models.User;
import est.money.mannager.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@Service
public class UserService extends BaseService<User, UserRepository>{

    @Autowired
    private AuthService authService;

    public User update(Long id, UserForUpdate newUser) {

        if(newUser.name == null || newUser.name.trim().length() == 0 || newUser.email == null || newUser.email.trim().length() == 0){
            throw  new ResponseStatusException(BAD_REQUEST);
        }

        boolean updatePass = newUser.password != null && newUser.password.trim().length() > 0
                && newUser.confirmation !=null && newUser.confirmation.trim().length() > 0;

        if(updatePass && !newUser.password.equals(newUser.confirmation)){
            throw  new ResponseStatusException(BAD_REQUEST);
        }

        return repo.findById(id)
                .map(user -> {
                    user.setName(newUser.name);
                    user.setEmail(newUser.email);
                    user.setAdmin(newUser.admin);

                    if(updatePass){
                        user.setHashPass(authService.hash(newUser.password.trim(), user.getSalt()));
                    }

                    return repo.save(user);
                })
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }
}
