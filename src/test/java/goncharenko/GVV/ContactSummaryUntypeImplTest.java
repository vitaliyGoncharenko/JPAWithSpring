package goncharenko.GVV;

import goncharenko.GVV.service.ContactSummaryUntypeImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Vitaliy on 12.02.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:app-context-annotation.xml"})
public class ContactSummaryUntypeImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactServiceImplTest.class);

    @Autowired
    private ContactSummaryUntypeImpl contactSummaryUntype;

    //for testFindAll()
    private static String firstNameExpected = "Chris";
    private static String lastNameExpected = "Schaefer";

    @Test
    public void displayAllContactSummary(){
        contactSummaryUntype.displayAllContactSummary();
    }
}
