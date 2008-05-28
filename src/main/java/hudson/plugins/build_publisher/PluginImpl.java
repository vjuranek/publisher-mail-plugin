package hudson.plugins.build_publisher;

import hudson.Plugin;
import hudson.model.Hudson;
import hudson.plugins.emailext.ExtendedEmailPublisher;


/**
 * Entry point of a plugin.
 * @plugin
 */
public class PluginImpl extends Plugin {

    public void start() throws Exception {
        
        ExtendedEmailPublisher.addEmailTriggerType(PublishedTrigger.DESCRIPTOR);
        ExtendedEmailPublisher.addEmailContentType(new BuildPublisherContent());
        
        BuildPublisherPostAction.POST_ACTIONS.add(MailPostAction.DESCRIPTOR);
    }
}
