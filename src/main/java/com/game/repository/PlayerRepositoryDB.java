package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {

    private final SessionFactory sessionFactory;

    public PlayerRepositoryDB() {
        Properties properties = new Properties();

        properties.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
        properties.put(Environment.URL, "jdbc:mysql://localhost:3306/rpg");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "sasha2058");

        properties.put(Environment.HBM2DDL_AUTO, "update");

        sessionFactory = new Configuration()
                .setProperties(properties)
                .buildSessionFactory();

    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {

        int skipPlayers = (pageNumber - 1) * pageSize;

        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT * FROM player LIMIT " + pageSize + "OFFSET " + skipPlayers;
            NativeQuery<Player> nativeQuery = session.createNativeQuery(sql, Player.class);
            return nativeQuery.list();
        }
    }

    @Override
    public int getAllCount() {

        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(*) FROM Player";
            Query<Player> query = session.createQuery(hql, Player.class);
            return query.executeUpdate();
        }
    }

    @Override
    public Player save(Player player) {

        try (Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            session.persist(player);
            transaction.commit();
            return player;
        }
    }

    @Override
    public Player update(Player player) {

        try (Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            session.persist(player);
            transaction.commit();
            return player;
        }
    }

    @Override
    public Optional<Player> findById(long id) {

        try (Session session = sessionFactory.openSession()){
            Player player = session.get(Player.class, id);
            return Optional.of(player);
        }
    }

    @Override
    public void delete(Player player) {

        try (Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            session.remove(player);
            transaction.commit();
        }
    }

    @PreDestroy
    public void beforeStop() {
        sessionFactory.close();
    }
}
