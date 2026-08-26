package probe.task;

import java.time.LocalDateTime;
import probe.parser.DateTimeParser;

public class Deadline extends Task {
    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: "
                + DateTimeParser.formatForDisplay(by) + ")";
    }

    @Override
    public String toFileFormat() {
        return String.format("D | %d | %s | %s", isDone ? 1 : 0, description,
                DateTimeParser.formatForStorage(by));
    }
}
