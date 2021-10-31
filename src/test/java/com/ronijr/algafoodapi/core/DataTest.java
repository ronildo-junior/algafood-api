package com.ronijr.algafoodapi.core;

import com.ronijr.algafoodapi.domain.model.*;
import com.ronijr.algafoodapi.domain.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public final class DataTest {
    private final CuisineRepository cuisineRepository;
    private final RestaurantRepository restaurantRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final List<String> uuidList = new ArrayList<>();
    private final PasswordEncoder passwordEncoder;
    public final static Integer CUISINE_COUNT = 6;
    public final static Integer CUISINE_RELATIONSHIP_BEGIN = 3;
    public final static Integer CUISINE_NON_EXISTENT_ID = CUISINE_COUNT + 1;
    public final static Integer RESTAURANT_COUNT = 6;
    public final static Integer RESTAURANT_RELATIONSHIP_BEGIN = 3;
    public final static Integer RESTAURANT_NON_EXISTENT_ID = RESTAURANT_COUNT + 1;
    public final static Integer STATE_COUNT = 6;
    public final static Integer STATE_RELATIONSHIP_BEGIN = 3;
    public final static Integer STATE_NON_EXISTENT_ID = STATE_COUNT + 1;
    public final static Integer CITY_COUNT = 6;
    public final static Integer CITY_RELATIONSHIP_BEGIN = 3;
    public final static Integer CITY_NON_EXISTENT_ID = CITY_COUNT + 1;
    public final static Integer PAYMENT_METHOD_COUNT = 6;
    public final static Integer PAYMENT_METHOD_RELATIONSHIP_BEGIN = 3;
    public final static Integer PAYMENT_METHOD_NON_EXISTENT_ID = PAYMENT_METHOD_COUNT + 1;
    public final static Integer USER_GROUP_COUNT = 6;
    public final static Integer USER_GROUP_RELATIONSHIP_BEGIN = 3;
    public final static Integer USER_GROUP_NON_EXISTENT_ID = USER_GROUP_COUNT + 1;
    public final static Integer USER_COUNT = 6;
    public final static Integer USER_RELATIONSHIP_BEGIN = 3;
    public final static Integer USER_NON_EXISTENT_ID = USER_COUNT + 1;
    public final static Integer ORDER_COUNT = 6;
    public final static Integer ORDER_NON_EXISTENT_ID = ORDER_COUNT + 1;

    public Cuisine createCuisine(int id) {
        Cuisine cuisine = Cuisine.builder().name(getCuisineName(id)).build();
        return cuisineRepository.save(cuisine);
    }

    public State createState(int id) {
        State state = State.builder().
                name(getStateName(id)).
                abbreviation("XX").
                build();
        return stateRepository.save(state);
    }

    public City createCity(int id) {
        City city = City.builder().
                name(getCityName(id)).
                state(createState(id)).
                build();
        return cityRepository.save(city);
    }

    public City createCity(int id, State state) {
        City city = City.builder().
                name(getCityName(id)).
                state(state).
                build();
        return cityRepository.save(city);
    }

    public Address createAddress(int id) {
        return Address.builder().
                postalCode("" + id).
                addressName("Street " + id).
                number("" + id).
                neighborhood("Neighborhood " + id).
                city(createCity(id)).
                build();
    }

    public Restaurant createRestaurant(int id) {
        Restaurant restaurant = Restaurant.builder().
                name(getRestaurantName(id)).
                deliveryFee(new BigDecimal("" + id)).
                cuisine(createCuisine(id)).
                address(createAddress(id)).
                build();
        return restaurantRepository.save(restaurant);
    }

    public Restaurant createRestaurant(int id, Cuisine cuisine) {
        Restaurant restaurant = Restaurant.builder().
                name(getRestaurantName(id)).
                deliveryFee(new BigDecimal("" + id)).
                cuisine(cuisine).
                address(createAddress(id)).
                build();
        return restaurantRepository.save(restaurant);
    }

    public Restaurant createRestaurant(int id, Cuisine cuisine, Address address) {
        Restaurant restaurant = Restaurant.builder().
                name(getRestaurantName(id)).
                deliveryFee(new BigDecimal("" + id)).
                cuisine(cuisine).
                address(address).
                build();
        return restaurantRepository.save(restaurant);
    }

    public PaymentMethod createPaymentMethod(int id) {
        PaymentMethod paymentMethod = PaymentMethod.builder().
                description(getPaymentMethodDescription(id)).
                build();
        return paymentMethodRepository.save(paymentMethod);
    }

    public Restaurant addPaymentMethodRestaurant(Restaurant restaurant, PaymentMethod paymentMethod) {
        restaurant.addPaymentMethod(paymentMethod);
        return restaurantRepository.save(restaurant);
    }

    public UserGroup createUserGroup(int id) {
        UserGroup userGroup = UserGroup.builder().
                name(getUserGroupName(id)).
                build();
        return userGroupRepository.save(userGroup);
    }

    public User createUser(int id) {
        User user = User.builder().
                name(getUserName(id)).
                email("email" + id + "@gmail.com").
                password(passwordEncoder.encode("123")).
                build();
        return userRepository.save(user);
    }

    public Product createProduct(int id, Restaurant restaurant) {
        Product product = Product.builder().
                name("Product Test " + id).
                description("Description Test " + id).
                price(BigDecimal.TEN).
                restaurant(restaurant).
                build();
        return productRepository.save(product);
    }

    public Order createOrder(int id, String code, User user, Restaurant restaurant, PaymentMethod paymentMethod) {
        Order order = Order.builder().
                code(code).
                deliveredAt(OffsetDateTime.now()).
                status(OrderStatus.CREATED).
                deliveryFee(restaurant.getDeliveryFee()).
                restaurant(restaurant).
                paymentMethod(paymentMethod).
                customer(user).
                deliveryAddress(restaurant.getAddress()).
                build();
        Product product = createProduct(id, restaurant);
        OrderItem item = OrderItem.builder().
                order(order).
                amount(BigDecimal.ONE).
                price(BigDecimal.TEN).
                product(product).
                build();
        order.getItems().add(item);
        order.calculateTotal();
        return orderRepository.save(order);
    }

    public String getCuisineName(int id){
        return "Cuisine Test " + id;
    }

    public String getRestaurantName(int id){
        return "Restaurant Test " + id;
    }

    public String getStateName(int id){
        return "State Test " + id;
    }

    public String getCityName(int id){
        return "City Test " + id;
    }

    public String getPaymentMethodDescription(int id){
        return "Payment Method Test " + id;
    }

    public String getUserGroupName(int id){
        return "User Group " + id;
    }

    public String getUserName(int id){
        return "User " + id;
    }

    public void createCuisineBaseData() {
        for (int i = 1; i <= CUISINE_COUNT; i++) {
            Cuisine cuisine = createCuisine(i);
            if (i >= CUISINE_RELATIONSHIP_BEGIN) {
                createRestaurant(i + 1 - CUISINE_RELATIONSHIP_BEGIN, cuisine);
            }
        }
    }

    public void createRestaurantBaseData() {
        for (int i = 1; i <= RESTAURANT_COUNT; i++) {
            Restaurant restaurant = createRestaurant(i);
            if (i >= RESTAURANT_RELATIONSHIP_BEGIN) {
                User user = createUser(i + 1 - RESTAURANT_RELATIONSHIP_BEGIN);
                restaurant.addManager(user);
            }
        }
    }

    public void createStateBaseData() {
        for (int i = 1; i <= STATE_COUNT; i++) {
            State state = createState(i);
            if (i >= STATE_RELATIONSHIP_BEGIN) {
                createCity(i + 1 - STATE_RELATIONSHIP_BEGIN, state);
            }
        }
    }

    public void createCityBaseData() {
        for (int i = 1; i <= RESTAURANT_COUNT; i++) {
            createCity(i);
        }
    }

    public void createPaymentMethodBaseData() {
        for (int i = 1; i <= PAYMENT_METHOD_COUNT; i++) {
            PaymentMethod paymentMethod = createPaymentMethod(i);
            if (i >= PAYMENT_METHOD_RELATIONSHIP_BEGIN) {
                Restaurant restaurant = createRestaurant(i + 1 - PAYMENT_METHOD_RELATIONSHIP_BEGIN);
                addPaymentMethodRestaurant(restaurant, paymentMethod);
            }
        }
    }

    public void createUserGroupBaseData() {
        for (int i = 1; i <= USER_GROUP_COUNT; i++) {
            createUserGroup(i);
        }
    }

    public void createUserBaseData() {
        for (int i = 1; i <= USER_COUNT; i++) {
            User user = createUser(i);
            if (i >= USER_RELATIONSHIP_BEGIN) {
                UserGroup userGroup = createUserGroup(i);
                user.linkUserGroup(userGroup);
                userRepository.save(user);
            }
        }
    }

    public String getUUID(int id) {
        return uuidList.get(id);
    }

    public void createOrderBaseData() {
        uuidList.clear();
        for (int i = 1; i <= ORDER_COUNT; i++) {
            User user = createUser(i);
            Restaurant restaurant = createRestaurant(i);
            PaymentMethod paymentMethod = createPaymentMethod(i);
            addPaymentMethodRestaurant(restaurant, paymentMethod);
            String uuid = UUID.randomUUID().toString();
            uuidList.add(uuid);
            Order order = createOrder(i, uuid, user, restaurant, paymentMethod);
            if (i >= USER_RELATIONSHIP_BEGIN) {
                order.confirm();
                orderRepository.save(order);
            }
        }
    }
}