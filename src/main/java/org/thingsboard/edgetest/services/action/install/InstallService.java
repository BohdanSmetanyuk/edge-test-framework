package org.thingsboard.edgetest.services.action.install;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.thingsboard.edgetest.data.host.HostDetails;
import org.thingsboard.edgetest.services.action.ActionService;

import javax.annotation.PostConstruct;

@Service
public class InstallService extends ActionService {

    @PostConstruct
    private void construct() {
        HostDetails targetHost = new AnnotationConfigApplicationContext("org.thingsboard.edgetest.data.host."+target).getBean(target, HostDetails.class);
        solution.initSolution(targetHost.getRestClient());
        logger.info("Installation target: " + "http" + targetHost.getHostName());
        logger.info("Solution " + solutionName + " initialized");
    }

    public void start() {
        solution.install();
    }
}
