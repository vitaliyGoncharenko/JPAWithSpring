package goncharenko.GVV.service;

import goncharenko.GVV.entity.Contact;
import goncharenko.GVV.entity.Contact_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by Vitaliy on 12.02.2016.
 */
@Service("jpaContactService")
@Transactional
public class ContactServiceImpl implements ContactService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactServiceImpl.class);
    final static String ALL_CONTACT_NATIVE_QUERY =
            "select id, first_name, last_name, birth_date, version from contact";

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

    @Transactional(readOnly = true)
    public List<Contact> findAllByNativeQuery() {
        return em.createNativeQuery(ALL_CONTACT_NATIVE_QUERY, Contact.class).getResultList();
    }

    //using the mapping result set SQL
    @Transactional(readOnly = true)
    public List<Contact> findAllByNativeQuery2() {
        return em.createNativeQuery(ALL_CONTACT_NATIVE_QUERY, "contactResult").getResultList();
    }

    @Transactional(readOnly = true)
    public List<Contact> findByCriteriaQuery(String firstName, String lastName) {
        LOGGER.info("Finding contact for firstName: " + firstName
                + " and lastName: " + lastName);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contact> criteriaQuery = cb.createQuery(Contact.class);
        Root<Contact> contactRoot = criteriaQuery.from(Contact.class);
        contactRoot.fetch(Contact_.contactTelDetails, JoinType.LEFT);
        contactRoot.fetch(Contact_.hobbies, JoinType.LEFT);
        criteriaQuery.select(contactRoot).distinct(true);
        Predicate criteria = cb.conjunction();
        if (firstName != null) {
            Predicate р = cb.equal(contactRoot.get(Contact_.firstName), firstName);
            criteria = cb.and(criteria, р);
        }
        if (lastName != null) {
            Predicate р = cb.equal(contactRoot.get(Contact_.lastName),lastName);
            criteria = cb.and(criteria, р);
        }
        criteriaQuery.where(criteria);
        return em.createQuery(criteriaQuery).getResultList();
    }
}
