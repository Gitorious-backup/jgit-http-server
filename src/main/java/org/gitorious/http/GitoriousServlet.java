/*
 * Extending JGit's GitServlet to use a custom repository resolver 
 */
package org.gitorious.http;
import org.eclipse.jgit.http.server.*;
import javax.servlet.ServletException;
import org.eclipse.jgit.http.server.resolver.*;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ReceivePack;

public class GitoriousServlet extends GitServlet {
    public GitoriousServlet() {
    }

    public void init() throws ServletException {
        String repositoryRoot = getServletConfig().getInitParameter("repository_root");
        String permissionBaseUri = getServletConfig().getInitParameter("permission_base_uri");

        GitoriousResolver resolver = new GitoriousResolver();
        resolver.setRepositoryRoot(repositoryRoot);
        resolver.setPermissionBaseUri(permissionBaseUri);
        setRepositoryResolver(resolver);

        setReceivePackFactory(new GitoriousReceivePackFactory());
    }

    class GitoriousReceivePackFactory implements ReceivePackFactory {
        public ReceivePack create(HttpServletRequest req, Repository db) throws ServiceNotEnabledException, ServiceNotAuthorizedException {
            System.err.println("ACCESS DENIED for " + db.toString() + "/" + req.getRemoteUser());
            //throw new ServiceNotEnabledException();
            throw new ServiceNotAuthorizedException();
        }
    }
}