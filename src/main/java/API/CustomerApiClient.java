package API;

import Model.Customer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import okhttp3.*;

public class CustomerApiClient extends ApiClientBase {
    private static final String BASE_URL = "http://localhost:8080/api/customers";
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Customer> getAll() throws IOException {
        Request request = new Request.Builder().url(BASE_URL).build();
        try (Response response = client.newCall(request).execute()) {
            return mapper.readValue(response.body().string(), new TypeReference<List<Customer>>(){});
        }
    }

    public String getNextId() throws IOException {
        Request request = new Request.Builder().url(BASE_URL + "/next-id").build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string().replaceAll("\"", "");
        }
    }

    public List<Customer> search(String keyword) throws IOException {
        HttpUrl url = HttpUrl.parse(BASE_URL + "/search").newBuilder().addQueryParameter("keyword", keyword).build();
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            return mapper.readValue(response.body().string(), new TypeReference<List<Customer>>(){});
        }
    }

    public Customer add(Customer customer) throws IOException {
        RequestBody body = RequestBody.create(mapper.writeValueAsString(customer), MediaType.parse("application/json"));
        Request request = new Request.Builder().url(BASE_URL).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException(response.body().string());
            return mapper.readValue(response.body().string(), Customer.class);
        }
    }

    public Customer update(String maKh, Customer customer) throws IOException {
        RequestBody body = RequestBody.create(mapper.writeValueAsString(customer), MediaType.parse("application/json"));
        Request request = new Request.Builder().url(BASE_URL + "/" + maKh).put(body).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException(response.body().string());
            return mapper.readValue(response.body().string(), Customer.class);
        }
    }

    public void delete(String maKh) throws IOException {
        Request request = new Request.Builder().url(BASE_URL + "/" + maKh).delete().build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException(response.body().string());
        }
    }
} 