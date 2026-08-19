/** Converts Probe task commands into the appropriate task subtype. */
public class TaskParser {
    /** Parses a {@code todo}, {@code deadline}, or {@code event} command. */
    public Task parse(String command) throws ProbeException {
        if (command.startsWith("todo")) {
            String description = command.substring(4).trim();
            if (description.isBlank()) {
                throw new ProbeException("A todo description cannot be empty.");
            }
            return new Todo(description);
        }

        if (command.startsWith("deadline")) {
            String[] parts = command.substring(8).trim().split(" /by ", 2);
            if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
                throw new ProbeException(
                        "A deadline must include a description and /by a date or time.");
            }
            return new Deadline(parts[0], parts[1]);
        }

        if (command.startsWith("event")) {
            String[] parts = command.substring(5).trim().split(" /from ", 2);
            if (parts.length != 2 || parts[0].isBlank()) {
                throw new ProbeException(
                        "An event must include a description and /from a starting date or time.");
            }
            String[] times = parts[1].split(" /to ", 2);
            if (times.length != 2 || times[0].isBlank() || times[1].isBlank()) {
                throw new ProbeException(
                        "An event must include /to and an ending date or time.");
            }
            return new Event(parts[0], times[0], times[1]);
        }

        throw new ProbeException("Invalid task type. Use todo, deadline, or event.");
    }
}
