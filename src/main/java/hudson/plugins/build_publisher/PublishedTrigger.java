package hudson.plugins.build_publisher;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Build;
import hudson.model.BuildListener;
import hudson.model.Project;
import hudson.model.Result;
import hudson.model.User;
import hudson.plugins.emailext.EmailType;
import hudson.plugins.emailext.ExtendedEmailPublisher;
import hudson.plugins.emailext.plugins.EmailContent;
import hudson.plugins.emailext.plugins.EmailTrigger;
import hudson.plugins.emailext.plugins.EmailTriggerDescriptor;
import hudson.scm.ChangeLogSet.Entry;
import hudson.tasks.Mailer;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * A trigger for extended mail notification which sends mail upon build publication via the build-publisher plugin.
 * 
 * @author dvrzalik
 */
public class PublishedTrigger extends EmailTrigger {

    @Override
    public <P extends AbstractProject<P,B>,B extends AbstractBuild<P,B>> boolean trigger(B build) {
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

    public <P extends AbstractProject<P, B>, B extends AbstractBuild<P, B>> boolean sendMail(AbstractBuild build,ExtendedEmailPublisher publisher, BuildListener listener) {

        EmailType mailType = getEmail();
        if (mailType == null) {
            return false;
        }
        
        publisher.sendMail(mailType,build,listener);

        return true;
    }    

}
