package ife.test.specs.user_profile.only_active_accounts_services

import groovy.util.logging.Slf4j
import ife.test.core.base.ClientsData
import ife.test.core.base.IfeSpec
import ife.test.core.base.common.header.user_profile.contact_channel.ContactChannel
import ife.test.core.tabs.dashboard.DashboardPage
import ife.test.core.tabs.new_solr_search.NewSearchPage
import spock.lang.Unroll

import static ife.test.core.base.common.header.user_profile.settings.SettingsButton.NO
import static ife.test.core.base.common.header.user_profile.settings.SettingsType.ACTIVE_ACCOUNTS_AND_SERVICES

@Unroll
@Slf4j
class InactiveAccountsFilterNo extends IfeSpec {

    static int TIMEOUT = 5
    static def clientsData = new ClientsData()
    static def client_inactive_services = clientsData.INDIVIDUAL_PREPAID_CLIENT_8
    static def client_disconnected_services = clientsData.INDIVIDUAL_CLIENT_2
    static def client_suspended_services = clientsData.SUSPENDED_MIX_CLIENT
    static def client_transferred_services = clientsData.INDIVIDUAL_CLIENT_21

    def 'Check if filter for active accounts/services works properly for not activated service.'() {

        when:
        def dashboardPage = to(DashboardPage)

        and:
        dashboardPage.header.userProfileCard.contactChannel.changeChannel(channel)

        and:
        dashboardPage.header.quickSearch.quickCustomerSearch(client_inactive_services.msisdn_2, client_inactive_services.partyId)
        log.info('Search button with value {} clicked', client_inactive_services.partyId)

        then:
        def newSearchPage = at(NewSearchPage)

        when:
        newSearchPage.header.clickOnHeaderUserProfileIfNotDisplayed()

        and:
        newSearchPage.header.userProfileCard.settingsButtonClick()

        and:
        newSearchPage.header.headerUserProfile.userProfileSettings.waitAndClickOnSettingsButton(ACTIVE_ACCOUNTS_AND_SERVICES, NO)
        log.info('Setting: "Active accounts/services are not displayed" is active')

        then:
        waitFor(TIMEOUT) {
            newSearchPage.customerResults.msisdn.displayed
            newSearchPage.customerResults.active.text() == '0 z 1'
        }
        log.info('Inactive customer msisdn is not displayed.')

        where:
        channel            | msisdn                            | partyId
        ContactChannel.CC  | client_inactive_services.msisdn_2 | client_inactive_services.partyId
        ContactChannel.POS | client_inactive_services.msisdn_2 | client_inactive_services.partyId
        ContactChannel.ADB | client_inactive_services.msisdn_2 | client_inactive_services.partyId
    }

    def 'Check if filter for active accounts/services works properly for disconnected services.'() {

        when:
        def dashboardPage = at(DashboardPage)

        and:
        dashboardPage.header.userProfileCard.contactChannel.changeChannel(channel)

        and:
        dashboardPage.localModal.waitAndCloseLocalModal()

        and:
        dashboardPage.header.clickOnHeaderUserProfileIfNotDisplayed()

        and:
        dashboardPage.header.quickSearch.quickCustomerSearch(client_disconnected_services.msisdn, client_disconnected_services.partyId)
        log.info('Search button with value {} clicked', client_disconnected_services.partyId)

        then:
        def newSearchPage = at(NewSearchPage)

        when:
        newSearchPage.header.clickOnHeaderUserProfileIfNotDisplayed()

        and:
        newSearchPage.header.userProfileCard.settingsButtonClick()

        and:
        newSearchPage.header.headerUserProfile.userProfileSettings.waitAndClickOnSettingsButton(ACTIVE_ACCOUNTS_AND_SERVICES, NO)
        log.info('Setting: "Active accounts/services are not displayed" is active')

        then:
        waitFor(TIMEOUT) {
            newSearchPage.customerResults.getMsisdnRow(1,0).text() == '48790128177'
            newSearchPage.customerResults.getMsisdnRow(2,0).text() == '48790128286'
            newSearchPage.customerResults.active.text() == '1 z 3'
        }
        log.info('Inactive customer msisdn are not displayed.')

        where:
        channel            | partyId                              | accountNo
        ContactChannel.CC  | client_disconnected_services.partyId | client_disconnected_services.msisdn
        ContactChannel.POS | client_disconnected_services.partyId | client_disconnected_services.msisdn
        ContactChannel.ADB | client_disconnected_services.partyId | client_disconnected_services.msisdn

    }

    def 'Check if filter for active accounts/services works properly for suspended service.'() {

        when:
        def dashboardPage = at(DashboardPage)

        and:
        dashboardPage.header.userProfileCard.contactChannel.changeChannel(channel)

        and:
        dashboardPage.localModal.waitAndCloseLocalModal()

        and:
        dashboardPage.header.clickOnHeaderUserProfileIfNotDisplayed()

        and:
        dashboardPage.header.quickSearch.quickCustomerSearch(client_suspended_services.msisdn, client_suspended_services.partyId)
        log.info('Search button with value {} clicked', client_suspended_services.partyId)

        then:
        def newSearchPage = at(NewSearchPage)

        when:
        newSearchPage.header.clickOnHeaderUserProfileIfNotDisplayed()

        and:
        newSearchPage.header.userProfileCard.settingsButtonClick()

        and:
        newSearchPage.header.headerUserProfile.userProfileSettings.waitAndClickOnSettingsButton(ACTIVE_ACCOUNTS_AND_SERVICES, NO)
        log.info('Setting: "Active accounts/services are not displayed" is active')

        then:
        waitFor(TIMEOUT) {
            newSearchPage.customerResults.msisdn.displayed
            newSearchPage.customerResults.active.text() == '0 z 1'
        }
        log.info('Inactive customer msisdn are not displayed.')

        where:
        channel            | partyId                           | accountNo
        ContactChannel.CC  | client_suspended_services.partyId | client_suspended_services.msisdn
        ContactChannel.POS | client_suspended_services.partyId | client_suspended_services.msisdn
        ContactChannel.ADB | client_suspended_services.partyId | client_suspended_services.msisdn
    }

    def 'Check if filter for active accounts/services works properly for transferred service.'() {

        when:
        def dashboardPage = at(DashboardPage)

        and:
        dashboardPage.header.userProfileCard.contactChannel.changeChannel(channel)

        and:
        dashboardPage.localModal.waitAndCloseLocalModal()

        and:
        dashboardPage.header.clickOnHeaderUserProfileIfNotDisplayed()

        and:
        dashboardPage.header.quickSearch.quickCustomerSearch(client_transferred_services.msisdn, client_transferred_services.partyId)
        log.info('Search button with value {} clicked', client_transferred_services.partyId)

        then:
        def newSearchPage = at(NewSearchPage)

        when:
        newSearchPage.header.clickOnHeaderUserProfileIfNotDisplayed()

        and:
        newSearchPage.header.userProfileCard.settingsButtonClick()

        and:
        newSearchPage.header.headerUserProfile.userProfileSettings.waitAndClickOnSettingsButton(ACTIVE_ACCOUNTS_AND_SERVICES, NO)
        log.info('Setting: "Active accounts/services are not displayed" is active')

        then:
        waitFor(TIMEOUT) {
            newSearchPage.customerResults.getMsisdnRow(9,0).text() == '48111021612'
            newSearchPage.customerResults.active.text() == '9 z 11'
        }
        log.info('Transferred customer msisdn are not displayed.')

        where:
        channel            | partyId                             | msisdn
        ContactChannel.CC  | client_transferred_services.partyId | client_transferred_services.msisdn
        ContactChannel.POS | client_transferred_services.partyId | client_transferred_services.msisdn
        ContactChannel.ADB | client_transferred_services.partyId | client_transferred_services.msisdn


    }
}
