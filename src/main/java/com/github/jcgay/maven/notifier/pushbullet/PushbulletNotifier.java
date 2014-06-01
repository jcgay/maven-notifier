package com.github.jcgay.maven.notifier.pushbullet;

import com.github.jcgay.maven.notifier.AbstractCustomEventSpy;
import com.squareup.mimecraft.FormEncoding;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.component.annotations.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Proxy;
import java.util.List;

@Component(role = com.github.jcgay.maven.notifier.Notifier.class, hint = "pushbullet")
public class PushbulletNotifier extends AbstractCustomEventSpy {

    @Override
    public void onEvent(MavenExecutionResult result) {
        super.onEvent(result);

        RequestBody body = buildNotification(buildTitle(result), buildNotificationMessage(result));
        if (body == null) {
            return;
        }

        send(body);
    }

    private void send(RequestBody body) {
        Request request = new Request.Builder()
                .url("https://api.pushbullet.com/v2/pushes")
                .post(body)
                .build();

        try {
            Response response = newHttpClient().newCall(request).execute();
            if (response.code() != 200) {
                logger.error(String.format(
                        "Pushbullet notification has failed, [%d] - [%s]%n%s",
                        response.code(),
                        response.message(),
                        response.body().string()
                ));
            }
        } catch (IOException e) {
            logger.error("Error while sending pushbullet notification.", e);
        }
    }

    private RequestBody buildNotification(String title, String message) {
        FormEncoding.Builder builder = new FormEncoding.Builder();
        if (configuration.getPushbulletDevice() != null) {
            builder.add("device_iden", configuration.getPushbulletDevice());
        }
        builder.add("type", "note")
                .add("title", title)
                .add("body", message);

        ByteArrayOutputStream data;
        try {
            data = new ByteArrayOutputStream();
            builder.build().writeBodyTo(data);
        } catch (IOException e) {
            logger.error("Can't build request body.", e);
            return null;
        }

        return RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), data.toByteArray());
    }

    private OkHttpClient newHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.setAuthenticator(new Authenticator() {
            @Override
            public Request authenticate(Proxy proxy, Response response) throws IOException {
                String credentials = Credentials.basic(configuration.getPushbulletKey(), "");
                return response.request().newBuilder().header("Authorization", credentials).build();
            }

            @Override
            public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
                return authenticate(proxy, response);
            }
        });
        return client;
    }

    @Override
    protected String buildTitle(MavenExecutionResult result) {
        return "Build " + getBuildStatus(result).message();
    }

    @Override
    protected String buildNotificationMessage(MavenExecutionResult result) {
        return super.buildTitle(result) + System.getProperty("line.separator") + super.buildNotificationMessage(result);
    }

    @Override
    public void onFailWithoutProject(List<Throwable> exceptions) {
        RequestBody body = buildNotification("Build Error", buildErrorDescription(exceptions));
        if (body == null) {
            return;
        }

        send(body);
    }
}
