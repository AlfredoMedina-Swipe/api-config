package io.swa.mvp.ssol.api.config.context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.swa.mvp.ssol.api.config.entity.AuditRequest;

@Component
public class AuditContext {
	private AuditContext() {

	}

	private static final Logger log = LoggerFactory.getLogger(AuditContext.class);

	private static final String WARN = "Audit is corupt in context, actual is : {0} update is: {1}";

	private static ThreadLocal<AuditRequest> auditRequest = new ThreadLocal<>();

	private static List<AuditRequest> history = new ArrayList<>();

	public static AuditRequest getCurrentAuditRequest() {
		if (auditRequest.get() == null) {
			AuditRequest auditRequestCurrent = new AuditRequest();
			auditRequestCurrent.setId(UUID.randomUUID());
			auditRequest.set(auditRequestCurrent);
			return auditRequest.get();
		}
		return auditRequest.get();
	}

	public static void updateAudit(AuditRequest updatedAudit) {
		AuditRequest currentAudit = auditRequest.get();
		if (!currentAudit.getId().equals(updatedAudit.getId())) {
			log.warn(WARN, currentAudit.getId(), updatedAudit.getId());

		}
		auditRequest.remove();
		auditRequest.set(updatedAudit);

	}

	public static void perist() {
		history.add(auditRequest.get());
		auditRequest.remove();
	}

	public static void auditInfo() {
		if (log.isDebugEnabled())
			log.info("Current Audit : " + auditRequest.get().toString());
	}

}
