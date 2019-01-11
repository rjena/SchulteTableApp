package ru.nstu.schultetable.rest;

public class ApiUtils {
    private ApiUtils() {}

    private static final String BASE_URL = "http://schultetable.pythonanywhere.com/";

    public static STInterface getAPIService() {
        return ApiClient.getClient(BASE_URL).create(STInterface.class);
    }
}
