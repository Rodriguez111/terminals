package terminals.storage;

import terminals.models.Registration;
import terminals.models.User;

import java.util.List;
import java.util.Map;

public interface RegistrationStorage {

    List<Registration> findAllEntriesForTheLastDay(String yesterday);

    void addEntry(Registration entry);

    void updateEntry(int id, int whoReceivedId);

    int findIdByField(Map<String, String> parameters);

    List<Registration> findEntriesByFilter(Map<String, String> parameters);
}
