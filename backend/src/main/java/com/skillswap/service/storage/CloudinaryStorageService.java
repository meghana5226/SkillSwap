package com.skillswap.service.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillswap.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

/**
 * Uploads files to Cloudinary via its plain signed-upload REST API
 * (https://cloudinary.com/documentation/upload_images#authenticated_requests),
 * deliberately without the Cloudinary Java SDK — one fewer dependency to
 * manage, and the signing scheme is simple enough to implement directly.
 *
 * Activate with: app.storage.provider=cloudinary
 * Required env vars: CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, CLOUDINARY_API_SECRET
 */
@Service
@ConditionalOnProperty(name = "app.storage.provider", havingValue = "cloudinary")
public class CloudinaryStorageService implements StorageService {

    private final String cloudName;
    private final String apiKey;
    private final String apiSecret;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CloudinaryStorageService(
            @Value("${app.storage.cloudinary.cloud-name:}") String cloudName,
            @Value("${app.storage.cloudinary.api-key:}") String apiKey,
            @Value("${app.storage.cloudinary.api-secret:}") String apiSecret) {
        this.cloudName = cloudName;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @Override
    public String store(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new ApiException("File is empty", HttpStatus.BAD_REQUEST);
        }

        try {
            long timestamp = System.currentTimeMillis() / 1000;
            String publicId = UUID.randomUUID().toString();

            // Cloudinary requires signing every param (except file/api_key/cloud_name)
            // sorted alphabetically as key=value pairs joined by '&', then SHA-1'd with the secret appended.
            String paramsToSign = "folder=" + folder + "&public_id=" + publicId + "&timestamp=" + timestamp;
            String signature = sha1Hex(paramsToSign + apiSecret);

            String boundary = "----SkillSwapBoundary" + UUID.randomUUID();
            byte[] body = buildMultipartBody(boundary, file, folder, publicId, timestamp, signature);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.cloudinary.com/v1_1/" + cloudName + "/auto/upload"))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ApiException("Cloudinary upload failed: " + response.body(), HttpStatus.BAD_GATEWAY);
            }

            JsonNode json = objectMapper.readTree(response.body());
            return json.get("secure_url").asText();

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("Cloudinary upload failed", HttpStatus.BAD_GATEWAY);
        }
    }

    private byte[] buildMultipartBody(String boundary, MultipartFile file, String folder,
                                       String publicId, long timestamp, String signature) throws IOException {
        String crlf = "\r\n";
        var out = new java.io.ByteArrayOutputStream();

        writeField(out, boundary, "api_key", apiKey);
        writeField(out, boundary, "timestamp", String.valueOf(timestamp));
        writeField(out, boundary, "folder", folder);
        writeField(out, boundary, "public_id", publicId);
        writeField(out, boundary, "signature", signature);

        out.write(("--" + boundary + crlf).getBytes(StandardCharsets.UTF_8));
        out.write(("Content-Disposition: form-data; name=\"file\"; filename=\""
                + file.getOriginalFilename() + "\"" + crlf).getBytes(StandardCharsets.UTF_8));
        out.write(("Content-Type: " + file.getContentType() + crlf + crlf).getBytes(StandardCharsets.UTF_8));
        out.write(file.getBytes());
        out.write(crlf.getBytes(StandardCharsets.UTF_8));
        out.write(("--" + boundary + "--" + crlf).getBytes(StandardCharsets.UTF_8));

        return out.toByteArray();
    }

    private void writeField(java.io.ByteArrayOutputStream out, String boundary, String name, String value) throws IOException {
        String crlf = "\r\n";
        out.write(("--" + boundary + crlf).getBytes(StandardCharsets.UTF_8));
        out.write(("Content-Disposition: form-data; name=\"" + name + "\"" + crlf + crlf).getBytes(StandardCharsets.UTF_8));
        out.write((value + crlf).getBytes(StandardCharsets.UTF_8));
    }

    private String sha1Hex(String input) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e);
        }
    }
}
