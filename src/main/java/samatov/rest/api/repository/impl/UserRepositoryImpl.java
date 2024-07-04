package samatov.rest.api.repository.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import samatov.rest.api.exception.NotFoundException;
import samatov.rest.api.exception.UserException;
import samatov.rest.api.model.User;
import samatov.rest.api.repository.UserRepository;
import samatov.rest.api.config.HibernateConfig;

import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public List<User> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User", User.class);
            return query.list();
        } catch (Exception e) {
            throw new UserException("Ошибка запроса, пользователи не найдены...");
        }
    }

    @Override
    public User findById(Integer id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new NotFoundException("Пользователь с указаным id:=" + id + " не найден...");
            }
            return user;
        } catch (Exception e) {
            throw new UserException("Ошибка запроса, пользователь с указаным id:=" + id + " не найден...");
        }
    }

    @Override
    public void removeById(Integer id) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user == null) {
                throw new NotFoundException("Пользователь с указаным id:=" + id + " не найден...");
            }
            session.delete(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new UserException("Ошибка запроса, пользователь с указаным id:=" + id + " не удалена...");
        }
    }

    @Override
    public User save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new UserException("Ошибка запроса, пользователя не удалось сохранить...");
        }
    }

    @Override
    public User update(User user) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new UserException("Ошибка запроса, пользователя не удалось изменить...");
        }
    }


}
