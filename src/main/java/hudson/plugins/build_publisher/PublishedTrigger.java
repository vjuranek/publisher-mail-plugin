package hudson.plugins.build_publisher;

import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.plugins.emailext.EmailType;
import hudson.plugins.emailext.ExtendedEmailPublisher;
import hudson.plugins.emailext.plugins.EmailTrigger;
import hudson.plugins.emailext.plugins.EmailTriggerDescriptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A trigger for extended mail notification which sends mail upon build publication via the build-publisher plugin.
 * 
 * @author dvrzalik
 */
public class PublishedTrigger extends EmailTrigger {

    @Override
    public boolean trigger(AbstractBuild<?,?> build){
    //public <P extends AbstractProject<P,B>,B extends AbstractBuild<P,B>> boolean trigger(B build) {
        //notification should be sent after the build is published
        return false;
    }

    @Override
    public EmailTriggerDescriptor getDescriptor() {
        return DESCRIPTOR;
    }
    public static DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    public static final class DescriptorImpl extends EmailTriggerDescriptor {

        @Override
        public String getTriggerName() {
            return "Published";
        }

        @Override
        public EmailTrigger newInstance() {
            return new PublishedTrigger();
        }

        @Override
        public String getHelpText() {
            return "Notification will be sent when this build is published via the build-publisher plugin";
        }
    }

    @Override
    public boolean getDefaultSendToDevs() {
        return false;
    }

    @Override
    public boolean getDefaultSendToList() {
        return false;
    }

    public <P extends AbstractProject<P, B>, B extends AbstractBuild<P, B>> boolean sendMail(B build,ExtendedEmailPublisher publisher, BuildListener listener) {

        EmailType mailType = getEmail();
        if (mailType == null) {
            return false;
        }
        
        // sendMail is private, better to solve it here than create a fork of email-ext
        Class params[] = {EmailType.class,AbstractBuild.class,BuildListener.class};
        try{
            Method m = publisher.getClass().getDeclaredMethod("sendMail", params);
            m.setAccessible(true);
            m.invoke(publisher,mailType,build,listener);
        } catch(NoSuchMethodException e) {
            e.printStackTrace();
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        } catch(InvocationTargetException e) {
            e.printStackTrace();
        }

        return true;
    }    

}
