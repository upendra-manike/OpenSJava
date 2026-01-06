package io.github.upendramanike.genai;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages LLM prompts with versioning and lifecycle support.
 */
public final class PromptManager {

    private final Map<String, PromptVersion> prompts = new HashMap<>();

    private PromptManager() {
    }

    /**
     * Creates a new prompt manager.
     *
     * @return a new prompt manager
     */
    public static PromptManager create() {
        return new PromptManager();
    }

    /**
     * Registers a prompt with a version.
     *
     * @param name the prompt name
     * @param version the version
     * @param content the prompt content
     */
    public void register(String name, String version, String content) {
        prompts.put(name + ":" + version, new PromptVersion(name, version, content));
    }

    /**
     * Gets a prompt by name and version.
     *
     * @param name the prompt name
     * @param version the version
     * @return the prompt content, or null if not found
     */
    public String get(String name, String version) {
        PromptVersion prompt = prompts.get(name + ":" + version);
        return prompt != null ? prompt.getContent() : null;
    }

    /**
     * Gets the latest version of a prompt.
     *
     * @param name the prompt name
     * @return the latest prompt content, or null if not found
     */
    public String getLatest(String name) {
        return prompts.values().stream()
                .filter(p -> p.getName().equals(name))
                .max((p1, p2) -> p1.getVersion().compareTo(p2.getVersion()))
                .map(PromptVersion::getContent)
                .orElse(null);
    }

    private static class PromptVersion {
        private final String name;
        private final String version;
        private final String content;

        PromptVersion(String name, String version, String content) {
            this.name = name;
            this.version = version;
            this.content = content;
        }

        String getName() {
            return name;
        }

        String getVersion() {
            return version;
        }

        String getContent() {
            return content;
        }
    }
}


