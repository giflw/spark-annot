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
        go "/hello"

        then: "content must be Hello World"
        driver.pageSource == "Hello World"
    }

    def "assert hello world tmpl"() {
        when: "root path called"
        go "/hello/tmpl"

        then: "content must be Hello World from template and view route"
        $("p").firstElement().text == "Hello World"
        $("p").lastElement().text == "hello-world.template"
    }

    def "assert hello world tmpl2"() {
        when: "root path called"
        go "/hello/tmpl2"

        then: "content must be Hello World from template and view route"
        driver.pageSource == "Hello World.notSoSimple"
    }
}
