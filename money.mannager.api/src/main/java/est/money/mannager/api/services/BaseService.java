package est.money.mannager.api.services;

import est.money.mannager.api.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public abstract class BaseService <T, TRepo extends CrudRepository<T, Long>>{

    @Autowired
    protected TRepo repo;

    public List<T> findAll(){
        return (List<T>) repo.findAll();
    }

    public T save(T value) {
        return repo.save(value);
    }

    public T find(long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    public T findOrNull(long id) {
        return repo.findById(id).orElse(null);
    }

    public T findOrDefault(long id, T defaultValue) {
        return repo.findById(id).orElse(defaultValue);
    }

    public void delete(Long id) {
        repo.delete(find(id));
    }

    public boolean exists(long id){
        return repo.existsById(id);
    }

    public void existsOrThrow(long id){
        if(!exists(id)){
            throw new ResponseStatusException(NOT_FOUND);
        }
    }
}
