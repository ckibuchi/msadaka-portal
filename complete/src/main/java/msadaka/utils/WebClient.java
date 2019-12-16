package msadaka.utils;

import okhttp3.*;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class WebClient {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client =   getUnsafeOkHttpClient();
    private static String bearer="";

    // private final HttpClient Client = new DefaultHttpClient();
    private static  String Content;
    private String ACTION;
    //JSONObject data = null;//new JSONObject();
    private String Error = null;


    public String mpesaendpoint,authurl,app_secret,app_key;



    int sizeData = 0;
    public WebClient()
    {}

    public WebClient(String mpesaendpoint,String authurl,String app_secret,String app_key)
    {
        this.mpesaendpoint=mpesaendpoint;
        this.authurl=authurl;
        this.app_secret=app_secret;
        this.app_key=app_key;

    }
    public String webRequest(String url,String data,String post_get,String params)
    {

        try{


            RequestBody body = RequestBody.create(JSON, data);

            Request request =null;
            if(post_get.equalsIgnoreCase("POST")) {
                request = new Request.Builder()
                        .addHeader("Content-Type", "application/json")
                        .url(url)

                        .post(body)
                        .build();
            }
            else
            {
                request = new Request.Builder()
                        .addHeader("Content-Type", "application/json")
                        .url(url)
                        .get()
                        .build();
            }
            Response response = client.newCall(request).execute();
            Content= response.body().string();

        }
        catch(Exception e)
        {

            e.printStackTrace();
            System.out.println("ERROR"+e.getMessage());
        }

        return Content;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    }).build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
