package tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePageMethods;
import driver.Driver;

public class HomePageTest {

    HomePageMethods homePage;

    @BeforeMethod
    public void setUp() {
        Driver.setup();
        homePage = new HomePageMethods();
    }

    @AfterMethod
    public void tearDown() {
        Driver.closeDriver();
    }

    @Test
    public void homePageTitleShouldNotBeEmpty() {
        String title = homePage.getTitle();
        Assert.assertFalse(title.isEmpty(), "Title should not be empty");
    }

    @Test
    public void homePageShouldBeLoaded() {
        Assert.assertTrue(homePage.isPageLoaded(), "Home page could not loaded");
    }
}
//    @Test
//    public void homePageShouldHaveMainContent() {
//        Assert.assertTrue(homePage.hasMainContent(), "MainContent could not found");
//    }
//
//    @Test
//    public void homePageShouldHaveNavigation() {
//        Assert.assertTrue(homePage.hasNavigation(), "Navigation could not found");
//    }
//
//    @Test
//    public void homePageShouldHaveSearchBox() {
//        Assert.assertTrue(homePage.isSearchBoxDisplayed(), "Search box is not displayed");
//    }
//
//    @Test
//    public void homePageShouldHaveLoginButton() {
//        Assert.assertTrue(homePage.isLoginButtonDisplayed(), "Login button is not displayed");
//    }
//
//    @Test
//    public void homePageShouldHaveCartButton() {
//        Assert.assertTrue(homePage.isCartButtonDisplayed(), "Cart button is not diplayed");
//    }
//

//}