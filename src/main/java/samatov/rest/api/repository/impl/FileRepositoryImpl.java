package samatov.rest.api.repository.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import samatov.rest.api.config.HibernateConfig;
import samatov.rest.api.exception.FileException;
import samatov.rest.api.exception.NotFoundException;
import samatov.rest.api.model.File;
import samatov.rest.api.repository.FileRepository;

import java.util.List;

public class FileRepositoryImpl implements FileRepository {

    @Override
    public List<File> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query<File> query = session.createQuery("FROM File", File.class);
            return query.list();
        } catch (Exception e) {
            throw new FileException("Ошибка запроса, посты не найдены");
        }
    }

    @Override
    public File findById(Integer id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            File file = session.get(File.class, id);
            if (file == null) {
                throw new NotFoundException("Ошибка запроса, file с указанным id:=" + id + " не найден...");
            }
            return file;
        } catch (Exception e) {
            throw new FileException("Ошибка запроса, file с указанным id:=" + id + " не найден...");
        }
    }

    @Override
    public File save(File file) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(file);
            transaction.commit();
            return file;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new FileException("Ошибка запроса, файл не удалось сохранить...");
        }
    }
}
