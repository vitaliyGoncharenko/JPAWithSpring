package goncharenko.GVV.service;

import goncharenko.GVV.entity.Contact;

import java.util.List;

/**
 * Created by Vitaliy on 09.02.2016.
 */
public interface ContactService {
    List<Contact> findAll();
    List<Contact> findAllWithDetail();
    Contact findById(Long id);
    Contact save(Contact contact);
    void delete(Contact contact);
}
