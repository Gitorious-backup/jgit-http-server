import org.eclipse.jgit.http.server.resolver.*;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.RepositoryCache.FileKey;
import org.eclipse.jgit.util.FS;
import java.io.File;
import java.io.IOException;

class GitoriousResolver implements RepositoryResolver {
    public Repository open(HttpServletRequest req, String name)
        throws RepositoryNotFoundException, ServiceNotAuthorizedException, 
               ServiceNotEnabledException {
        System.out.println("Looking for repository at " + name);
        try {
            File gitDir = new File("/tmp", name);
            Repository db = RepositoryCache.open(FileKey.lenient(gitDir, FS.DETECTED), true);
            return db;
        } catch (IOException io) {
            throw new RepositoryNotFoundException(name);
        }
    }
}