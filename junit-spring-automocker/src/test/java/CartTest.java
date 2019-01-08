import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CartTest {


    @Test
    void product_can_be_instantiated() {
        new Product();
    }

    @Test
    void cart_with_one_item() {
        Cart cart = new Cart();

        Product product = new Product();
        cart.add(product);

        assertThat(cart.getTotalPrice())
            .as("Price of a card containing one product")
            .isEqualTo(product.getPrice());
    }
}
