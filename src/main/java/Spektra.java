import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Spektra {
    private String publicKey, secretKey;

    public Spektra(String publicKey, String secretKey){
        this.publicKey = publicKey;
        this.secretKey = secretKey;
    }

    private static String API_URL = "https://api-test.spektra.co";
    private static String OAUTH_URL = API_URL + "/oauth/token?grant_type=client_credentials";
    private static String API_CHECKOUT_URL = API_URL + "/api/v1/checkout/initiate";
    private static String CHECKOUT_URL = "https://demo-checkout.spektra.co/";
    private static final String SUCCESS_MESSAGE = "Request Processed Successfully";
    private static final String FAIL_MESSAGE = "Failed To Process Request";
    private static final String UNAUTHORIZED = "Unauthorized";
    private static final String SUCCESS_STATUS = "00";
    private static final String FAILURE_STATUS = "01";

    private static Map<String, Object> success(Map<String, Object> map) {
        map.put("message", SUCCESS_MESSAGE);
        map.put("status", SUCCESS_STATUS);
        return map;
    }

    private static Map<String, Object> failure(Map<String, Object> map) {
        map.put("message", FAIL_MESSAGE);
        map.put("status", FAILURE_STATUS);
        return map;
    }

    private static Map<String, Object> unauthorized(Map<String, Object> map) {
        map.put("message", UNAUTHORIZED);
        map.put("status", FAILURE_STATUS);
        return map;
    }

    private String authenticate() throws IOException {
        String appKeySecret = publicKey + ":" + secretKey;
        byte[] bytes = appKeySecret.getBytes("ISO-8859-1");
        String encoded = Base64.getEncoder().encodeToString(bytes);

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(OAUTH_URL)
                .post(body)
                .addHeader("Authorization", "Basic " + encoded)
                .addHeader("cache-control", "no-cache")
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            JSONObject jsonObject = new JSONObject(response.body().string());
            return jsonObject.getString("access_token");
        } else return null;
    }

    public Map<String, Object> checkout(Checkout checkout) throws IOException {
        Map<String, Object> map = new HashMap<>();

        String accessToken = authenticate();
        if (Objects.isNull(accessToken)){
            return unauthorized(map);
        }

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.valueOf(new JSONObject(checkout)));
        Request request = new Request.Builder()
                .url(API_CHECKOUT_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("cache-control", "no-cache")
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            JSONObject jsonObject = new JSONObject(response.body().string());
            map.put("checkoutUrl", CHECKOUT_URL + jsonObject.getString("checkoutID"));
            return success(map);
        } else {
            return failure(map);
        }
    }
}
