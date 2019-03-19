package app.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NetUtils {
    private static final byte[] LOCKER = new byte[0];
    private static NetUtils mInstance;
    private OkHttpClient mOkHttpClient;

    private NetUtils() {
        OkHttpClient.Builder ClientBuilder = new OkHttpClient.Builder();
        ClientBuilder.readTimeout(20, TimeUnit.SECONDS);//读取超时
        ClientBuilder.connectTimeout(10, TimeUnit.SECONDS);//连接超时
        ClientBuilder.writeTimeout(60, TimeUnit.SECONDS);//写入超时
        // 支持HTTPS请求，跳过证书验证
        ClientBuilder.sslSocketFactory(createSSLSocketFactory());
        ClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        mOkHttpClient=ClientBuilder.build();
    }

    /**
     * 单例模式获取NetUtils
     * @return
     */
    public static NetUtils getInstance() {
        if (mInstance == null) {
            synchronized (LOCKER) {
                if (mInstance == null) {
                    mInstance = new NetUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * get请求，同步方式，获取网络数据，是在主线程中执行的，需要新起线程，将其放到子线程中执行
     * @param url
     * @return
     */
    public Response getDataSynFromNet(String url) {
        //1 构造Request
        Request.Builder builder = new Request.Builder();
        Request request=builder.get().url(url).build();
        //2 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //3 执行Call，得到response
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
    /**
     * post请求，同步方式，提交数据，是在主线程中执行的，需要新起线程，将其放到子线程中执行
     * @param url
     * @param bodyParams
     * @return
     */
    public Response postDataSynToNet(String url,Map<String,String> bodyParams) {
        //1构造RequestBody
        RequestBody body=setRequestBody(bodyParams);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();
        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call，得到response
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public interface myNetCall{
        public void success(String dataJson);

        public void failed(String err);
    }

    /**
     * get请求，异步方式，获取网络数据，是在子线程中执行的，需要切换到主线程才能更新UI
     * @param url
     * @param myNetCall
     * @return
     */
    public void getDataAsynFromNet(String url, final myNetCall myNetCall) {
        //1 构造Request
        Request.Builder builder = new Request.Builder();
        Request request=builder.get().url(url).build();
        //2 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //3 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(response.body().string());
            }
        });
    }

    /**
     * post请求，异步方式，提交数据，是在子线程中执行的，需要切换到主线程才能更新UI
     * @param url
     * @param bodyParams
     * @param myNetCall
     */
    public void postDataAsynToNet(String url, Map<String,String> bodyParams, final myNetCall myNetCall) {
        //1构造RequestBody
        RequestBody body=setRequestBody(bodyParams);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();
        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(e.getMessage());
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(response.body().string());
            }
        });
    }

    /**
     * post请求，异步方式，提交数据，是在子线程中执行的，需要切换到主线程才能更新UI
     * @param url
     * @param json
     * @param myNetCall
     */
    public void postDataAsynToNet(String url, String json, final myNetCall myNetCall) {
        //1构造RequestBody
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();
        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(e.getMessage());
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(response.body().string());
            }
        });
    }

    /**
     * post请求，异步方式，上传文件
     * @param url
     * @param filename
     * @param filepath
     * @param json
     * @param myNetCall
     */
    public void postUpdateFile(String  url, String filename, String filepath, String  json, final myNetCall myNetCall){
        File file = new File(filepath);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("filename", filename)
//                .addFormDataPart("file", filename, fileBody)
//                .build();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("app", "com.zzz.myapp")
                .addFormDataPart("json", json)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"mFile\"; filename=\"" + filename + "\""), fileBody)
                .build();

        Request request = new Request.Builder().url(url).post(requestBody).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(response.body().string());
            }
        });
    }

    /**
     * post的请求参数，构造RequestBody
     * @param BodyParams
     * @return
     */
    private RequestBody setRequestBody(Map<String, String> BodyParams){
         RequestBody body=null;
         okhttp3.FormBody.Builder formEncodingBuilder=new okhttp3.FormBody.Builder();
         if(BodyParams != null){
             Iterator<String> iterator = BodyParams.keySet().iterator();
             String key = "";
             while (iterator.hasNext()) {
                 key = iterator.next().toString();
                 formEncodingBuilder.add(key, BodyParams.get(key));
                 Log.d("post http", "post_Params==="+key+"===="+BodyParams.get(key));
             }
         }
         body=formEncodingBuilder.build();
         return body;
     }


    /**
     * 判断网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) { }
        else { //如果仅仅是用来判断网络连接 //则可以使用cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     * @return
     */
    public SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) { }
        return ssfFactory;
    }
    /**
     * 用于信任所有证书
     */
    class TrustAllCerts implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
