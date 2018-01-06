package com.github.eunsiljo.kakaoimagesearcher.api.utils;

import android.content.Context;
import android.widget.Toast;

import com.github.eunsiljo.kakaoimagesearcher.R;
import com.github.eunsiljo.kakaoimagesearcher.api.data.ErrorVO;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by EunsilJo on 2017. 8. 11..
 */

public class APIUtils {

    public static final String TAG = APIUtils.class.getSimpleName();

    /**
     * Get api random code
     *
     * @return random code
     */
    public static String getAPIRandomCode() {
        return getRandomCode(20);
    }

    /**
     * Get random code (english and number)
     *
     * @param length random code length
     * @return random code
     */
    public static String getRandomCode(int length) {
        Random rnd = new Random();
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < length; i++) {
            if (rnd.nextBoolean()) {
                buf.append((char) (rnd.nextInt(26) + 97));
            } else {
                buf.append((rnd.nextInt(10)));
            }
        }

        return String.valueOf(buf);
    }

    /**
     * Handling api request failure
     */
    public static void onError(Context context, Object error) {
        String message;
        if(error instanceof ErrorVO){
            message = ((ErrorVO) error).getMessage();
        }else{
            message = context.getString(R.string.toast_common_network_fail);
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Parse ErrorVO
     * @param retrofit Retrofit
     * @param response ResponseBody
     * @return ErrorVO
     */
    public static ErrorVO parseError(Retrofit retrofit, ResponseBody response) {
        Converter<ResponseBody, ErrorVO> converter =
                retrofit.responseBodyConverter(ErrorVO.class, new Annotation[0]);

        ErrorVO error;
        try {
            error = converter.convert(response);
        } catch (IOException e) {
            return new ErrorVO();
        }

        return error;
    }

    // =============================================================================
    // JSON
    // =============================================================================

    /**
     * Make a vo to json string
     *
     * @param json  json string
     * @param clazz convert class
     * @return vo object
     */
    public static Object toJsonString(String json, Class clazz) {
        Gson gson = new Gson();
        TypeAdapter adapter = gson.getAdapter(clazz);
        try {
            return adapter.fromJson(json);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Make a vo to json object
     *
     * @param object json object
     * @param clazz  convert class
     * @return vo object
     */
    public static Object toJsonObject(Object object, Class clazz) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return gson.fromJson(json, clazz);
    }

    public static ArrayList toJsonArray(Object object, Type typeOfT) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return gson.fromJson(json, typeOfT);
    }

    public static String toJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
