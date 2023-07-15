import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Handler implements RequestHandler {

    @Override
    public Object handleRequest(Object o, Context context) {
        LambdaLogger lambdaLogger = context.getLogger();
        lambdaLogger.log("OBJETO: " + o.toString());
        lambdaLogger.log("CONEXT: " + context);
        return o;
    }
}