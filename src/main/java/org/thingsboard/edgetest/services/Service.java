package org.thingsboard.edgetest.services;

import org.springframework.beans.factory.annotation.Value;

abstract public class Service {
    @Value("${solution.name}")
    protected String solutionName;
    @Value("${host.name}")
    protected String host;
}
