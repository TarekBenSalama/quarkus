package io.quarkus.bootstrap.resolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;

/**
 *
 * @author Alexey Loubyansky
 */
public class TsDependency {

    protected final TsArtifact artifact;
    protected String scope;
    protected boolean optional;
    protected List<TsArtifact> excluded = Collections.emptyList();

    public TsDependency(TsArtifact artifact) {
        this(artifact, null, false);
    }

    public TsDependency(TsArtifact artifact, String scope) {
        this(artifact, scope, false);
    }

    public TsDependency(TsArtifact artifact, boolean optional) {
        this(artifact, null, optional);
    }

    public TsDependency(TsArtifact artifact, String scope, boolean optional) {
        this.artifact = artifact;
        this.scope = scope;
        this.optional = optional;
    }

    public TsDependency exclude(String artifactId) {
        return exclude(TsArtifact.ga(artifactId));
    }

    public TsDependency exclude(String groupId, String artifactId) {
        return exclude(TsArtifact.ga(groupId, artifactId));
    }

    public TsDependency exclude(TsArtifact artifact) {
        if (excluded.isEmpty()) {
            excluded = new ArrayList<>();
        }
        excluded.add(artifact);
        return this;
    }

    public TsDependency exclude(TsArtifact... artifacts) {
        for (TsArtifact artifact : artifacts) {
            exclude(artifact);
        }
        return this;
    }

    public Dependency toPomDependency() {
        final Dependency dep = new Dependency();
        dep.setGroupId(artifact.groupId);
        dep.setArtifactId(artifact.artifactId);
        final String updateClassifier = artifact.classifier;
        if (updateClassifier != null && !updateClassifier.isEmpty()) {
            dep.setClassifier(updateClassifier);
        }
        dep.setType(artifact.type);
        dep.setVersion(artifact.version);
        if (scope != null) {
            dep.setScope(scope);
        }
        if (optional) {
            dep.setOptional(optional);
        }
        if (!excluded.isEmpty()) {
            for (TsArtifact excluded : excluded) {
                final Exclusion exclusion = new Exclusion();
                exclusion.setGroupId(excluded.groupId);
                exclusion.setArtifactId(excluded.artifactId);
                dep.addExclusion(exclusion);
            }
        }
        return dep;
    }
}
