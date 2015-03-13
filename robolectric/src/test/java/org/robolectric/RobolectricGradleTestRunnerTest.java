package org.robolectric;

import org.junit.Rule;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.rules.ExpectedException;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import static org.assertj.core.api.Assertions.assertThat;

public class RobolectricGradleTestRunnerTest {
  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void getAppManifest_shouldCreateManifest() throws Exception {
    final RobolectricGradleTestRunner runner = new RobolectricGradleTestRunner(ManifestTest.class);
    final AndroidManifest manifest = runner.getAppManifest(runner.getConfig(ManifestTest.class.getMethod("withoutAnnotation")));

    assertThat(manifest.getPackageName()).isEqualTo("org.sandwich.foo");
    assertThat(manifest.getResDirectory().getPath()).isEqualTo("build/intermediates/res/flavor1/type1");
    assertThat(manifest.getAssetsDirectory().getPath()).isEqualTo("build/intermediates/assets/flavor1/type1");
    assertThat(manifest.getAndroidManifestFile().getPath()).isEqualTo("build/intermediates/manifests/full/flavor1/type1/AndroidManifest.xml");
  }

  @Test
  public void getAppManifest_shouldCreateManifestWithMethodOverrides() throws Exception {
    final RobolectricGradleTestRunner runner = new RobolectricGradleTestRunner(ManifestTest.class);
    final AndroidManifest manifest = runner.getAppManifest(runner.getConfig(ManifestTest.class.getMethod("withOverrideAnnotation")));

    assertThat(manifest.getPackageName()).isEqualTo("org.sandwich.bar");
    assertThat(manifest.getResDirectory().getPath()).isEqualTo("build/intermediates/res/flavor2/type2");
    assertThat(manifest.getAssetsDirectory().getPath()).isEqualTo("build/intermediates/assets/flavor2/type2");
    assertThat(manifest.getAndroidManifestFile().getPath()).isEqualTo("build/intermediates/manifests/full/flavor2/type2/AndroidManifest.xml");
  }

  @Test
  public void getAppManifest_shouldNotRequireFlavor() throws Exception {
    final RobolectricGradleTestRunner runner = new RobolectricGradleTestRunner(NoFlavorTest.class);
    final AndroidManifest manifest = runner.getAppManifest(runner.getConfig(NoFlavorTest.class.getMethod("withoutAnnotation")));

    assertThat(manifest.getPackageName()).isEqualTo("org.sandwich");
    assertThat(manifest.getResDirectory().getPath()).isEqualTo("build/intermediates/res/type");
    assertThat(manifest.getAssetsDirectory().getPath()).isEqualTo("build/intermediates/assets/type");
    assertThat(manifest.getAndroidManifestFile().getPath()).isEqualTo("build/intermediates/manifests/full/type/AndroidManifest.xml");
  }

  @Test
  public void getAppManifest_shouldThrowException_whenTypeNotSpecified() throws Exception {
    final RobolectricGradleTestRunner runner = new RobolectricGradleTestRunner(NoTypeTest.class);
    exception.expect(RuntimeException.class);
    runner.getAppManifest(runner.getConfig(NoTypeTest.class.getMethod("withoutAnnotation")));
  }

  @Test
  public void getAppManifest_shouldThrowException_whenApplicationIdNotSpecified() throws Exception {
    final RobolectricGradleTestRunner runner = new RobolectricGradleTestRunner(NoApplicationTest.class);
    exception.expect(RuntimeException.class);
    runner.getAppManifest(runner.getConfig(NoApplicationTest.class.getMethod("withoutAnnotation")));
  }

  @Ignore
  @Config(applicationId = "org.sandwich.foo", type = "type1", flavor = "flavor1")
  public static class ManifestTest {

    @Test
    public void withoutAnnotation() throws Exception {
    }

    @Test @Config
    public void withDefaultsAnnotation() throws Exception {
    }

    @Test @Config(applicationId = "org.sandwich.bar", type = "type2", flavor = "flavor2")
    public void withOverrideAnnotation() throws Exception {
    }
  }

  @Ignore
  @Config(applicationId = "org.sandwich", type = "type")
  public static class NoFlavorTest {

    @Test
    public void withoutAnnotation() throws Exception {
    }
  }

  @Ignore
  @Config(applicationId = "org.sandwich")
  public static class NoTypeTest {

    @Test
    public void withoutAnnotation() throws Exception {
    }
  }

  @Ignore
  @Config(type = "type")
  public static class NoApplicationTest {

    @Test
    public void withoutAnnotation() throws Exception {
    }
  }
}