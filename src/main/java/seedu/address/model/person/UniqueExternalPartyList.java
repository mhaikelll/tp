package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * A list of external parties that enforces uniqueness between its elements and does not allow nulls.
 * An external party is considered unique by comparing using {@code Person#isSamePerson(Person)}.
 * As such, adding and updating of external parties uses Person#isSamePerson(Person) for equality so as to ensure that
 * the external parties being added or updated is unique in terms of identity in the UniqueExternalPartyList.
 * However, the removal of an external party uses Person#equals(Object) so as to ensure that
 * the person with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Person#isSamePerson(Person)
 */
public class UniqueExternalPartyList implements Iterable<ExternalParty> {
    private final ObservableList<ExternalParty> internalList = FXCollections.observableArrayList();
    private final ObservableList<ExternalParty> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent external party as the given argument.
     */
    public boolean contains(ExternalParty toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSamePerson);
    }

    /**
     * Adds an external party to the list.
     * The external party must not already exist in the list.
     */
    public void add(ExternalParty toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the external party {@code target} in the list with {@code editedExternalParty}.
     * {@code target} must exist in the list.
     * The external party identity of {@code editedExternalParty} must not be the same
     * as another existing external party in the list.
     */
    public void setExternalParty(ExternalParty target, ExternalParty editedExternalParty) {
        requireAllNonNull(target, editedExternalParty);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        if (!target.isSamePerson(editedExternalParty) && contains(editedExternalParty)) {
            throw new DuplicatePersonException();
        }

        internalList.set(index, editedExternalParty);
    }

    /**
     * Removes the equivalent external party from the list.
     * The external party must exist in the list.
     */
    public void remove(ExternalParty toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new PersonNotFoundException();
        }
    }

    public void setExternalParties(UniqueExternalPartyList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code externalParty}.
     * {@code externalParty} must not contain duplicate persons.
     */
    public void setExternalParties(List<ExternalParty> externalParty) {
        requireAllNonNull(externalParty);
        if (!externalPartyAreUnique(externalParty)) {
            throw new DuplicatePersonException();
        }

        internalList.setAll(externalParty);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ExternalParty> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<ExternalParty> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueExternalPartyList)) {
            return false;
        }

        UniqueExternalPartyList otherUniqueExternalPartyList = (UniqueExternalPartyList) other;
        return internalList.equals(otherUniqueExternalPartyList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code externalParty} contains only unique external party.
     */
    private boolean externalPartyAreUnique(List<ExternalParty> externalParty) {
        for (int i = 0; i < externalParty.size() - 1; i++) {
            for (int j = i + 1; j < externalParty.size(); j++) {
                if (externalParty.get(i).isSamePerson(externalParty.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

}
