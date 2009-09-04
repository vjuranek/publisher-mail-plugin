package hudson.plugins.build_publisher;

import hudson.Plugin;
import hudson.plugins.emailext.ExtendedEmailPublisher;
import hudson.plugins.emailext.plugins.ContentBuilder;


/**
 * Entry point of the publisher-mail plugin.
 * 
 * @plugin
 * @author dvrzalik
 */
public class PluginImpl extends Plugin {

    public void start() throws Exception {
        
        ExtendedEmailPublisher.addEmailTriggerType(PublishedTrigger.DESCRIPTOR);
        ContentBuilder.addEmailContentType(new BuildPublisherContent());
        ContentBuilder.addEmailContentType(new TestInfoContent());
        
        BuildPublisherPostAction.POST_ACTIONS.add(MailPostAction.DESCRIPTOR);
    }
}
