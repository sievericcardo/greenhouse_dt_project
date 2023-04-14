import no.uio.microobject.main.Settings;
import no.uio.microobject.runtime.REPL;

import java.util.HashMap;

public class SmolRunner {
    public static void main(String[] args) {
        // get current time in milliseconds
        long timeMilli = System.currentTimeMillis();

        Settings settings = new Settings(true, true,
            "C:\\Users\\Gianl\\Desktop\\UniTo\\internship\\greenhouse_twin_project" +
                "\\smol_runner\\src\\main\\kg_output",
            "C:\\Users\\Gianl\\Desktop\\UniTo\\internship\\greenhouse_twin_project" +
                "\\smol_runner\\src\\main\\resources\\greenhouse.ttl",
            "ast:",
            "https://github.com/Edkamb/SemanticObjects/Program#",
            "https://github.com/Edkamb/SemanticObjects/Run" + timeMilli + "#",
            "https://github.com/Edkamb/SemanticObjects#",
            new HashMap<String, String>(),
            false);

        REPL repl = new REPL(settings);

        repl.command("verbose", "true");
        repl.command("read",
            "\"C:\\Users\\Gianl\\Desktop\\UniTo\\internship\\greenhouse_twin_project" +
                "\\smol_runner\\src\\main\\resources\\greenhouse.smol\"");
        repl.command("auto", "");
        repl.command("dump", "out.ttl");
    }
}
