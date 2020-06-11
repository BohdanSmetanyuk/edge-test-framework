package org.thingsboard.edgetest.black.box.cases.docker;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;
import org.thingsboard.edgetest.black.box.config.ContainersConfig;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {ContainersConfig.class})
public class EdgeContainer {

    private DockerComposeContainer edgeContainer;

    @Autowired
    public void init(ContainersConfig containers) {
        edgeContainer = containers.getEdgeContainer();
    }

    @Test
    public void runEdgeContainer() {
        edgeContainer.start();
        log.info("Edge started");
    }
}
