package org.thingsboard.edgetest.services.action.uninstall;

import org.springframework.stereotype.Service;
import org.thingsboard.edgetest.configuration.ApplicationConfig;
import org.thingsboard.edgetest.data.host.HostDetails;
import org.thingsboard.edgetest.services.action.ActionService;


@Service
public class UninstallService extends ActionService {

    @Override
    public void init(ApplicationConfig applicationConfig) {
        super.init(applicationConfig);
        HostDetails targetHost = applicationConfig.getTargetDetails(target);
        solution.initSolution(targetHost.getRestClient());
        logger.info("Unnstallation target: " + "http" + targetHost.getHostName());
        logger.info("Solution " + solutionName + " initialized");
        inited = true;
    }

    public void start() {
        solution.uninstall();
    }
}
