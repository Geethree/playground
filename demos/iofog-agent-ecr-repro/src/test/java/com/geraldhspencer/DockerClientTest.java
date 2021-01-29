package com.geraldhspencer;

import org.eclipse.iofog.microservice.Registry;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class DockerClientTest {

  public static String getStdOut(Process process) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    return reader.readLine();
  }

  public String getECRLoginPassword() throws IOException {
    Process process = Runtime.getRuntime().exec("aws ecr get-login-password");

    return getStdOut(process);
  }

  @Test
  void pullImage() throws Exception {
    SimpleDockerPullRepro dct = new SimpleDockerPullRepro();
    Registry.RegistryBuilder registryBuilder = new Registry.RegistryBuilder()
        .setId(1)
        .setUrl("742073802618.dkr.ecr.us-west-2.amazonaws.com")
        .setUserName("AWS")
        .setUserEmail("user@domain.com")
        .setPassword(getECRLoginPassword());

    dct.pullImage(
        "742073802618.dkr.ecr.us-west-2.amazonaws.com/strateos/poc/iofog/uploader",
        registryBuilder.build()
    );
  }

}