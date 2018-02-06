package org.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.entity.Game;

@Stateless
@Transactional
public class GameResource {

    @PersistenceContext
    EntityManager em;

    public Game findById(String id) {
        return this.em.find(Game.class, id);
    }

    public List<Game> findAll() {
        Query q = this.em.createQuery("SELECT g FROM Game g");
        q.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        return q.getResultList();
    }

    public Game save(Game p) {
        return this.em.merge(p);
    }
}
