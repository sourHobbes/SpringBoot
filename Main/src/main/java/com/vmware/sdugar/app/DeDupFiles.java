package com.vmware.sdugar.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by sdugar on 4/1/16.
 */
public class DeDupFiles {
   public static final Logger log = LoggerFactory.getLogger(DeDupFiles.class);
   public static final File MARKER = new File("");
   private static final int BYTES_TO_READ = 10000;
   private static final int BYTES_TO_READ_LARGE = 100 * BYTES_TO_READ;

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

   public static List<Set<String>> deDupFilesRec(final Map<String, FileInputStream> files)
           throws IOException {
      Map<String, FileInputStream> dupMap = new HashMap<>();
      Map<String, List<String>> matchMap = new HashMap<>();
      List<Set<String>> dupFiles = new ArrayList<>();

      final Consumer<Map.Entry<String, List<String>>> matchConsumer =
            (Map.Entry<String, List<String>> e) -> {
               log.info("Deduping file set {}", e.getValue());
               Map<String, FileInputStream> dupStreams =
                       e.getValue().stream().collect(Collectors.toMap(Function.identity(), files::get));
               try {
                  deDupFilesRec(dupStreams).forEach(dupFiles::add);
               } catch (IOException e1) {
                  log.error("IO exception", e);
               }
            };

      for (Map.Entry<String, FileInputStream> e : files.entrySet()) {
         if (e.getValue().available() <= 0) {
            // no more to read and we matched everything till now
            // so we have duplicates
            dupFiles.add(files.keySet());
            break;
         }
         
         byte [] bytes = new byte[BYTES_TO_READ_LARGE];
         int readLen = e.getValue().read(bytes, 0, bytes.length);
         String match = new String(bytes, 0, readLen);
         List<String> matches = matchMap.getOrDefault(match, new ArrayList<>());
         matches.add(e.getKey());
         matchMap.put(match, matches);
      }

      matchMap.entrySet()
              .stream()
              .filter(e -> e.getValue().size() > 1)
              .forEach(matchConsumer);

      return dupFiles;
   }

   public static List<Set<String>> deDupFiles(List<File> files)
         throws IOException {
      Map<Long, List<File>> fileSizeMap = new HashMap<>();
      final List<Set<String>> dupFileSets = new ArrayList<>();

      for (File fi : files) {
         if (fi == MARKER) continue;
         BasicFileAttributes attr = null;
         try {
            attr = Files.readAttributes(Paths.get(fi.toURI()), BasicFileAttributes.class);
            // a special file like a socket, pipe etc..skip it..
            if (attr.isOther()) continue;
         } catch (NoSuchFileException nsfe) {
            // FIXME this happens with filenames have spaces in name
            log.warn("Can't find file : {}", fi.getAbsolutePath());
            continue;
         }
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
                     //log.info("adding file with size : {} name : {}",
                     //        e.getKey(), e.getValue());
                     final FileInputStream fis = new FileInputStream(f);
                     deDupSet.put(f.getAbsolutePath(), fis);
                  } catch (FileNotFoundException e1) {
                     e1.printStackTrace();
                  }
               });
               deDupFilesRec(deDupSet).forEach(dupFileSets::add);
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
         List<File> files = nDeepDirWalk("/Users/sourabhdugar/", 1);

         files.stream().forEach(
               (f) -> {
                  if (f == MARKER) {
                     sb.append("\t");
                  } else {
                     log.info(sb.toString() + f.getAbsolutePath());
                  }
               });
         List<Set<String>> fileSets = deDupFiles(files);
         fileSets.stream().filter((set) -> !set.isEmpty()).forEach((set) -> {
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
