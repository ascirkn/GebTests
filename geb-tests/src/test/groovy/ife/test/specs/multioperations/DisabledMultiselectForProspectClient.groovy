package ife.test.specs.multioperations

import geb.module.FormElement
import groovy.util.logging.Slf4j
import ife.test.core.base.IfeSpec
import ife.test.core.base.common.header.user_profile.contact_channel.ContactChannel
import ife.test.core.tabs.dashboard.DashboardPage
import ife.test.core.tabs.new_solr_search.NewSearchPage

@Slf4j
class DisabledMultiselectForProspectClient extends IfeSpec {

    static int TIMEOUT = 5

    def 'Verify disabled multiselect button for prospect client'() {

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
        NewSearchPage newSearchPage = at(NewSearchPage)

        and:
        newSearchPage.customerResults.findProspectClient()

        and:
        waitFor(TIMEOUT) {
            newSearchPage.multiContextSelectButton.isDisplayed()
            newSearchPage.multiContextSelectButton.module(FormElement).disabled
        }
        log.info('Multiselect button is displayed and disabled')
    }
}
