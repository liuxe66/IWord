package com.liuxe.iword.http.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Liuxe on 2022/3/19 21:38
 * desc :
 */
public class CheckGsonConverterFactory extends Converter.Factory {

    public static CheckGsonConverterFactory create() {
        return create(new Gson());
    }


    public static CheckGsonConverterFactory create(Gson gson) {
        return new CheckGsonConverterFactory(gson);
    }

    private final Gson gson;

    private CheckGsonConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        //返回自定义的响应体转换器
        return new CheckGsonResponseBodyConverter<>(gson,adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        //返回自定义的请求转换器
        return new CheckGsonRequestBodyConverter<>(gson, adapter);
    }
}
