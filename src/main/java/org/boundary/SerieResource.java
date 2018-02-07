package org.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.entity.Serie;

@Stateless
@Transactional
public class SerieResource {

    @PersistenceContext
    EntityManager em;

    public Serie findById(String id) {
        return this.em.find(Serie.class, id);
    }

    public List<Serie> findAll() {
        Query q = this.em.createQuery("SELECT s FROM Serie s");
        q.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        return q.getResultList();
    }

    public Serie save(Serie p) {
        return this.em.merge(p);
    }
}
