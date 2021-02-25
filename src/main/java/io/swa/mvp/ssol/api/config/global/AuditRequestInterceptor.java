package io.swa.mvp.ssol.api.config.global;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import io.swa.mvp.ssol.api.config.entity.AuditRequest;
import static io.swa.mvp.ssol.api.config.context.AuditContext.*;
//import net.minidev.json.JSONObject;
//import net.minidev.json.JSONValue;

@Component
public class AuditRequestInterceptor implements AsyncHandlerInterceptor {

	@Value("${selful.cors.no-origin:true}")
	private boolean isNoOriginEnable;

	private static final Logger log = LoggerFactory.getLogger(AuditRequestInterceptor.class);
//	private static final String REQUEST = "request";
//	private static final String RESPONSE = "response";
//	private static final String ORIGIN = "origin";
	private static final String NEXT = "next";

	public boolean getEnable() {
		return isNoOriginEnable;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

//		Map<String, Object> mapRequest = new HashMap<String, Object>();

//		mapRequest.put(REQUEST, request);
//		mapRequest.put(RESPONSE, response);
//
//		mapRequest = verifyRequest(mapRequest);
		getCurrentAuditRequest().setInitRequest(Timestamp.valueOf(LocalDateTime.now()));
		getCurrentAuditRequest().setRoute(request.getRequestURI());
		getCurrentAuditRequest().setRequestMethod(request.getMethod());
		List<String> headers = new ArrayList<>();

		request.getHeaderNames().asIterator().forEachRemaining(headers::add);
		Map<String, String> requestHeaders = new HashMap<>();
		headers.stream().forEach(header -> requestHeaders.put(header, request.getHeader(header)));

		getCurrentAuditRequest().setRequestHeaders(requestHeaders);
		getCurrentAuditRequest().setRequetOriginHost(request.getLocalAddr());

		log.debug(requestHeaders.toString());

		auditInfo();

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		List<String> headers = new ArrayList<>();

		response.getHeaderNames().forEach(headers::add);
		Map<String, String> responseHeaders = new HashMap<>();
		headers.stream().forEach(header -> responseHeaders.put(header, response.getHeader(header))

		);

		getCurrentAuditRequest().setResponseHeaders(responseHeaders);
		getCurrentAuditRequest().setStatus(HttpStatus.valueOf(response.getStatus()));
		getCurrentAuditRequest().setEndRequest(Timestamp.valueOf(LocalDateTime.now()));

		auditInfo();
	}

//	private Map<String, Object> verifyRequest(Map<String, Object> mapRequest) {
//
//		HttpServletRequest request = (HttpServletRequest) mapRequest.get(REQUEST);
//
//		HttpServletResponse response = (HttpServletResponse) mapRequest.get(RESPONSE);
//		List<String> headers = new ArrayList<String>();
//
//		request.getHeaderNames().asIterator().forEachRemaining(headers::add);
//		Map<String, String> requestHeaders = new HashMap<>();
//		headers.stream().forEach(header -> {
//			requestHeaders.put(header, request.getHeader(header));
//
//		});
//	
//		auditRequest.setRoute(request.getRequestURI());
//		auditRequest.setRequestHeaders(requestHeaders);
//		if(requestHeaders.containsKey(ORIGIN)) {
//			auditRequest.setRequetOriginHost(requestHeaders.get(ORIGIN));
//		}
//
//		log.debug(requestHeaders.toString());
//
//		return mapRequest;
//	}
//

//	@Override
//	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
//
//		return false;
//	}
//
//	@Override
//	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
//			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
//			ServerHttpResponse response) {
//
//		ObjectMapper mapper = new ObjectMapper();
//
//		Map<String, Object> mapBody = mapper.convertValue(body, Map.class);
//		auditRequest.setResponseBody(mapBody.toString());
//
//		return body;
//	}

//	private boolean verifyOrigin(Map<String, String> headers) {
//
//		return headers.containsKey(ORIGIN) && !(headers.get(ORIGIN).isBlank() || headers.get(ORIGIN).isEmpty());
//	}

}
