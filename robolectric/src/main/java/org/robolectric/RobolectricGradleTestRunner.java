package org.robolectric;

import org.robolectric.res.FileFsFile;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.junit.runners.model.InitializationError;
import org.robolectric.util.Logger;

/**
 * Test runner customized for running unit tests either through the Gradle CLI or
 * Android Studio. The runner uses the build type and build flavor to compute the
 * resource, asset, and AndroidManifest paths.
 *
 * Using this test runner requires that the applicationId, type, and flavor be set
 * on the @Config annotation.
 */
public class RobolectricGradleTestRunner extends RobolectricTestRunner {
  private static final String BUILD_OUTPUT = "build/intermediates";

  public RobolectricGradleTestRunner(Class<?> klass) throws InitializationError {
    super(klass);
  }

  @Override
  protected AndroidManifest getAppManifest(Config config) {
    final String type = config.type();
    final String flavor = config.flavor();
    final String applicationId = config.applicationId();

    if (type == null || type.isEmpty()) {
      Logger.error("No type specified in @Config annotation");
      Logger.error("This is required when using RobolectricGradleTestRunner!");
      throw new RuntimeException("Unknown type specified in @Config annotation!");
    }

    if (applicationId == null || applicationId.isEmpty()) {
      Logger.error("No applicationId specified in @Config annotation");
      Logger.error("This is required when using RobolectricGradleTestRunner!");
      throw new RuntimeException("Unknown applicationId specified in @Config annotation.");
    }

    final FileFsFile res = FileFsFile.from(BUILD_OUTPUT, "res", flavor, type);
    final FileFsFile assets = FileFsFile.from(BUILD_OUTPUT, "assets", flavor, type);
    final FileFsFile manifest = FileFsFile.from(BUILD_OUTPUT, "manifests", "full", flavor, type, "/AndroidManifest.xml");

    Logger.debug("Robolectric assets directory: " + assets.getPath());
    Logger.debug("   Robolectric res directory: " + res.getPath());
    Logger.debug("   Robolectric manifest path: " + manifest.getPath());
    Logger.debug("    Robolectric package name: " + config.applicationId());
    return new AndroidManifest(manifest, res, assets, applicationId);
  }
}
