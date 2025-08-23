package edu.eci.arsw.blacklistvalidator;
import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.*;

public class HostBlackListsValidatorThread extends Thread {
    private int startIndex;
    private int endIndex;
    private String ipAddress;
    private HostBlacklistsDataSourceFacade facade;
    private List<Integer> blackListOccurrences;
    private int checkedLists;

    public HostBlackListsValidatorThread(int startIndex, int endIndex, String ipAddress, HostBlacklistsDataSourceFacade facade) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.ipAddress = ipAddress;
        this.facade = facade;
        this.blackListOccurrences = new ArrayList<>();
        this.checkedLists = 0;
    }

    @Override
    public void run() {
        for (int i = startIndex; i < endIndex; i++) {
            checkedLists++;
            if (facade.isInBlackListServer(i, ipAddress)) {
                blackListOccurrences.add(i);
            }
        }
    }

    public List<Integer> getBlackListOccurrences() {
        return blackListOccurrences;
    }

    public int getCheckedLists() {
        return checkedLists;
    }
}