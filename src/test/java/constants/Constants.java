package constants;

import org.openqa.selenium.By;
import util.ConfigManager;

public final class Constants {

    private Constants() {
    }

    public static final String HOME_PAGE_URL = ConfigManager.getProperty("base.url");
    public static final String LOGIN_URL = HOME_PAGE_URL + "giris";


    public static final By GET_STARTED_BUTTON = By.cssSelector(".cta-login-text");
    public static final By REFUSE_COOKIES_BUTTON = By.cssSelector(".axeptio-btn");
    public static final By SEARCH_BOX = By.name("search_query");
    public static final By CART_BUTTON = By.cssSelector(".cart-button");
    public static final By GIRIS_YAP_BUTTON = By.cssSelector(".login");
    public static final By MAIN_CONTENT = By.cssSelector(".main.b-color-1");
    public static final By NAVIGATION = By.cssSelector(".navigation");
    public static final By OPENING_WIDGET = By.cssSelector(".ab_widget_container_popin-image_close_button");


    public static final By LOADING_SPINNER = By.className("loading-spinner");
    public static final By ERROR_MESSAGE = By.className("error-message");
    
    public static final int DEFAULT_TIMEOUT = 10;
    public static final int SHORT_TIMEOUT = 5;
    public static final int LONG_TIMEOUT = 30;


} 