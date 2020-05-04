package org.thingsboard.edgetest.services.action.uninstall;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.thingsboard.edgetest.data.host.HostDetails;
import org.thingsboard.edgetest.services.action.ActionService;

import javax.annotation.PostConstruct;

@Service
public class UninstallService extends ActionService {

    @PostConstruct
    private void construct() {
        HostDetails targetHost = new AnnotationConfigApplicationContext("org.thingsboard.edgetest.data.host."+target).getBean(target, HostDetails.class);
        solution.initSolution(targetHost.getRestClient());
        logger.info("Unnstallation target: " + "http" + targetHost.getHostName());
        logger.info("Solution " + solutionName + " initialized");
    }

    public void start() {
        solution.uninstall();
    }
}
