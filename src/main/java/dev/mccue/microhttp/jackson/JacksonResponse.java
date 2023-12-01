package dev.mccue.microhttp.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mccue.microhttp.handler.IntoResponse;
import dev.mccue.reasonphrase.ReasonPhrase;
import org.microhttp.Header;
import org.microhttp.Response;

import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An implementation of {@link IntoResponse} which produces JSON using
 * a Jackson {@link ObjectMapper}.
 * @param objectMapper The {@link ObjectMapper} to use.
 * @param status The http status code.
 * @param headers The headers to include in the request. Content-Type is added automatically.
 * @param body The object to encode into JSON.
 */
public record JacksonResponse(
        ObjectMapper objectMapper,
        int status,
        List<Header> headers,
        Object body
) implements IntoResponse {
    public JacksonResponse(
            int status,
            List<Header> headers,
            Object body
    ) {
        this(
                new ObjectMapper(),
                status,
                List.copyOf(headers),
                body
        );
    }

    public JacksonResponse(
            int status,
            Object body
    ) {
        this(
                status,
                List.of(),
                body
        );
    }

    public JacksonResponse(
            Object body
    ) {
        this(
                200,
                body
        );
    }

    @Override
    public Response intoResponse() {
        try {
            var body = objectMapper.writeValueAsBytes(this.body);
            var headers = new ArrayList<>(this.headers);
            headers.add(new Header("Content-Type", "application/json; charset=utf-8"));
            return new Response(
                    status,
                    ReasonPhrase.forStatus(status),
                    Collections.unmodifiableList(headers),
                    body
            );
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }
}
