/** Represents a task occurring between two user-provided times. */
public class Event extends Task {
    /** The user-provided event start time. */
    protected String from;
    /** The user-provided event end time. */
    protected String to;

    /** Creates an event task. */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
    
}
