/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.app;

import com.vmware.sdugar.model.DsaSigner;
import com.vmware.sdugar.model.PasswordEncoderConfig;
import com.vmware.sdugar.model.PkCrypt;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.util.Base64;
import java.util.UUID;

/**
 * Author: sdugar
 * Date:   12/8/15
 * Time:   2:38 PM
 */
@EnableAutoConfiguration
@ComponentScan(
      basePackages = {"com.vmware.sdugar.db",
                      "com.vmware.sdugar.app",
                      "com.vmware.sdugar.rest",
                      "com.vmware.sdugar.rest.exception",
                      "com.vmware.sdugar.model",
                      "com.vmware.sdugar.security"}
)
public class App {

   public static void main(String[] args) throws Throwable {
      SpringApplication app = new SpringApplication(App.class);
      app.setBannerMode(Banner.Mode.CONSOLE);
      app.run(args);

      DsaSigner signer = new DsaSigner();
      byte[] signature = signer.sign("hereisavalue");
      final String signedString = Base64.getEncoder().encodeToString(signature);
      System.out.println("The signature is " + signedString);
      boolean data = signer.verify(Base64.getDecoder().decode(signedString.getBytes()),
              "hereisavalue");
      System.out.println("the signature verification result: " + data);
      /*
      PkCrypt crypt = new PkCrypt("/Users/sourabhdugar/java/public.der",
              "/Users/sourabhdugar/java/private.der");
      final String userPassword = "sharedSecretHere";

      String jwtToken =
         PasswordEncoderConfig.createJWT(UUID.randomUUID().toString(), "SOURABH",
            "AUTH_TOKEN", userPassword);
      //crypt.saveKey(new File("/Users/sourabhdugar/java/shared_enc"), jwtToken);
      //String shared = crypt.loadKey(new File("/Users/sourabhdugar/java/shared_enc"));

      PasswordEncoderConfig.parseJWT(jwtToken, userPassword);
      //System.out.println("The password match result is " +
      //        new BCryptPasswordEncoder().matches("sharedsecrethere", userPassword));
      //System.out.println("The shared secret is " + shared);
      //SpringApplication app = new SpringApplication(App.class);
      //app.setBannerMode(Banner.Mode.CONSOLE);
      //app.run(args);
      crypt.saveKey(new File("/Users/sourabhdugar/java/shared_enc2"), "supersecretkeyword");
      crypt.loadKey(new File("/Users/sourabhdugar/java/shared_enc2"));
      */
   }
}
