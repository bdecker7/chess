package Handler;


import Service.RegisterResult;
import com.google.gson.Gson;

public class RegisterHandler {
    RegisterRequest request = (LoginRequest) Gson.fromJson(reqData, LoginRequest.class);

    LoginService service = new LoginService();
    LoginResult result = service.login(request);

    return gson.toJson(result);

    public RegisterResult handleRequest()

}
