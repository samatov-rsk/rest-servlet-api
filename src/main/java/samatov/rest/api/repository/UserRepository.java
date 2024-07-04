package samatov.rest.api.repository;

import samatov.rest.api.model.User;

import java.util.List;

public interface UserRepository <T,ID> {

    List<User> findAll();

    User findById(Integer id);

    User save(User user);

    void removeById(Integer id);

    User update(User user);
}
