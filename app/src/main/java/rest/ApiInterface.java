package rest;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface ApiInterface {

    @POST("{string}")
    Call<String> setGenericRequestRaw(@Path(value = "string", encoded = true) String string);

    @Multipart
    @POST("/UploadPics/upload.php")
    Call<String> postImage(@Part MultipartBody.Part image, @Part("name") RequestBody name);
}