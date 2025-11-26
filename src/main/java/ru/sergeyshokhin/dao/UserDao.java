package ru.sergeyshokhin.dao;


import jakarta.persistence.criteria.CriteriaBuilder;


import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.MutationQuery;

import ru.sergeyshokhin.entity.User;
import ru.sergeyshokhin.exception.AppException;
import ru.sergeyshokhin.util.AppUtil;

import java.util.Optional;


import static org.hibernate.resource.transaction.spi.TransactionStatus.MARKED_ROLLBACK;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {
    private final String HQL_DELETE = "DELETE FROM User WHERE id = :id";
    private final static UserDao INSTANCE = new UserDao();

    public static UserDao getINSTANCE() {
        return INSTANCE;
    }

    private final SessionFactory sessionFactory = AppUtil.getSessionFactory();

    public User create(User user) throws AppException {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.getTransaction();
            try {
                transaction.begin();
                session.persist(user);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                log.error("Введены данные, нарушающие ограничения базы данных. " + e.getMessage());
                throw new AppException("Нарушена уникальность. Данный email уже зарегистрирован.", e);
            } catch (HibernateException e) {
                log.error("Возникла неустранимая ошибка. " + e.getMessage() + ". Приложение остановлено!");
                throw new RuntimeException();
            } finally {
                if (transaction.isActive() || transaction.getStatus() == MARKED_ROLLBACK) transaction.rollback();
            }
        }
        return user;
    }

    public Optional<User> get(Integer id) {
        User userFromDB ;
        try (var session = sessionFactory.openSession()) {
            userFromDB = session.find(User.class, id);
        }
        return userFromDB == null ? Optional.empty() : Optional.of(userFromDB);
    }

    public boolean remove(Integer id) {

        boolean result = false;
        try (var session = sessionFactory.openSession()) {
            var transaction = session.getTransaction();
            MutationQuery query = session.createMutationQuery(HQL_DELETE);
            query.setParameter("id", id);
            int count;
            try {
                transaction.begin();
                count = query.executeUpdate();
                transaction.commit();
                if (count == 1) result = true;
            } catch (HibernateException e) {
                log.error("Возникла неустранимая ошибка базы данных. " + e.getMessage() + ". Приложение остановлено!");
                if (transaction.isActive() || transaction.getStatus() == MARKED_ROLLBACK) transaction.rollback();
                throw new RuntimeException();
            }
        }
        return result;
    }

    public Optional<User> update(User user) throws AppException {

        try (var session = sessionFactory.openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<User> updateQuery = builder.createCriteriaUpdate(User.class);
            Root<User> root = updateQuery.from(User.class);

            if (user.getName() != null) updateQuery.set(root.get("name"), user.getName());
            if (user.getEmail() != null) updateQuery.set(root.get("email"), user.getEmail());
            if (user.getAge() != 0) updateQuery.set(root.get("age"), user.getAge());
            updateQuery.where(builder.equal(root.get("id"), user.getId()));

            MutationQuery query = session.createMutationQuery(updateQuery);
            Transaction transaction = session.getTransaction();
            try {
                transaction.begin();
                int rows = query.executeUpdate();
                transaction.commit();

                if (rows == 1) user = session.find(User.class, user.getId());
                else user = null;
            } catch (ConstraintViolationException e) {
                log.error("Введены данные, нарушающие ограничения базы данных. " + e.getMessage());
                throw new AppException("Нарушена уникальность. Данный email уже зарегистрирован.",e);
            } catch (HibernateException e) {
                log.error("Возникла неустранимая ошибка базы данных. " + e.getMessage() + ". Приложение остановлено!");
                throw new RuntimeException();
            } finally {
                if (transaction.isActive() || transaction.getStatus() == MARKED_ROLLBACK) transaction.rollback();
            }
        }
        return user == null ? Optional.empty() : Optional.of(user);
    }
}
