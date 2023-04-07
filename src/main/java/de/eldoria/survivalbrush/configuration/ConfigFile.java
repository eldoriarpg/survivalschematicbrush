package de.eldoria.survivalbrush.configuration;

@SuppressWarnings("FieldMayBeFinal")
public class ConfigFile {
    private BlockSettings blockSettings = new BlockSettings();

    public BlockSettings blockSettings() {
        return blockSettings;
    }

    public void blockSettings(BlockSettings blockSettings) {
        this.blockSettings = blockSettings;
    }
}
