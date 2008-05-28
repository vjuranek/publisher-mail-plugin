package hudson.plugins.build_publisher;

import hudson.model.Hudson;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Build;
import hudson.model.Descriptor;
import hudson.model.StreamBuildListener;
import hudson.tasks.Publisher;
import hudson.plugins.emailext.ExtendedEmailPublisher;
import hudson.plugins.emailext.plugins.EmailTrigger;
import hudson.tasks.Publisher;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dvrzalik
 */
public class MailPostAction implements BuildPublisherPostAction {

    public void post(AbstractBuild build, HudsonInstance publicHudson) {
        
        AbstractProject project = build.getProject();
        try {
            
            Publisher result = getPublisher(project, ExtendedEmailPublisher.DESCRIPTOR);
            if(result instanceof ExtendedEmailPublisher) {
                ExtendedEmailPublisher publisher = (ExtendedEmailPublisher) result;
                for(EmailTrigger trigger: publisher.getConfiguredTriggers()) {
                    if(trigger instanceof PublishedTrigger) {
                        ((PublishedTrigger) trigger).<AbstractProject,AbstractBuild>sendMail(build, publisher,new StreamBuildListener(System.out));
                    }
                }
            }
            
        } catch (Exception ex) {
             Logger.getLogger(Hudson.class.getName()).log(Level.SEVERE, "Unable to find extended mail publisher", ex);
        }

    }

    public MailPostActionDescriptor getDescriptor() {
        return DESCRIPTOR;
    }
    public static final MailPostActionDescriptor DESCRIPTOR = new MailPostActionDescriptor();

    public static class MailPostActionDescriptor extends PostActionDescriptor {

        @Override
        public String getDisplayName() {
            return "Mail notification";
        }

        @Override
        public BuildPublisherPostAction newInstance() {
            return new MailPostAction();
        }
    }
    
    public static Publisher getPublisher(AbstractProject project, Descriptor<Publisher> descriptor) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method[] methods = project.getClass().getMethods();
        for(Method method: methods) {
            Class[] types = method.getParameterTypes();
            if(method.getName().equals("getPublisher") && (types.length == 1) && (types[0].getName().equals("hudson.model.Descriptor"))) {
                return (Publisher) method.invoke(project, descriptor);
            }
        }
        return null;
    }
}
