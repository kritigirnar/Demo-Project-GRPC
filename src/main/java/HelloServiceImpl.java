import com.example.grpc.Hello;
import com.example.grpc.HelloGrpcServiceGrpc;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import jakarta.inject.Singleton;

@GrpcService
@Singleton
public class HelloServiceImpl extends HelloGrpcServiceGrpc.HelloGrpcServiceImplBase {

    @Override
    public void sayHello(Hello.HelloRequest request, StreamObserver<Hello.HelloResponse> responseObserver) {

        String clientName = request.getClientName();
        String soapResponse = callSoapService(clientName);

        Hello.HelloResponse response = Hello.HelloResponse.newBuilder()
                .setMessage("Hello " + soapResponse)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private String callSoapService(String clientName) {
       String soapEndpoint = "http://localhost:8080/HelloSoapService";  // Your SOAP service URL
        String soapRequest = createSoapRequest(clientName);
        return "Welcome " + clientName;
    }

    private String createSoapRequest(String clientName) {
        return "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2001/12/soap-envelope\" "
                + "SOAP-ENV:encodingStyle=\"http://www.w3.org/2001/12/soap-encoding\">"
                + "<SOAP-ENV:Body xmlns:m=\"http://www.flydubai.com/HelloSoap\">"
                + "<m:HelloSoap><m:ClientName>" + clientName + "</m:ClientName></m:HelloSoap>"
                + "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
    }
}
