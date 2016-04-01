package com.vmware.sdugar.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * Created by sdugar on 4/1/16.
 */
public class DeDupFiles {
   public static final Logger log = LoggerFactory.getLogger(DeDupFiles.class);
   public static final File MARKER = new File("");
   
   public static List<File> dirWalk(String dir)
         throws NotDirectoryException {
      File d = new File(dir);
      if (!d.isDirectory()) {
         throw new NotDirectoryException(dir + " is not a directory");
      }
      List<File> files = Arrays.asList(d.listFiles());
      return files;
   }

   public static List<File> nDeepDirWalk(final String dir,
                                         final int depth) {
      Queue<File> depthStack = new LinkedList<>();
      List<File> retFiles = new ArrayList<>();
      File initDir = new File(dir);
      int explored = 0;

      depthStack.add(initDir);
      depthStack.add(MARKER);

      while (!depthStack.isEmpty()) {
         final File f = depthStack.remove();
         if (f == MARKER) {
            retFiles.add(MARKER);
            if (++explored >= depth) {
               break;
            }
            depthStack.add(MARKER);
            continue;
         }
         for (final File fi : f.listFiles()) {
            if (fi.isDirectory()) {
               depthStack.add(fi);
            }
            retFiles.add(fi);
         }
      }

      return retFiles;
   }

   public static List<File> deDupFiles(List<File> files)
         throws IOException {
      for (File fi : files) {
         BasicFileAttributes attr = Files.readAttributes(Paths.get(fi.getAbsolutePath()),
               BasicFileAttributes.class);
         log.info(String.format("File : %40s : %-10s", fi.getAbsolutePath(), attr.size()));
      }
      return null;
   }

   public static void main(String[] args) {
      try {
         StringBuilder sb = new StringBuilder("");
         List<File> files = nDeepDirWalk("/Users/sdugar/testcodes", 3);

         files.stream().forEach(
               (f) -> {
                  if (f == MARKER) {
                     sb.append("\t");
                  } else {
                     log.info(sb.toString() + f.getAbsolutePath());
                  }
               });
         deDupFiles(files);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
