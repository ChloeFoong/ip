/** Represents a task that should be completed by a specified time. */
public class Deadline extends Task {
    /** The user-provided deadline text. */
    protected String by;

    /** Creates a deadline task. */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
