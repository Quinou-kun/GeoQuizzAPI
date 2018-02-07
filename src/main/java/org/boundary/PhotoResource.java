package org.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.entity.Photo;

@Stateless
@Transactional
public class PhotoResource {

    @PersistenceContext
    EntityManager em;

    public Photo findById(String id) {
        return this.em.find(Photo.class, id);
    }

    public List<Photo> findAll() {
        Query q = this.em.createQuery("SELECT p FROM Photo p");
        q.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        return q.getResultList();
    }

    public Photo save(Photo p) {
        return this.em.merge(p);
    }
}
