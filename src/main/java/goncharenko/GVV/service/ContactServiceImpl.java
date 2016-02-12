package goncharenko.GVV.service;

import goncharenko.GVV.entity.Contact;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Vitaliy on 12.02.2016.
 */
@Service("jpaContactService")
//@Repository
@Transactional
public class ContactServiceImpl implements ContactService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactServiceImpl.class);

    @PersistenceContext
    private EntityManager em;


    @Transactional(readOnly = true)
    public List<Contact> findAll() {
        return em.createNamedQuery("Contact.findAll", Contact.class).getResultList();
    }

    @Transactional(readOnly = true)
    public List<Contact> findAllWithDetail() {
        return em.createNamedQuery("Contact.findAllWithDetail", Contact.class).getResultList();
    }

    @Transactional(readOnly = true)
    public Contact findById(Long id) {
//        return (Contact)sessionFactory.getCurrentSession().getNamedQuery("Contact.findByid").setParameter("id",id).uniqueResult();
        TypedQuery<Contact> query = em.createNamedQuery("Contact.findById", Contact.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Contact save(Contact contact) {
        if (contact.getId() == null) {
            LOGGER.info("Inserting new contact");
            em.persist(contact);
        } else {
            em.merge(contact);
            LOGGER.info("Updating existing contact");
        }
        LOGGER.info("Contact saved with id: " + contact.getId());
        return contact;
    }

    public void delete(Contact contact) {
        Contact mergedContact = em.merge(contact);
        em.remove(mergedContact);
        LOGGER.info("Contact with id: " + contact.getId() + " deleted successfully");
    }
}
