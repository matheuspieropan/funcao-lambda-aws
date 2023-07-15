import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;

import java.util.ArrayList;


public class Handler implements RequestHandler<SNSEvent, ArrayList<Object>> {

    static final String HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
            + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
            + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>"
            + "AWS SDK for Java</a>";

    static final String TEXTBODY = "This email was sent through Amazon SES "
            + "using the AWS SDK for Java.";

    static final String SUBJECT = "Amazon SES test (AWS SDK for Java)";

    @Override
    public ArrayList<Object> handleRequest(SNSEvent event, Context context) {
        String destinatario = event.getRecords().get(0).getSNS().getMessage();
        try {
            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
            SendEmailRequest request = new SendEmailRequest().withDestination(new Destination()
                    .withToAddresses(destinatario)).withMessage(new Message().withBody(new Body()
                    .withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY)).withText(new Content()
                            .withCharset("UTF-8").withData(TEXTBODY))).withSubject(new Content()
                    .withCharset("UTF-8").withData(SUBJECT))).withSource("matheus.2015jf@hotmail.com");
            SendEmailResult sendEmailResult = client.sendEmail(request);
            context.getLogger().log(sendEmailResult.toString());
        } catch (Exception ex) {
            context.getLogger().log(ex.getMessage());
        }
        return null;
    }
}