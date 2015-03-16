package easylearnServices.Rest.Endpoint;
import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;
import easylearnServices.Core.Common.EasylearnService;
import easylearnServices.Core.Common.IEasylearnService;

import javax.ws.rs.core.Context;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by patrickreichelt on 24/02/15.
 */
public class EasyLearnApplication extends Application {
    private final Set<Object> singletons = new HashSet<Object>();
    private final Set<Class<?>> resourcesToRegister = new HashSet<Class<?>>();
    private final IEasylearnService _serviceContext;

    public EasyLearnApplication() {

        // create a new ServiceContext
        _serviceContext = new EasylearnService();

        // make the service accessible for Context and create the ServiceProvider
        SingletonTypeInjectableProvider<Context, IEasylearnService> ServiceProvider = new SingletonTypeInjectableProvider<Context, IEasylearnService>(IEasylearnService.class, _serviceContext) {
        };
        getSingletons().add(ServiceProvider);

        // add the resources to the service
        getClasses().add(CardStackEndpoint.class);
        getClasses().add(AuthorEndpoint.class);
    }

    @Override
    public final Set<Class<?>> getClasses() { return resourcesToRegister; }

    @Override
    public final Set<Object> getSingletons() {
        return singletons;
    }
}
