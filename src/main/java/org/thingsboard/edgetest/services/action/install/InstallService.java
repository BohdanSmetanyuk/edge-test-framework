package org.thingsboard.edgetest.services.action.install;

import org.springframework.stereotype.Service;
import org.thingsboard.edgetest.services.action.ActionService;

@Service
public class InstallService extends ActionService {

    public void start() {
        solution.install();
    }
}
