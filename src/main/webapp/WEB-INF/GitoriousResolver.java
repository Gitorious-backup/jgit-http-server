import org.eclipse.jgit.http.server.resolver;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;

class GitoriousResolver extends RepositoryResolver {
    public Repository open(HttpServletRequest req, String name)
        throws RepositoryNotFoundException, ServiceNotAuthorizedException, 
               ServiceNotEnabledException{
    }
}