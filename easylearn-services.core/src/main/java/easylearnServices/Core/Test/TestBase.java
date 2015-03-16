package easylearnServices.Core.Test;

import org.junit.After;
import org.junit.Before;

/**
 * Created by patrickreichelt on 24/02/15.
 */
public abstract class TestBase
{

    protected EasylearnServiceMock _serviceContext;

    public EasylearnServiceMock getContext() {
        return _serviceContext;
    };

    /**
     * prepare the variables before testing
     */
    @Before
    public void buildService(){

        _serviceContext = new EasylearnServiceMock();
        _serviceContext.createSampleData();

    }

    /**
     * delete the test data base after testing
     */
    @After
    public void tearDownService() {

    }
}
