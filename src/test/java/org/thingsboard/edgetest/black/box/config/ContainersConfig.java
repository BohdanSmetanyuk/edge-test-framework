package org.thingsboard.edgetest.black.box.config;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.thingsboard.edgetest.black.box.util.DataSolver;

import javax.annotation.PostConstruct;

@Component
@Getter
public class ContainersConfig {

    private DockerComposeContainer cloudContainer;
    private DockerComposeContainer edgeContainer;

    @PostConstruct
    private void init() {
        DataSolver ds = new DataSolver();
        cloudContainer = new DockerComposeContainer(ds.getDockerFile("cloud"))
                .withPull(false)
                .withLocalCompose(true)
                .waitingFor("mytb", Wait.forHttp("/home")); //
        edgeContainer = new DockerComposeContainer(ds.getDockerFile("edge"))
                .withPull(false)
                .withLocalCompose(true)
                .waitingFor("mytbedge", Wait.forHttp("/home")); //
    }

}
