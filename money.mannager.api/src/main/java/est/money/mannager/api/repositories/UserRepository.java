package est.money.mannager.api.repositories;

import est.money.mannager.api.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    public Optional<User> findByEmail(String email);
    public boolean existsByEmail(String email);
}
