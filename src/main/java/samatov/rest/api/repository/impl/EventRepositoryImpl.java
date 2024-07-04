package samatov.rest.api.repository.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import samatov.rest.api.config.HibernateConfig;
import samatov.rest.api.exception.EventException;
import samatov.rest.api.exception.NotFoundException;
import samatov.rest.api.model.Event;
import samatov.rest.api.repository.EventRepository;

import java.util.List;

public class EventRepositoryImpl implements EventRepository {

    @Override
    public List<Event> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query<Event> query = session.createQuery("FROM Event", Event.class);
            return query.list();
        } catch (Exception e) {
            throw new EventException("Ошибка запроса, события не найдены");
        }
    }

    @Override
    public Event findById(Integer id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Event event = session.get(Event.class, id);
            if (event == null) {
                throw new NotFoundException("Ошибка запроса, событие с указанным id:=" + id + " не найдено...");
            }
            return event;
        } catch (Exception e) {
            throw new EventException("Ошибка запроса, событие с указанным id:=" + id + " не найдено...");
        }
    }

    @Override
    public Event save(Event event) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(event);
            transaction.commit();
            return event;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new EventException("Ошибка запроса, событие не удалось сохранить...");
        }
    }
}
