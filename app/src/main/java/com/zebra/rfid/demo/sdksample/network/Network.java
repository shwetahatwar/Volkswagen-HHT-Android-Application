package com.zebra.rfid.demo.sdksample.network;

import android.content.Context;
import android.widget.ImageView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zebra.rfid.demo.sdksample.utils.Utils;

public class Network {

    public interface APIResultCallback {
        void onSuccess(JsonElement result);
        void onFailure();
    }

    Context context;
    APIResultCallback apiResultCallback;
    String url;
    String method = "GET";
    JsonObject body = new JsonObject();

    private Network() {

    }

//    public static void loadImage(Context context, String imageName, ImageView imageView) {
//        String url = Utils.getImageUrl(imageName);
//        Ion.with(context)
//            .load(url)
//            .withBitmap()
//            .intoImageView(imageView);
//    }

    public static Network with(Context context, String api) {
        Network network = new Network();
        network.context = context;
        network.url = Utils.getUrl(api);
        return network;
    }

    public Network method(String method) {
        this.method = method;
        return this;
    }

    public Network body(JsonObject object) {
        this.body = object;
        return this;
    }

    public void result(APIResultCallback result) {
        this.apiResultCallback = result;

        if (method == "GET") {
            Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result.get("status").getAsString().equals("success")) {
                            apiResultCallback.onSuccess(result.get("data"));
                        } else {
                            apiResultCallback.onFailure();
                        }
                    }
                });
        } else {
            Ion.with(context)
                .load(method, url)
                .setJsonObjectBody(body)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result.get("status").getAsString().equals("success")) {
                            apiResultCallback.onSuccess(result.get("data"));
                        } else {
                            apiResultCallback.onFailure();
                        }
                    }
                });
        }

    }
}
