package com.vmware.sdugar.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

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

   /**
    * This function performs a dir walk to list all files up to {@param depth}
    *
    * @param dir    The directory to walk. A 0 value means all sub-directories are explored
    * @param depth  The depth at which to stop exploring sub-directories
    *
    * @return       List of {@link File} objects obtained by performing the walk
     */
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
            if (depth > 0 && ++explored >= depth) {
               break;
            }
            depthStack.add(MARKER);
            continue;
         }

         if (f.listFiles() != null) {
            for (final File fi : f.listFiles()) {
               if (fi.isDirectory()) {
                  depthStack.add(fi);
               } else {
                  retFiles.add(fi);
               }
            }
         }
      }

      return retFiles;
   }

   public static Set<String> deDupFilesRec(Map<String, FileInputStream> files)
           throws IOException {
      Map<String, FileInputStream> dupMap = new HashMap<>();
      Map<String, List<String>> matchMap = new HashMap<>();
      Set<String> dupFiles = new HashSet<>();

      for (Map.Entry<String, FileInputStream> e : files.entrySet()) {
         if (e.getValue().available() <= 0) {
            // no more to read and we matched everything till now
            // so we have duplicates
            dupFiles.addAll(files.keySet());
            break;
         }

         byte [] bytes = new byte[100];
         e.getValue().read(bytes, 0, bytes.length);

         String match = new String(bytes);
         List<String> matches = matchMap.getOrDefault(match, new ArrayList<>());
         matches.add(e.getKey());
         matchMap.put(match, matches);
      }
      matchMap.entrySet().stream().filter((e) -> e.getValue().size() > 1)
         .forEach((e) -> {
            e.getValue().stream().forEach((s) -> dupMap.put(s, files.get(s)));
            try {
               dupFiles.addAll(deDupFilesRec(dupMap));
            } catch (IOException e1) {
               e1.printStackTrace();
            }
         });

      return dupFiles;
   }

   public static List<Set<String>> deDupFiles(List<File> files)
         throws IOException {
      Map<Long, List<File>> fileSizeMap = new HashMap<>();
      List<Set<String>> dupFileSets = new ArrayList<>();

      for (File fi : files) {
         if (fi == MARKER) continue;
         BasicFileAttributes attr = Files.readAttributes(Paths.get(fi.getAbsolutePath()),
               BasicFileAttributes.class);
         log.info(String.format("%-80s : %-10s", fi.getAbsolutePath(), attr.size()));
         if (fileSizeMap.get(attr.size()) == null) {
            List<File> sameSizeFiles = new ArrayList<>();
            sameSizeFiles.add(fi);
            fileSizeMap.put(attr.size(), sameSizeFiles);
         } else {
            fileSizeMap.get(attr.size()).add(fi);
         }
      }
      // all files of 0 length are duplicates
      if (fileSizeMap.get(0) != null) {
         Set<File> dupSet = new HashSet<>();
         fileSizeMap.remove(0).stream().forEach(dupSet::add);
      }
      fileSizeMap.entrySet().stream().filter((e) -> e.getValue().size() > 1)
         .forEach((e) -> {
            final Map<String, FileInputStream> deDupSet = new HashMap<>();
            try {
               e.getValue().stream().forEach((f) -> {
                  try {
                     log.info("adding file with size : {} name : {}",
                             e.getKey(), e.getValue());
                     final FileInputStream fis = new FileInputStream(f);
                     deDupSet.put(f.getAbsolutePath(), fis);
                  } catch (FileNotFoundException e1) {
                     e1.printStackTrace();
                  }
               });
               Set<String> dupFiles = deDupFilesRec(deDupSet);
               dupFileSets.add(dupFiles);
            } catch (IOException e1) {
               e1.printStackTrace();
            } finally {
               for (FileInputStream fis : deDupSet.values()) {
                  try {
                     fis.close();
                  } catch (IOException e1) {
                     e1.printStackTrace();
                  }
               }
            }
         });
      return dupFileSets;
   }

   public static void main(String[] args) {
      try {
         StringBuilder sb = new StringBuilder("");
         List<File> files = nDeepDirWalk("/Users/sourabhdugar/testcodes/", 3);

         files.stream().forEach(
               (f) -> {
                  if (f == MARKER) {
                     sb.append("\t");
                  } else {
                     log.info(sb.toString() + f.getAbsolutePath());
                  }
               });
         List<Set<String>> fileSets = deDupFiles(files);
         fileSets.stream().forEach((set) -> {
            StringBuilder fileNames = new StringBuilder();
            set.stream().forEach((s) -> fileNames.append(s).append(","));
            log.info("----------------");
            log.info(fileNames.toString());
            log.info("----------------");
         });
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
