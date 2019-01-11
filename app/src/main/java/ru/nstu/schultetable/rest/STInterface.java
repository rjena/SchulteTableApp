package ru.nstu.schultetable.rest;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.nstu.schultetable.models.TestModel;
import ru.nstu.schultetable.models.UserModel;

public interface STInterface {
    // войти в профиль
    @POST("/api/authenticate")
    @FormUrlEncoded
    Call<UserModel> signIn(
            @Field("usernameOrEmail") String usernameOrEmail,
            @Field("password") String password
    );

    // зарегистрироваться
    @POST("/api/users")
    @FormUrlEncoded
    Call<UserModel> signUp(
            @Field("login") String login,
            @Field("email") String email,
            @Field("name") String name,
            @Field("bday") String bday,
            @Field("password") String password,
            @Field("tokenToConfirmEmail") String tokenToConfirmEmail,
            @Field("tokenToResetPassword") String tokenToResetPassword
    );

    // добавить результат тестирования
    @POST("/api/tests/")
    @FormUrlEncoded
    Call<TestModel> addTest(
            @Field("date") String date,
            @Field("user_id") String userId,
            @Field("age") int age,
            @Field("stWE") String stWE,
            @Field("stWU") String stWU,
            @Field("stPS") String stPS
    );

    // получить список пользователей
    @GET("/api/users")
    Call<UserModel[]> getUsers(@Query("format") String format);

    // получить список результатов тестирований
    @GET("/api/tests")
    Call<TestModel[]> getTests(@Query("format") String format);

    // получить тест по id
    @GET("/api/Tests/{TestId}")
    Call<TestModel> getTestById(
            @Path("TestId") String testId,
            @Query("format") String format
    );

    // редактировать информацию о пользователке
    @PUT("api/user/{userId}")
    @FormUrlEncoded
    Call<UserModel> changeUsersData(
            @Path("userId") String userId,
            @Field("id") String id,
            @Field("login") String login,
            @Field("email") String email,
            @Field("name") String name,
            @Field("bday") String bday,
            @Field("password") String password,
            @Field("tokenToConfirmEmail") String tokenToConfirmEmail,
            @Field("tokenToResetPassword") String tokenToResetPassword
    );

    // Получение пользователя по id
    @GET("/api/user/{userId}")
    Call<UserModel> getUserByID(
            @Path("userId") String userId,
            @Query("format") String format
    );

    /*@POST("/api/send_mail_to_confirm")
    @FormUrlEncoded
    Call<SuccessAndMessageModel> sendMailToConfirm(
            @Field("username") String username,
            @Field("email") String email
    );

    @POST("/api/resetpassword")
    @FormUrlEncoded
    Call<SuccessAndMessageModel> resetPasswordSendEmail(
            @Field("username") String username,
            @Field("email") String email
    );

    @POST("/api/restore")
    @FormUrlEncoded
    Call<SuccessAndMessageModel> restorePassword(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );*/
}
