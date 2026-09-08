package probe.task;

import java.util.Objects;

/**
 * Represents a label assigned to a task.
 */
public final class Tag {
    private final String name;

    /**
     * Creates a tag from a name.
     *
     * @param name Tag name, with or without a leading {@code #}.
     */
    public Tag(String name) {
        String normalizedName = name.trim();
        if (normalizedName.startsWith("#")) {
            normalizedName = normalizedName.substring(1);
        }
        if (normalizedName.isBlank()) {
            throw new IllegalArgumentException("Tag name cannot be empty.");
        }
        this.name = normalizedName;
    }

    /**
     * Returns the tag name without its display prefix.
     *
     * @return Tag name.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "#" + this.name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Tag)) {
            return false;
        }
        Tag otherTag = (Tag) other;
        return name.equals(otherTag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
