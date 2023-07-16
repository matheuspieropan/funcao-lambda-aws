import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import java.util.ArrayList;

import static com.amazonaws.regions.Regions.*;


public class Handler implements RequestHandler<SNSEvent, ArrayList<Object>> {

    final String SUBJECT = "Bem vindo ao Seu imóvel aqui!";

    final String HTMLBODY = "<h1>É uma honra ter você aqui</h1>"
            + "<p>Acesso sua conta clicando <a href='https://seuimovelaqui.vercel.app/'> aqui </a>";

    @Override
    public ArrayList<Object> handleRequest(SNSEvent event, Context context) {

        AmazonSimpleEmailService client = createClient();
        SendEmailRequest request = createRequest(event.getRecords().get(0).getSNS().getMessage());

        try {
            client.sendEmail(request);
        } catch (Exception ex) {
            context.getLogger().log(ex.getMessage());
        }
        return null;
    }

    AmazonSimpleEmailService createClient() {
        return AmazonSimpleEmailServiceClientBuilder.standard().withRegion(US_EAST_1).build();
    }

    SendEmailRequest createRequest(String destination) {
        return new SendEmailRequest().withDestination(createDestination(destination))
                .withMessage(createMessage()).withSource("matheus.2015jf@hotmail.com");
    }

    Destination createDestination(String destination) {
        return new Destination().withToAddresses(destination);
    }

    Message createMessage() {
        return new Message().withBody(new Body().withHtml(createContent(HTMLBODY)))
                .withSubject(createContent(SUBJECT));
    }

    Content createContent(String content) {
        return new Content().withCharset("UTF-8").withData(content);
    }
}