package goncharenko.GVV.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Vitaliy on 12.02.2016.
 */
@Service("contactSummaryUntype")
public class ContactSummaryUntypeImpl {
    private static Logger LOGGER = LoggerFactory.getLogger(ContactSummaryUntypeImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    public void displayAllContactSummary(){
        List result = em.createQuery("select c.firstName, c.lastName, t.telNumber from Contact c " +
                "left join c.contactTelDetails t  where t.telType='Home'")
                .getResultList();
        int count = 0;
        for(Iterator i = result.iterator(); i.hasNext();){
            Object[] values = (Object[])i.next();
            LOGGER.info(++count + ": " + values[0] + ", "+ values[1] + ", " + values[2]);
        }
    }
}
