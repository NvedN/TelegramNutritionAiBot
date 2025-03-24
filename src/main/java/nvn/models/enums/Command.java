package nvn.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {
  START("/start"),
  NEW("/new"),
  RESET("/reset"),
  OLD("/old"),
  REPORT("/report"),
  STOP("/stop"),
  UNKNOWN("");

  private final String command;

  public static Command fromString(String commandStr) {
    for (Command command : values()) {
      if (command.command.equalsIgnoreCase(commandStr)) {
        return command;
      }
    }
    return UNKNOWN;
  }
}
