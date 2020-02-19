package uk.co.asepstrath.bank;

import io.jooby.StatusCode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.*;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UnitTest {
    static OkHttpClient client = new OkHttpClient();


    @Test
    public void testJson() throws JSONException {
        Request req = new Request.Builder()
                .url("http://localhost:" + "8080"+"/account/json")
                .build();
        try (InputStream resourceAsStream = UnitTest.class.getResourceAsStream("/sum.json")) {
            String json = IOUtils.toString(resourceAsStream, "utf-8");
            try (Response rsp = client.newCall(req).execute()) {
                JSONAssert.assertEquals(json,rsp.body().toString(),true);
            }

        } catch (IOException e) {
        }

    }


}
