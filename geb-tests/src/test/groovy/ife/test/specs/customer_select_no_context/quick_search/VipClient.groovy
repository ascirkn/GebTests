package ife.test.specs.customer_select_no_context.quick_search

import groovy.util.logging.Slf4j
import ife.test.core.base.ClientsData
import ife.test.core.base.IfeSpec
import ife.test.core.base.common.header.user_profile.contact_channel.ContactChannel
import ife.test.core.tabs.clientcontext.ClientContextPage
import ife.test.core.tabs.dashboard.DashboardPage
import ife.test.core.tabs.new_solr_search.NewSearchPage

import static ife.test.core.tabs.clientcontext.sidebar.no_context_customer_search.NoContextCustomerSearchModule.InputType.QUICK_SEARCH_INPUT
import static ife.test.core.tabs.clientcontext.sidebar.no_context_customer_search.NoContextCustomerSearchModule.ValidationType.CorrectValidation

@Slf4j
class VipClient extends IfeSpec {

    def clientsData = new ClientsData()
    def client = clientsData.VIP_CLIENT_1
    int TIMEOUT = 5

    def 'Check if the customer is a VIP client'() {

        when:
        def dashboardPage = to(DashboardPage)

        and:
        dashboardPage.header.userProfileCard.contactChannel.changeChannel(ContactChannel.CC)

        then:
        waitFor { dashboardPage.header.tooltabs.newSearchTab.isDisplayed() }

        when:
        dashboardPage.header.tooltabs.newSearchTab.click()
        log.info('New search tab clicked')

        then:
        def newSearchPage = at(NewSearchPage)

        and:
        newSearchPage.customerResults.findProspectClient()

        when:
        newSearchPage.customerResults.clickOnServiceRow()
        log.info('Context button clicked')

        then:
        at(ClientContextPage)

        and:
        waitFor(TIMEOUT) {
            dashboardPage.header.tooltabs.findTabWithLabel('Nowy klient').isDisplayed()
            dashboardPage.noContextCustomerSearch.chooseClientButton.isDisplayed()
        }

        when:
        dashboardPage.noContextCustomerSearch.chooseClientButton.click()
        log.info('Choose client button clicked')

        then:
        waitFor(TIMEOUT) { dashboardPage.noContextCustomerSearch.quickSearchInput.isDisplayed() }

        when:
        dashboardPage.noContextCustomerSearch.quickSearchInput.value(client.nip)

        then:
        waitFor(TIMEOUT) {
            dashboardPage.noContextCustomerSearch.getValidationCheck(
                CorrectValidation, 'Wpisano poprawny NIP', QUICK_SEARCH_INPUT).isDisplayed()
        }

        when:
        dashboardPage.noContextCustomerSearch.searchClientButton.click()

        then:
        dashboardPage.noContextCustomerSearch.verifyHiddenVipClientData()
        log.info('Customer {} is a VIP client', client.fullName)

    }
}
