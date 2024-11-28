package space.bxteam.commons.bukkit.inventory;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for handling {@link Material} manipulations.
 */
public final class MaterialUtil {
    private MaterialUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    /**
     * Formats the {@link Material} into a human-readable string.
     * @param material the material to format
     * @return the formatted material name
     */
    public static String format(@NotNull Material material) {
        String name = material.name().toLowerCase().replace("_", " ");
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
