/*
 * This is a class that implements looking up Gitorious paths from 
 * the database.
 * Currently it's quite error prone, but works. Kind of
 */
package org.gitorious.http;

import org.eclipse.jgit.http.server.resolver.*;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.RepositoryCache.FileKey;
import org.eclipse.jgit.util.FS;
import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.*;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;


class GitoriousResolver implements RepositoryResolver {
    private String repositoryRoot;
    private String permissionBaseUri;
    private static Logger logger = Logger.getLogger("org.gitorious");
    static {
        logger.setLevel(org.apache.log4j.Level.INFO);
        BasicConfigurator.configure();
    }

    public Repository open(HttpServletRequest req, String name)
        throws RepositoryNotFoundException, ServiceNotAuthorizedException, 
               ServiceNotEnabledException {
        logger.info("************ Looking for repository at " + name + "**************");
        String realName = lookupName(name);
        try {
            File gitDir = new File(repositoryRoot, realName);
            Repository db = RepositoryCache.open(FileKey.lenient(gitDir, FS.DETECTED), true);
            return db;
        } catch (IOException io) {
            logger.error("ERROR: " + io.getMessage());
            throw new RepositoryNotFoundException(name);
        }
    }

    public void setRepositoryRoot(String repositoryRoot) {
        this.repositoryRoot = repositoryRoot;
    }

    public void setPermissionBaseUri(String uri) {
        this.permissionBaseUri = uri;
    }

    public String lookupName(String in){
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            String path = in.split("\\.git")[0];

            // Should be /project/repository/config

            String url = permissionBaseUri + path + "/config";

            HttpGet httpGet = new HttpGet(url);

            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            
            InputStream is = entity.getContent();

            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader buff = new BufferedReader(reader);

            String line = null;
            String result = null;
            
            while ((line = buff.readLine()) != null) {
                if (line.indexOf("real_path") != -1) {
                    result = line.split("real_path:")[1];
                }
            }
            
            is.close();
            String fullPath =  result;
            logger.info("Path is " + fullPath);
            return fullPath;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return e.getMessage();
        }
    }
}