package io.quarkus.scala.maven.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.shared.invoker.*;
import org.junit.jupiter.api.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import io.quarkus.maven.it.QuarkusPlatformAwareMojoTestBase;
import io.quarkus.maven.utilities.MojoUtils;

public class ScalaCreateMavenProjectIT extends QuarkusPlatformAwareMojoTestBase {

    private Invoker invoker;
    private File testDir;

    @Test
    public void testProjectGenerationFromScratchForScala() throws MavenInvocationException, IOException {

        testDir = initEmptyProject("projects/project-generation-scala");
        assertThat(testDir).isDirectory();
        invoker = initInvoker(testDir);

        Properties properties = new Properties();
        properties.put("projectGroupId", "org.acme");
        properties.put("projectArtifactId", "acme");
        properties.put("projectVersion", "1.0-SNAPSHOT");
        properties.put("extensions", "scala,resteasy-jsonb");
        setup(properties);

        // As the directory is not empty (log) navigate to the artifactID directory
        testDir = new File(testDir, "acme");

        assertThat(new File(testDir, "pom.xml")).isFile();
        assertThat(new File(testDir, "src/main/scala")).isDirectory();
        assertThat(new File(testDir, "src/main/resources/application.properties")).isFile();

        String config = Files
                .asCharSource(new File(testDir, "src/main/resources/application.properties"), Charsets.UTF_8)
                .read();
        assertThat(config).isEmpty();

        assertThat(new File(testDir, "src/main/docker/Dockerfile.native")).isFile();
        assertThat(new File(testDir, "src/main/docker/Dockerfile.jvm")).isFile();

        Model model = loadPom(testDir);
        final DependencyManagement dependencyManagement = model.getDependencyManagement();
        final List<Dependency> dependencies = dependencyManagement.getDependencies();
        assertThat(dependencies.stream()
                .anyMatch(d -> d.getArtifactId().equals(MojoUtils.TEMPLATE_PROPERTY_QUARKUS_PLATFORM_ARTIFACT_ID_VALUE)
                        && d.getVersion().equals(MojoUtils.TEMPLATE_PROPERTY_QUARKUS_PLATFORM_VERSION_VALUE)
                        && d.getScope().equals("import")
                        && d.getType().equals("pom")))
                .isTrue();

        assertThat(
                model.getDependencies().stream().anyMatch(d -> d.getArtifactId().equals("quarkus-resteasy")
                        && d.getVersion() == null))
                .isTrue();
        assertThat(
                model.getDependencies().stream().anyMatch(d -> d.getArtifactId().equals("quarkus-scala")
                        && d.getVersion() == null))
                .isTrue();

        assertThat(model.getProfiles()).hasSize(1);
        assertThat(model.getProfiles().get(0).getId()).isEqualTo("native");
    }

    private InvocationResult setup(Properties params)
            throws MavenInvocationException, FileNotFoundException, UnsupportedEncodingException {

        params.setProperty("platformGroupId", getBomGroupId());
        params.setProperty("platformArtifactId", getBomArtifactId());
        params.setProperty("platformVersion", getBomVersion());

        InvocationRequest request = new DefaultInvocationRequest();
        request.setBatchMode(true);
        request.setGoals(Collections.singletonList(
                getMavenPluginGroupId() + ":" + getMavenPluginArtifactId() + ":" + getMavenPluginVersion() + ":create"));
        request.setProperties(params);
        File log = new File(testDir, "build-create-" + testDir.getName() + ".log");
        final PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(new FileOutputStream(log), false, "UTF-8"),
                InvokerLogger.INFO);
        invoker.setLogger(logger);
        return invoker.execute(request);
    }
}
