package hudson.plugins.build_publisher;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.plugins.emailext.EmailType;
import hudson.plugins.emailext.ExtendedEmailPublisher;
import hudson.plugins.emailext.plugins.EmailContent;
import hudson.tasks.test.AbstractTestResultAction;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * An EmailContent for failing tests. Only shows tests that have failed.
 * 
 * @author Max Rydahl Andersen
 */
public class TestInfoContent implements EmailContent {
   
   private static final String TOKEN = "TEST_INFO";
   
   public String getToken() {
       return TOKEN;
   }
   
   public List<String> getArguments() {
       return Collections.emptyList();
   }
   
   public String getHelpText() {
       return "Displays summarized test info.";
   }
   
   public <P extends AbstractProject<P, B>, B extends AbstractBuild<P, B>>
   String getContent(AbstractBuild<P, B> build, ExtendedEmailPublisher publisher,
           EmailType emailType, Map<String, ?> args) {
       
       StringBuffer buffer = new StringBuffer();
       AbstractTestResultAction<?> testResult = build.getTestResultAction();
       
       if (null == testResult) {
           return "No tests ran.";
       }
       
       return testResult.getTotalCount() + " tests " + testResult.getFailCount() + " failure(s) " + testResult.getFailureDiffString();     
                       
   }
       
   public boolean hasNestedContent() {
       return false;
   }
   
}
