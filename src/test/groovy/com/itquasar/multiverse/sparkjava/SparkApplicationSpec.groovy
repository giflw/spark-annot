package com.itquasar.multiverse.sparkjava

import geb.spock.GebReportingSpec

class SparkApplicationSpec extends GebReportingSpec {

    static def app

    def setupSpec(){
        app = SparkApplication.launch()
    }

    def cleanupSpec() {
        app.stop()
    }

    def "assert hello world"() {
        when: "root path called"
        go "/"

        then: "content must be Hello World"
        driver.pageSource == "Hello World"
    }
}
