package ife.test.specs.bsp_advanced_search_cc.advanced_search.new_billing_data

import groovy.util.logging.Slf4j
import ife.test.core.base.IfeSpec
import ife.test.core.base.ClientsData
import ife.test.core.tabs.dashboard.DashboardPage
import ife.test.core.tabs.new_advanced_search.NewAdvancedSearchPage
import ife.test.core.tabs.new_solr_search.NewSearchPage

import static ife.test.core.base.common.header.user_profile.contact_channel.ContactChannel.CC

@Slf4j
class VipClient extends IfeSpec {


    static def clientsData = new ClientsData()
    static def client = clientsData.VIP_CLIENT_1

    def 'Check if the customer is a VIP client'() {

        when:
        def dashboardPage = to(DashboardPage)

        and:
        dashboardPage.header.userProfileCard.contactChannel.changeChannel(CC)

        and:
        dashboardPage.localModal.waitAndCloseLocalModal()

        then:
        waitFor { dashboardPage.header.tooltabs.newSearchTab.isDisplayed() }

        when:
        dashboardPage.header.tooltabs.newSearchTab.click()

        then:
        def newSearchPage = at(NewSearchPage)

        and:
        waitFor (4){
            newSearchPage.advancedSearchButton.isDisplayed()
        }

        when:
        newSearchPage.advancedSearchButton.click()

        then:
        def newAdvancedSearch = at(NewAdvancedSearchPage)

        when:
        newAdvancedSearch.billingDataTab.partyId.value(client.partyId)

        and:
        newAdvancedSearch.billingDataTab.searchButtonClick()
        log.info('Search button with value {} clicked', client.partyId)

        then:
        newAdvancedSearch.billingDataTab.customerResults.verifyVipIconDisplayed()

        and:
        newAdvancedSearch.billingDataTab.customerResults.verifyClientDetailsAreHidden()
        log.info('Customer {} is a VIP client', client.fullName)

    }
}
