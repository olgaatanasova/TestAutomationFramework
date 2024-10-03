package saucedemotests.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import saucedemotests.core.SauceDemoBaseWebTest;
import saucedemotests.enums.TestData;

public class ProductsTests extends SauceDemoBaseWebTest {
    public static final String ERROR_WHEN_ADDING_PRODUCTS_TO_CART = "There was problem with the adding of the products to the cart!";
    public final String BACKPACK_TITLE = "Sauce Labs Backpack";
    public final String SHIRT_TITLE = "Sauce Labs Bolt T-Shirt";

    @BeforeEach
    public void beforeTest() {
        loginPage.navigate();
        loginPage.submitLoginForm(TestData.STANDARD_USER_USERNAME.getValue(), TestData.STANDARD_USER_PASSWORD.getValue());
        inventoryPage.waitForPageTitle();
    }

    @Test
    public void productAddedToShoppingCart_when_addToCart() {
        // Add products to shopping cart
        inventoryPage.addProductsByTitle(BACKPACK_TITLE, SHIRT_TITLE);

        // Go to shopping cart
        inventoryPage.clickShoppingCartLink();

        // Assert Items and Totals
        Assertions.assertEquals(2, shoppingCartPage.getShoppingCartItems().size(), ERROR_WHEN_ADDING_PRODUCTS_TO_CART);
    }

    @Test
    public void userDetailsAdded_when_checkoutWithValidInformation() {
        // Add products to shopping cart
        inventoryPage.addProductsByTitle(BACKPACK_TITLE);

        // Go to shopping cart
        inventoryPage.clickShoppingCartLink();

        // Go to checkout
        shoppingCartPage.clickCheckout();

        // Fill form
        checkoutYourInformationPage.fillShippingDetails("John", "Doe", "12345");

        // Continue
        checkoutYourInformationPage.clickContinue();

        // Assert Cart Items number
        Assertions.assertEquals(1, checkoutOverviewPage.getShoppingCartItems().size(), "Product count mismatch.");

        // Calculate expected total cost
        String totalLabel = checkoutOverviewPage.getTotalLabelText();

        // Assert Cart Items Titles and total cost
        Assertions.assertTrue(totalLabel.contains("Total"), "Total amount is not calculated correctly.");
    }

    @Test
    public void orderCompleted_when_addProduct_and_checkout_withConfirm() {
        // Add Backpack and T-shirt to shopping cart
        inventoryPage.addProductsByTitle(BACKPACK_TITLE, SHIRT_TITLE);

        // Click on shopping Cart
        inventoryPage.clickShoppingCartLink();

        // Go to Billing Info
        shoppingCartPage.clickCheckout();

        // Fill form
        checkoutYourInformationPage.fillShippingDetails("John", "Doe", "12345");

        // Continue
        checkoutYourInformationPage.clickContinue();

        // Assert Items removed from Shopping Cart
        checkoutOverviewPage.clickFinish();
        Assertions.assertTrue(checkoutCompletePage.getPageUrl().contains("checkout-complete.html"), "Order was not successfully completed.");

        // Complete Order
        checkoutCompletePage.navigate();

        // Assert Shopping cart is empty
        Assertions.assertTrue(inventoryPage.getShoppingCartItemsNumber() == 0, "Cart is not empty after order completion.");
    }
}