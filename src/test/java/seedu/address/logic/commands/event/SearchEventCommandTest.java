package seedu.address.logic.commands.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalEvents.DANCE_EVENT;
import static seedu.address.testutil.TypicalEvents.getEmptyAddressBook;
import static seedu.address.testutil.TypicalEvents.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.event.EventMatchesPredicate;

@SuppressWarnings("checkstyle:Regexp")
public class SearchEventCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model emptyModel = new ModelManager(getEmptyAddressBook(), new UserPrefs());

    @Test
    public void execute_nameKeyword_oneEventFound() throws CommandException {
        String expectedMessage = String.format(SearchEventCommand.MESSAGE_SUCCESS, 1);
        EventMatchesPredicate predicate = new EventMatchesPredicate(DANCE_EVENT.getEventName().fullEventName,
                null, null);
        SearchEventCommand command = new SearchEventCommand(predicate);
        CommandResult result = command.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_validStartTimeKeyword_eventFound() throws CommandException {
        String expectedMessage = String.format(SearchEventCommand.MESSAGE_SUCCESS, 1);
        EventMatchesPredicate predicate = new EventMatchesPredicate(null, DANCE_EVENT.getEventStartTime(), null);
        SearchEventCommand command = new SearchEventCommand(predicate);
        CommandResult result = command.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_multipleKeywords_multipleEventsFound() throws CommandException {
        String expectedMessage = String.format(SearchEventCommand.MESSAGE_SUCCESS, 1);
        EventMatchesPredicate predicate = new EventMatchesPredicate("e", DANCE_EVENT.getEventStartTime(), null);
        SearchEventCommand command = new SearchEventCommand(predicate);
        CommandResult result = command.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_noEventsExist_throwsCommandException() {
        EventMatchesPredicate predicate = new EventMatchesPredicate("NonExistentEvent", null, null);
        SearchEventCommand command = new SearchEventCommand(predicate);
        assertThrows(CommandException.class, () -> command.execute(emptyModel));
    }

    @Test
    public void execute_noMatchingEvents_returnsNoMatchMessage() throws CommandException {
        EventMatchesPredicate predicate = new EventMatchesPredicate("NonExistentEvent", null, null);
        SearchEventCommand command = new SearchEventCommand(predicate);
        CommandResult result = command.execute(model);
        assertEquals(SearchEventCommand.MESSAGE_NO_MATCH, result.getFeedbackToUser());
    }

    @Test
    public void equals() {
        EventMatchesPredicate firstPredicate = new EventMatchesPredicate("first", null, null);
        EventMatchesPredicate secondPredicate = new EventMatchesPredicate("second", null, null);

        SearchEventCommand firstCommand = new SearchEventCommand(firstPredicate);
        SearchEventCommand secondCommand = new SearchEventCommand(secondPredicate);

        // same object -> returns true
        assertEquals(firstCommand, firstCommand);

        // same values -> returns true
        SearchEventCommand firstCommandCopy = new SearchEventCommand(firstPredicate);
        assertEquals(firstCommand, firstCommandCopy);

        // different types -> returns false
        assertNotEquals(firstCommand, 1);

        // null -> returns false
        assertNotEquals(firstCommand, null);

        // different event -> returns false
        assertNotEquals(firstCommand, secondCommand);
    }
}

