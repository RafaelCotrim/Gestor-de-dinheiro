package est.money.mannager.api.repositories;

import est.money.mannager.api.dtos.StatisticsDto;
import est.money.mannager.api.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    public Optional<User> findByEmail(String email);
    public boolean existsByEmail(String email);
}
