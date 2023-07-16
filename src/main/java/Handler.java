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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import static com.amazonaws.regions.Regions.US_EAST_1;


public class Handler implements RequestHandler<SNSEvent, ArrayList<Object>> {

    final String SUBJECT = "Bem vindo ao Seu imóvel aqui!";

    @Override
    public ArrayList<Object> handleRequest(SNSEvent event, Context context) {
        String json = event.getRecords().get(0).getSNS().getMessage();
        JsonObject object = new Gson().fromJson(json, JsonObject.class);

        AmazonSimpleEmailService client = createClient();
        SendEmailRequest request = createRequest(object.get("email").getAsString(), object.get("usuario").getAsString());
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

    SendEmailRequest createRequest(String destination, String user) {
        return new SendEmailRequest().withDestination(createDestination(destination))
                .withMessage(createMessage(user)).withSource("matheus.2015jf@hotmail.com");
    }

    Destination createDestination(String destination) {
        return new Destination().withToAddresses(destination);
    }

    Message createMessage(String user) {
        return new Message().withBody(new Body().withHtml(
                createContent(createBody(user)))).withSubject(createContent(SUBJECT));
    }

    Content createContent(String content) {
        return new Content().withCharset("UTF-8").withData(content);
    }

    String createBody(String user) {
        return "<html>" +
                "" +
                "<head>" +
                "    <style>" +
                "        .conteudo-primeira-div {" +
                "            color: white;" +
                "            text-align: center;" +
                "            width: 100%;" +
                "        }" +
                "" +
                "        .blocos {" +
                "            font-size: 16px;" +
                "            margin-bottom: 20px;" +
                "        }" +
                "" +
                "        .image {" +
                "            width: 240px;" +
                "        }" +
                "" +
                "        a {" +
                "            color: white !important;" +
                "            text-decoration: none;" +
                "        }" +
                "" +
                "        .botao {" +
                "            background-color: rgb(24, 33, 155);" +
                "            border-radius: 28px;" +
                "            display: inline-block;" +
                "            color: #ffffff;" +
                "            font-family: Arial;" +
                "            font-size: 17px;" +
                "            padding: 10px 20px;" +
                "            border: none;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "" +
                "<body" +
                "    style=\"background-color: white !important;color: black;width: 90%;font-family: 'Nunito Sans', sans-serif; margin: 0 auto;\">" +
                "    <div style=\"background-color: rgb(24, 33, 155) !important;display: flex;\">" +
                "        <div class=\"conteudo-primeira-div\">" +
                "            <h1>SEJA BEM VINDO!</h1>" +
                "        </div>" +
                "    </div>" +
                "" +
                "    <div style=\"padding: 15px\">" +
                "        <h2>Prezado usuário, " + user + "</h2>" +
                "" +
                "        <div class=\"blocos\">" +
                "            Seja bem-vindo ao nosso sistema de anúncio de imóveis! Estamos empolgados por você ter se juntado à nossa" +
                "            comunidade de proprietários e locatários em busca do lugar perfeito para morar." +
                "        </div>" +
                "" +
                "        <div class=\"blocos\">" +
                "            Nosso sistema oferece uma plataforma abrangente para encontrar, anunciar e explorar uma ampla variedade de" +
                "            imóveis disponíveis. Seja você um proprietário que deseja anunciar seu imóvel ou um locatário em busca de" +
                "            uma nova casa, estamos aqui para ajudar." +
                "        </div>" +
                "" +
                "        <div class=\"blocos\">" +
                "            <button class=\"botao\"><a href=\"https://seuimovelaqui.vercel.app/\" target=\"_blank\">Acessar" +
                "                    plataforma</a></button>" +
                "        </div>" +
                "" +
                "        <div class=\"blocos\">" +
                "            <div style=\"text-align: center;\">" +
                "                <img class=\"image\" src=\"https://seuimovelaqui.vercel.app/assets/foto/Logo.png\">" +
                "            </div>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "" +
                "</html>";
    }
}