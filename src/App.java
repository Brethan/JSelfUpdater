import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.stream.Stream;

import org.kohsuke.github.GHAsset;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class App {

    public static void main(String[] args) {
        String repoOwner = "Brethan";
        String repoName = "JSelfUpdater";
        String releaseTag = "v1.0"; // Tag name of the release
        String assetName = "someAsset.jar"; // Name of the release asset

        try {
            downloadReleaseAsset(new RepoInfo(repoOwner, repoName, releaseTag, assetName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Stream<GHAsset> getAssetStream(GHRelease release) throws IOException {
        return release.listAssets().toList().stream();
    }

    public static void downloadReleaseAsset(RepoInfo info) throws IOException {
        final GitHub github = GitHub.connectAnonymously();
        final GHRepository repository = github.getRepository(info.repoOwner + "/" + info.repoName);
        final GHRelease release = repository.getReleaseByTagName(info.releaseTag);
        final GHAsset ass = getAssetStream(release)
                .filter(a -> a.getName().equals(info.assetName))
                .findAny()
                .orElseThrow();
        //
        ass.getBrowserDownloadUrl();

        System.out.println("Release asset not found.");
    }
}

class RepoInfo {
    String repoOwner;
    String repoName;
    String releaseTag;
    String assetName;

    public RepoInfo(String repoOwner, String repoName, String releaseTag, String assetName) {
        this.repoOwner = repoOwner;
        this.repoName = repoName;
        this.releaseTag = releaseTag;
        this.assetName = assetName;
    }
}
