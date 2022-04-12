package ife.test.specs.solr_search_cc.solr_search

import groovy.util.logging.Slf4j
import ife.test.core.base.ClientsData
import ife.test.core.base.IfeSpec
import ife.test.core.base.common.header.user_profile.contact_channel.ContactChannel
import ife.test.core.tabs.clientcontext.ClientContextPage
import ife.test.core.tabs.dashboard.DashboardPage
import ife.test.core.tabs.new_solr_search.NewSearchPage
import spock.lang.Unroll

@Unroll
@Slf4j
class AnonymousClient extends IfeSpec {

    static int TIMEOUT = 5
    static def clientsData = new ClientsData()
    static def client = clientsData.ANONYMOUS_CLIENT_1


    def 'Check if customer is an anonymous client by msisdn - for #channel channel'() {
        when:
        def dashboardPage = to(DashboardPage)

        and:
        dashboardPage.header.userProfileCard.contactChannel.changeChannel(channel)

        and:
        dashboardPage.header.quickSearch.quickCustomerSearch(client.msisdn)
        log.info('Search button with value {} clicked', client.msisdn)

        then:
        def newSearchPage = at(NewSearchPage)

        and:
        newSearchPage.customerResults.waitForLoadAndFindCustomerData()

        and:
        waitFor(TIMEOUT) {
            newSearchPage.customerResults.anonymousIcon.isDisplayed()
            newSearchPage.customerResults.veTag.isDisplayed()
            newSearchPage.customerResults.fullName.text() == client.fullName
            newSearchPage.customerResults.pesel.text() == 'Brak'
            newSearchPage.customerResults.nip.text() == ''
            newSearchPage.customerResults.regon.text() == ''
            newSearchPage.customerResults.accountNumber.text() == client.accountNo
            newSearchPage.customerResults.msisdn.text() == client.msisdn
        }

        when:
        newSearchPage.newVcard.expandVCard()

        then:
        waitFor {
            newSearchPage.newVcard.isDisplayed()
            newSearchPage.newVcard.vCardHeader.isDisplayed()
            newSearchPage.newVcard.vCardHeader.text() == 'Klient Anonimowy'
        }
        log.info('Anonymous client data shown in VCard are correct')

        when:
        newSearchPage.customerResults.clickOnServiceRow(client.msisdn)

        then:
        def clientContextPage = at(ClientContextPage)

        and:
        clientContextPage.sidebar.expandSidebarIfCollapsed()

        and:
        clientContextPage.sidebar.clientCard.waitForLoadClientData()

        and:
        waitFor(TIMEOUT) {
            clientContextPage.sidebar.sidebarHeader.checkFormattedMsisdn(clientContextPage.sidebar.sidebarHeader.clientMsisdn.text(),
                client.msisdn)
            clientContextPage.sidebar.clientCard.clientFullName.text() == client.fullName
            clientContextPage.sidebar.clientCard.accountNo.text() == client.accountNo
            clientContextPage.sidebar.clientCard.nip.text() == '-'
            clientContextPage.sidebar.clientCard.regon.text() == '-'
            clientContextPage.sidebar.clientCard.partyId.text() == '-'
        }
        log.info('Anonymous client data are correctly displayed on client card')

        where:
        channel            | accountNo        | msisdn
        ContactChannel.CC  | client.accountNo | client.msisdn
        ContactChannel.POS | client.accountNo | client.msisdn
        ContactChannel.ADB | client.accountNo | client.msisdn
    }
}
