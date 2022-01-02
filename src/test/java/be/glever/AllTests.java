import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import be.glever.ant.util.ByteUtilTest;
import be.glever.ant.message.MessageRegistryTest;
import be.glever.ant.message.requestedresponse.CapabilitiesResponseMessageTest;

@RunWith(Suite.class)
@SuiteClasses({
    ByteUtilTest.class,
    MessageRegistryTest.class,
    CapabilitiesResponseMessageTest.class,
})
public class AllTests {}
