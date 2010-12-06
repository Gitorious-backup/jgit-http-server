import org.eclipse.jgit.http.server.*;

public class GitoriousServlet extends GitServlet {
    public GitoriousServlet() {
        GitoriousResolver resolver = new GitoriousResolver();
        setRepositoryResolver(resolver);
    }
}