package com.vmware.sdugar.model.test;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.testng.annotations.Test;

/**
 * Created by sourabhdugar on 6/15/16.
 */
@Test
public class HtmlSanitizerTest {
    private static final PolicyFactory policy = new HtmlPolicyBuilder()
            .allowStandardUrlProtocols()
            .allowStyling()
            .requireRelNofollowOnLinks()
            .toFactory()
            .and(Sanitizers.BLOCKS)
            .and(Sanitizers.LINKS)
            .and(Sanitizers.STYLES)
            .and(Sanitizers.FORMATTING)
            .and(new HtmlPolicyBuilder().allowElements("style").allowTextIn("style").toFactory());
    @Test
    public void testBasicHtmlSanitization() {
        String sb = "<p style=\"Font: Helvetica\"/>";

        System.out.println(policy.sanitize(sb));
    }
}
