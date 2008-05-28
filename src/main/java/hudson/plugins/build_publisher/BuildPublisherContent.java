package hudson.plugins.build_publisher;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Build;
import hudson.model.Project;
import hudson.plugins.emailext.EmailType;
import hudson.plugins.emailext.plugins.EmailContent;
import hudson.tasks.Publisher;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * $PUBLIC_HUDSON_URL = url of public hudson instance
 * 
 * @author dvrzalik
 */
public class BuildPublisherContent implements EmailContent {

    public String getToken() {
       return "PUBLIC_HUDSON_URL";
    }

    public <P extends AbstractProject<P, B>, B extends AbstractBuild<P, B>> String getContent(AbstractBuild<P, B> build, EmailType type) {
        Publisher publisher;
        try {
            publisher = MailPostAction.getPublisher(build.getProject(), BuildPublisher.DESCRIPTOR);
            if(publisher instanceof BuildPublisher) {
            HudsonInstance instance = ((BuildPublisher)publisher).getPublicHudsonInstance();
            if(instance != null) {
                return instance.getUrl();
            }
        }
        } catch (Exception ex) {
            Logger.getLogger(BuildPublisherContent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    public boolean hasNestedContent() {
        return false;
    }

    public String getHelpText() {
        return "Shows URL of public Hudson instance";
    }

}
