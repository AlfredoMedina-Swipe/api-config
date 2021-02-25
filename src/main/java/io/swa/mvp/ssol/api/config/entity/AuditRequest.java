package io.swa.mvp.ssol.api.config.entity;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class AuditRequest {

	private UUID id;

	private UUID userId;

	private String userName;

	private Map<String, String> requestHeaders;

	private Map<String, String> responseHeaders;

	private String requestMethod;

	private String route;

	private String requestBody;

	private String responseBody;

	private String requetOriginHost;

	private HttpStatus status;

	private Timestamp initRequest;

	private Timestamp endRequest;
}
