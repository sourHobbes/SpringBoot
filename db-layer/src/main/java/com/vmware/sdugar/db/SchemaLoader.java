/* **********************************************************************
 * Copyright 2015 VMware, Inc. All rights reserved. VMware Confidential
 * **********************************************************************/
package com.vmware.sdugar.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.datastax.driver.core.Session;

/**
 * Author: sdugar
 * Date:   12/7/15
 * Time:   11:21 AM
 */
public class SchemaLoader {
   private static final Logger LOG =
         LoggerFactory.getLogger(SchemaLoader.class);

   private static final String COMMENT_PREFIX = "#";
   private static final String STATEMENT_END = ";";

   public static void loadSchema(Session session, String fileName) {
      try {
         InputStream is = Thread.currentThread().getContextClassLoader()
               .getResourceAsStream(fileName);
         if (is == null) {
            throw new RuntimeException("could not find resource [" + fileName
                  + "]");
         }
         BufferedReader in = new BufferedReader(new InputStreamReader(is));
         String line = null;
         StringBuilder buf = new StringBuilder();
         while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.startsWith(COMMENT_PREFIX)) {
               continue;
            }

            buf.append(line);

            if (line.endsWith(STATEMENT_END)) {
               String stmt = buf.toString();

               LOG.info("\r\n" + stmt);
               session.execute(stmt);

               buf.setLength(0);
            }
         }
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
}
