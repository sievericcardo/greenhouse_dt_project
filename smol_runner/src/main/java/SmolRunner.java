import no.uio.microobject.main.Settings;
import no.uio.microobject.runtime.REPL;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class SmolRunner {
    static boolean verbose = true;
    static boolean materialize = false;
    static String kgOutput = "src/main/kg_output/";
    static String greenhouseAssetModel = "src/main/resources/greenhouse.ttl";
    static String domainPrefix = "http://www.semanticweb.org/gianl/ontologies/2023/1/sirius-greenhouse#";
    static String progPrefix = "https://github.com/Edkamb/SemanticObjects/Program#";
    static String runPrefix = "https://github.com/Edkamb/SemanticObjects/Run" + System.currentTimeMillis() + "#";
    static String langPrefix = "https://github.com/Edkamb/SemanticObjects#";
    static HashMap<String, String> extraPrefixes = new HashMap<>();
    static boolean useQueryType = false;

    public static void main(String[] args) {
        Settings settings = getSettings();

        REPL repl = new REPL(settings);

        repl.command("verbose", "true");

        repl.command("read",
            "src/main/resources/greenhouse.smol");

        repl.command("auto", "");
        repl.command("dump", "out.ttl");
    }

    @NotNull
    private static Settings getSettings() {
        String assetModel = getAssetModel();

        return new Settings(
            SmolRunner.verbose,
            SmolRunner.materialize,
            SmolRunner.kgOutput,
            assetModel,
            SmolRunner.domainPrefix,
            SmolRunner.progPrefix,
            SmolRunner.runPrefix,
            SmolRunner.langPrefix,
            SmolRunner.extraPrefixes,
            SmolRunner.useQueryType
        );
    }

    private static String getAssetModel() {
        // Read the asset model from the file in SmolRunner.greenhouseAssetModel
        try {
            return Files.readString(new File(SmolRunner.greenhouseAssetModel).toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
