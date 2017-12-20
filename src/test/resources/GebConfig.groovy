// default is to use htmlunit
driver = "htmlunit"
baseUrl = "http://127.0.0.1:4567"
reportsDir = "target/reports/geb"

/*environments {
    // when system property 'geb.env' is set to 'remote' use a remote Firefox driver
    remote {
        driver = {
            def remoteWebDriverServerUrl = new URL("http://example.com/webdriverserver")
            new RemoteWebDriver(remoteWebDriverServerUrl, DesiredCapabilities.firefox())
        }
    }
}*/