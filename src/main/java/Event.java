import java.time.LocalDateTime;

public class Event extends Task{
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: "
                + DateTimeParser.formatForDisplay(from) + " to: "
                + DateTimeParser.formatForDisplay(to) + ")";
    }
    
    @Override
    public String toFileFormat() {
        return String.format("E | %d | %s | %s | %s", isDone ? 1 : 0, description,
                DateTimeParser.formatForStorage(from), DateTimeParser.formatForStorage(to));
    }
}
