package org.alice.ide;

import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.net.UriUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.java.util.zip.ByteArrayDataSource;
import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import org.alice.tweedle.file.ManifestEncoderDecoder;
import org.lgna.project.Project;
import org.lgna.project.io.IoUtilities;
import org.lgna.project.io.ProjectIo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static edu.cmu.cs.dennisc.java.io.FileUtilities.*;
import static org.lgna.project.io.IoUtilities.BACKUP_EXTENSION;
import static org.lgna.project.io.IoUtilities.PROJECT_EXTENSION;

class ProjectFileUtilities {
  private static final String BACKUP_AUTO = "auto";
  private static final String BACKUP_SAVE = "save";
  private static final DateTimeFormatter ORDER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
  private static final int BACKUP_MAX = 5;
  private static final int SECONDS_BETWEEN_BACKUPS = 300;

  private final ProjectApplication projectApp;

  private final ScheduledExecutorService savingService;
  private ScheduledFuture<?> saveFuture;

  ProjectFileUtilities(ProjectApplication app) {
    projectApp = app;
    savingService = Executors.newSingleThreadScheduledExecutor();
  }

  final void saveProjectTo(File file) throws IOException {
    saveCopyOfProjectTo(file);
    backupSavedProject();
  }

  final void startAutoSaving() {
    if (saveFuture != null) {
      saveFuture.cancel(false);
    }
    saveFuture = savingService.scheduleAtFixedRate(autosaveActiveProject(), SECONDS_BETWEEN_BACKUPS, SECONDS_BETWEEN_BACKUPS, TimeUnit.SECONDS);
  }

  private void saveCopyOfProjectTo(File file) throws IOException {
    Project project = projectApp.getUpToDateProject();
    IoUtilities.writeProject(file, project, thumbnailAndManifestDataSources(project));
  }

  private DataSource[] thumbnailAndManifestDataSources(Project project) {
    List<DataSource> sources = new ArrayList<>();
    DataSource thumbnail = thumbnailDataSource();
    if (thumbnail != null) {
      sources.add(thumbnail);
    }
    sources.add(new ByteArrayDataSource(
        ProjectIo.MANIFEST_ENTRY_NAME,
        ManifestEncoderDecoder.toJson(project.createSaveManifest())));
    return sources.toArray(new DataSource[0]);
  }

  void exportCopyOfProjectTo(File file) throws IOException {
    Project project = projectApp.getForcedUpToDateProject();
    IoUtilities.exportProject(file, project, thumbnailDataSources());
  }

  private DataSource[] thumbnailDataSources() {
    DataSource thumbnail = thumbnailDataSource();
    return thumbnail == null ? new DataSource[]{} : new DataSource[]{thumbnail};
  }

  private DataSource thumbnailDataSource() {
    try {
      final BufferedImage thumbnailImage = projectApp.createThumbnail();
      if (thumbnailImage == null
          || thumbnailImage.getWidth() <= 0
          || thumbnailImage.getHeight() <= 0) {
        return null;
      }
      final byte[] data = ImageUtilities.writeToByteArray(ImageUtilities.PNG_CODEC_NAME, thumbnailImage);
      return new DataSource() {
        @Override
        public String getName() {
          return "thumbnail.png";
        }

        @Override
        public void write(OutputStream os) throws IOException {
          os.write(data);
        }
      };
    } catch (Throwable ignored) {
    }
    return null;
  }

  private void backupSavedProject() throws IOException {
    File saved = UriUtilities.getFile(projectApp.getUri());
    if (saved == null) {
      return;
    }
    Path backupDir = backupDirectory(saved);
    if (backupDir == null) {
      return;
    }
    File backupFile = backupFile(BACKUP_SAVE, backupDir);

    copyFile(saved, backupFile);

    removeExtraBackups(BACKUP_SAVE, backupDir);
  }

  private Runnable autosaveActiveProject() {
    return new Runnable() {
      @Override
      public void run() {
        try {
          ProjectFileUtilities.this.backupActiveProject();
        } catch (IOException e) {
          Logger.throwable(e, "Unable to autosave project.");
          e.printStackTrace();
        }
      }
    };
  }

  private void backupActiveProject() throws IOException {
    File saved = UriUtilities.getFile(projectApp.getUri());
    if (saved == null) {
      return;
    }
    Path backupDir = backupDirectory(saved);
    if (backupDir == null) {
      return;
    }
    File backupFile = backupFile(BACKUP_AUTO, backupDir);

    saveCopyOfProjectTo(backupFile);

    removeExtraBackups(BACKUP_AUTO, backupDir);
  }

  private Path backupDirectory(File saved) {
    String fileName = saved.getName();
    String directoryName;
    directoryName = (PROJECT_EXTENSION.equals(getExtension(fileName)) ? getBaseName(fileName) : fileName) + "." + BACKUP_EXTENSION;

    Path backupDir = saved.toPath().resolveSibling(directoryName);
    if (Files.notExists(backupDir)) {
      try {
        Files.createDirectory(backupDir);
      } catch (IOException e) {
        Dialogs.showWarning("Unable to Save Backups", "Backup directory `" + backupDir + "` could not be created.");
        return null;
      }
    }
    return backupDir;
  }

  private File backupFile(String type, Path backupDir) {
    String fileName = String.format("%s%s.%s", type, LocalDateTime.now().format(ORDER_FORMAT), PROJECT_EXTENSION);
    return backupDir.resolve(fileName).toFile();
  }

  private void removeExtraBackups(final String type, Path backupDir) {
    File[] backups = listFiles(backupDir.toFile(), new FileFilter() {
      @Override
      public boolean accept(File file) {
        return file.isFile() && file.getName().startsWith(type);
      }
    });
    if (backups.length > BACKUP_MAX) {
      Arrays.sort(backups);
      for (int i = 0; i < backups.length - BACKUP_MAX; i++) {
        File backup = backups[i];
        if (!backup.delete()) {
          Logger.warning("Unable to delete old backup file " + backup);
        }
      }
    }
  }
}
