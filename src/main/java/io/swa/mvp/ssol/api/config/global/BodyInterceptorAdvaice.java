package io.swa.mvp.ssol.api.config.global;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import static io.swa.mvp.ssol.api.config.context.AuditContext.*;

//@RestControllerAdvice
@ControllerAdvice
public class BodyInterceptorAdvaice implements RequestBodyAdvice, ResponseBodyAdvice {

	private static final Logger log = LoggerFactory.getLogger(BodyInterceptorAdvaice.class);

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

		return inputMessage;
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {

//		ObjectMapper mapper = new ObjectMapper();
		auditInfo();
		getCurrentAuditRequest().setRequestBody(body.toString());
		auditInfo();
		if (log.isDebugEnabled())
			log.info(body.toString());

		return body;
	}

	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		log.info("No body");
		return body;
	}

	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

//		ObjectMapper mapper = new ObjectMapper();
		auditInfo();
		getCurrentAuditRequest().setResponseBody(body.toString());
		auditInfo();
		if (log.isDebugEnabled())
			log.info(body.toString());
		return body;
	}

}
