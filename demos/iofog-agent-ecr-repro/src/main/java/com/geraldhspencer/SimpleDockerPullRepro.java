package com.geraldhspencer;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.eclipse.iofog.microservice.Registry;



public class SimpleDockerPullRepro {

  DockerClient dockerClient;

  public SimpleDockerPullRepro() {
    initDocker();
  }

  public void initDocker() {
    DefaultDockerClientConfig.Builder configBuilder = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost("unix:///var/run/docker.sock");
    DockerClientConfig config = configBuilder.build();
    dockerClient = DockerClientBuilder.getInstance(config).build();
  }

  public void pullImage(String image, Registry registry) throws Exception {
    System.out.println("Pulling");
    System.out.println(registry.getPassword());

    try {
      PullImageCmd req = dockerClient.pullImageCmd(image).withAuthConfig(
          new AuthConfig()
              .withRegistryAddress(registry.getUrl())
              .withEmail(registry.getUserEmail())
              .withUsername(registry.getUserName())
              .withPassword(registry.getPassword())
      );

      PullImageResultCallback res = new PullImageResultCallback();
      res = req.exec(res);

      res.awaitCompletion();
    }  catch (Exception e) {
      throw new Exception(e.getMessage(), e);
    }
  }
}
