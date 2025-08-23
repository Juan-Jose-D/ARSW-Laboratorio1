/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */

    public List<Integer> checkHost(String ipaddress, int N) {
        LinkedList<Integer> blackListOcurrences = new LinkedList<>();
        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();
        int totalServers = skds.getRegisteredServersCount();
        int segment = totalServers / N;
        int remainder = totalServers % N;
        HostBlackListsValidatorThread[] threads = new HostBlackListsValidatorThread[N];
        int start = 0;
        for (int i = 0; i < N; i++) {
            int end = start + segment + (i < remainder ? 1 : 0);
            threads[i] = new HostBlackListsValidatorThread(start, end, ipaddress, skds);
            start = end;
        }
        for (int i = 0; i < N; i++) {
            threads[i].start();
        }
        int ocurrencesCount = 0;
        int checkedListsCount = 0;
        for (int i = 0; i < N; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            List<Integer> found = threads[i].getBlackListOccurrences();
            ocurrencesCount += found.size();
            blackListOcurrences.addAll(found);
            checkedListsCount += threads[i].getCheckedLists();
        }
        if (ocurrencesCount >= BLACK_LIST_ALARM_COUNT) {
            skds.reportAsNotTrustworthy(ipaddress);
        } else {
            skds.reportAsTrustworthy(ipaddress);
        }
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        return blackListOcurrences;
    }
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    
    
}