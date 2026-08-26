package probe.parser;

import java.time.LocalDateTime;
import probe.ProbeException;
import probe.task.Deadline;
import probe.task.Event;
import probe.task.Task;
import probe.task.Todo;

public class Parser {
    public Task parseTask(String command) throws ProbeException {
        if (command.startsWith("todo")) {
            String description = command.substring(4).trim();
            if (description.isBlank()) throw new ProbeException("A todo description cannot be empty.");
            return new Todo(description);
        }
        if (command.startsWith("deadline")) {
            String[] parts = command.substring(8).trim().split(" /by ", 2);
            if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) throw new ProbeException("A deadline must include a description and /by a date or time.");
            try { return new Deadline(parts[0], DateTimeParser.parse(parts[1])); }
            catch (IllegalArgumentException e) { throw new ProbeException(e.getMessage()); }
        }
        String[] parts = command.substring(5).trim().split(" /from ", 2);
        if (parts.length != 2 || parts[0].isBlank()) throw new ProbeException("An event must include a description and /from a starting date or time.");
        String[] times = parts[1].split(" /to ", 2);
        if (times.length != 2 || times[0].isBlank() || times[1].isBlank()) throw new ProbeException("An event must include /to and an ending date or time.");
        try {
            LocalDateTime from = DateTimeParser.parse(times[0]);
            LocalDateTime to = DateTimeParser.parse(times[1]);
            if (to.isBefore(from)) throw new ProbeException("An event cannot end before it starts.");
            return new Event(parts[0], from, to);
        } catch (IllegalArgumentException e) { throw new ProbeException(e.getMessage()); }
    }
    public int parseNumber(String command, String error) throws ProbeException {
        String[] parts = command.split(" ");
        if (parts.length != 2) throw new ProbeException(error);
        try { return Integer.parseInt(parts[1]); }
        catch (NumberFormatException e) { throw new ProbeException("The task number must be a number."); }
    }
}
