package com.midtier.bonmunch.dummy.service;

import com.midtier.bonmunch.dummy.model.Order;
import com.midtier.bonmunch.dummy.model.Person;
import com.midtier.bonmunch.dummy.model.RestaurantAdminInfo;
import com.midtier.bonmunch.dummy.repository.dao.OrderRepository123;
import com.midtier.bonmunch.dummy.repository.dao.ReactivePersonRepository;
import com.midtier.bonmunch.dummy.repository.dao.ReactivePersonRepository2;
import com.midtier.bonmunch.dummy.model.CustomerInfo;
import com.midtier.bonmunch.dummy.model.Person2;
import com.midtier.bonmunch.dummy.model.Gallery;
import com.midtier.bonmunch.model.Customer;
import com.midtier.bonmunch.model.DailySummay;
import com.midtier.bonmunch.dummy.model.ItemOption;
import com.midtier.bonmunch.dummy.model.ItemOptionSection;
import com.midtier.bonmunch.model.LastOrder;
import com.midtier.bonmunch.dummy.model.MenuItem;
import com.midtier.bonmunch.model.MonthlySummary;
import com.midtier.bonmunch.dummy.model.Promotion;
import com.midtier.bonmunch.model.Restaurant;
import com.midtier.bonmunch.dummy.model.User;
import com.midtier.bonmunch.dummy.repository.dao.CustomerInfoRepository;
import com.midtier.bonmunch.dummy.repository.dao.PromotionRepository;
import com.midtier.bonmunch.repository.dao.CustomerRepository;
import com.midtier.bonmunch.repository.dao.UserRepository;
import com.midtier.bonmunch.dummy.repository.model.CustomerInfoDTO;
import com.midtier.bonmunch.repository.model.CustomerDTO;
import com.midtier.bonmunch.repository.model.OrderDTO;
import com.midtier.bonmunch.repository.model.PersonDTO;
import com.midtier.bonmunch.repository.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.ZonedDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DummyService {

    private static String UPLOAD_ROOT = "upload-dir";

    @Autowired
    ReactivePersonRepository2 personRepository2;

    @Autowired
    CustomerInfoRepository customerInfoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PromotionRepository promotionRepository;

    @Autowired
    ReactivePersonRepository personRepository;

    @Autowired
    OrderRepository123 orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    /*
    login User Object
     */
    public Mono<Person2> loginPerson2(Mono<Person2> person) {


        return person
                .map(userObject ->
                             personRepository2.findByFirstnameAndLastname
                                     (userObject.getFirstname(), userObject.getLastname()))
                .flatMap(userDTO -> this.getPerson2(userDTO));


    }

    public Mono<Person2> getPerson2(Mono<Person2> p){
        return p.map(p1 -> t(p1));
    }

    public Person2 t(Person2 p){
        return p;
    }



    private CustomerInfoDTO getCustomerInfoBuild(CustomerInfo cs) {
        return CustomerInfoDTO.builder()
                              .customerInfoId(UUID.randomUUID())
                              .customerId(cs.getCustomerId())
                              .title(cs.getTitle())
                              .description(cs.getDescription())
                              .contactUs(cs.getContactUs())
                              .createdDate(ZonedDateTime.now())
                              .updatedDate(ZonedDateTime.now())
                              .build();

    }


    /*
    Create CustomerInfoDTO object
     */

    public Mono<CustomerInfo> createCustomerInfo(Mono<CustomerInfo> customerInfo) {

        return
                customerInfo.map(cs -> getCustomerInfoBuild(cs))
                            // this is influenced by subscribeOn
                            //.doOnNext(s -> System.out.println(s + " before publishOn using thread: " + Thread.currentThread().getName()))
                            .publishOn(Schedulers.elastic())
                            .log("publisON")
                            .map(customerInfoRepository::save)
                            .publishOn(Schedulers.elastic())
                            .log("publish after save")
                            // the rest is influenced by publishOn
                            //.doOnNext(s -> System.out.println(s + " after publishOn using thread: " + Thread.currentThread().getName()))
                            .log("SubscriberON")
                            .subscribeOn(Schedulers.parallel())
                            .flatMap(customerDTO -> this.getCustomerInfoDTOBuild(customerDTO));


    }



    /*
   Get customerInfo details
    */
    public Mono<CustomerInfo> getCustomerInfo(UUID customerId) {

        return Mono.just(customerInfoRepository.findByCustomerId(customerId))
                   .flatMap(customerDTO -> this.getCustomerInfoDTOBuild(customerDTO));


    }



    public Mono<CustomerInfo> getCustomerInfoDTOBuild(Mono<CustomerInfoDTO> dto) {

        //using callable becuase want lazy mapping once the data is stream

        return dto.map(customerDto -> CustomerInfo.builder()
                                                  .customerId(customerDto.getCustomerId())
                                                  .title(customerDto.getTitle())
                                                  .description(customerDto.getDescription())
                                                  .contactUs(customerDto.getContactUs())
                                                  .createdDate(customerDto.getCreatedDate())
                                                  .updatedDate(customerDto.getUpdatedDate())
                                                  .build())
                  .log("Inside fromCallable");

    }

    public Mono<List<Gallery>> getGallery() {


        List<Gallery> d = new ArrayList<>();

        d.add(Gallery.builder()
                     .orderId(1)
                     .thumbnailSrc("https://images.unsplash.com/photo-1501854140801-50d01698950b?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max")
                     .fullSizeSrc("https://images.unsplash.com/photo-1501854140801-50d01698950b?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb")
                     .alt("1")
                     .title("first")
                     .build());

        d.add(Gallery.builder()
                     .orderId(2)
                     .thumbnailSrc("https://images.unsplash.com/photo-1554579384-2d90b36072eb?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max")
                     .fullSizeSrc("https://images.unsplash.com/photo-1554579384-2d90b36072eb?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max")
                     .alt("2")
                     .title("second")
                     .build());

        d.add(Gallery.builder()
                     .orderId(3)
                     .thumbnailSrc("https://images.unsplash.com/photo-1501854140801-50d01698950b?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max")
                     .fullSizeSrc("https://images.unsplash.com/photo-1501854140801-50d01698950b?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb")
                     .alt("3")
                     .title("third")
                     .build());

        d.add(Gallery.builder()
                     .orderId(4)
                     .thumbnailSrc("https://images.unsplash.com/photo-1546810667-b9eb524e0a0d?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max")
                     .fullSizeSrc("https://images.unsplash.com/photo-1546810667-b9eb524e0a0d?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb")
                     .alt("4")
                     .title("fourth")
                     .build());


        d.add(Gallery.builder()
                     .orderId(5)
                     .thumbnailSrc("https://images.unsplash.com/photo-1501854140801-50d01698950b?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max")
                     .fullSizeSrc("https://images.unsplash.com/photo-1501854140801-50d01698950b?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb")
                     .alt("5")
                     .title("five")
                     .build());

        d.add(Gallery.builder()
                     .orderId(6)
                     .thumbnailSrc("https://images.unsplash.com/photo-1554579384-2d90b36072eb?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max")
                     .fullSizeSrc("https://images.unsplash.com/photo-1554579384-2d90b36072eb?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max")
                     .alt("6")
                     .title("six")
                     .build());

        d.add(Gallery.builder()
                     .orderId(7)
                     .thumbnailSrc("https://images.unsplash.com/photo-1501854140801-50d01698950b?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max")
                     .fullSizeSrc("https://images.unsplash.com/photo-1501854140801-50d01698950b?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb")
                     .alt("7")
                     .title("seven")
                     .build());

        d.add(Gallery.builder()
                     .orderId(8)
                     .thumbnailSrc("https://images.unsplash.com/photo-1546810667-b9eb524e0a0d?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max")
                     .fullSizeSrc("https://images.unsplash.com/photo-1546810667-b9eb524e0a0d?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb")
                     .alt("8")
                     .title("eight")
                     .build());


        d.add(Gallery.builder()
                     .orderId(9)
                     .thumbnailSrc("https://images.unsplash.com/photo-1501854140801-50d01698950b?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max")
                     .fullSizeSrc("https://images.unsplash.com/photo-1501854140801-50d01698950b?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb")
                     .alt("9")
                     .title("nine")
                     .build());

        d.add(Gallery.builder()
                     .orderId(10)
                     .thumbnailSrc("https://images.unsplash.com/photo-1554579384-2d90b36072eb?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max")
                     .fullSizeSrc("https://images.unsplash.com/photo-1554579384-2d90b36072eb?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max")
                     .alt("10")
                     .title("ten")
                     .build());

        d.add(Gallery.builder()
                     .orderId(11)
                     .thumbnailSrc("https://images.unsplash.com/photo-1501854140801-50d01698950b?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max")
                     .fullSizeSrc("https://images.unsplash.com/photo-1501854140801-50d01698950b?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb")
                     .alt("11")
                     .title("eleven")
                     .build());

        d.add(Gallery.builder()
                     .orderId(12)
                     .thumbnailSrc("https://images.unsplash.com/photo-1546810667-b9eb524e0a0d?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max")
                     .fullSizeSrc("https://images.unsplash.com/photo-1546810667-b9eb524e0a0d?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb")
                     .alt("12")
                     .title("twelve")
                     .build());

        d.add(Gallery.builder()
                     .orderId(13)
                     .thumbnailSrc("https://images.unsplash.com/photo-1501854140801-50d01698950b?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max")
                     .fullSizeSrc("https://images.unsplash.com/photo-1501854140801-50d01698950b?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb")
                     .alt("13")
                     .title("thirteen")
                     .build());

        d.add(Gallery.builder()
                     .orderId(14)
                     .thumbnailSrc("https://images.unsplash.com/photo-1554579384-2d90b36072eb?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max")
                     .fullSizeSrc("https://images.unsplash.com/photo-1554579384-2d90b36072eb?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max")
                     .alt("14")
                     .title("fourteen")
                     .build());

        d.add(Gallery.builder()
                     .orderId(15)
                     .thumbnailSrc("https://images.unsplash.com/photo-1501854140801-50d01698950b?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max")
                     .fullSizeSrc("https://images.unsplash.com/photo-1501854140801-50d01698950b?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb")
                     .alt("15")
                     .title("fifteen")
                     .build());

        d.add(Gallery.builder()
                     .orderId(16)
                     .thumbnailSrc("https://images.unsplash.com/photo-1546810667-b9eb524e0a0d?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max")
                     .fullSizeSrc("https://images.unsplash.com/photo-1546810667-b9eb524e0a0d?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb")
                     .alt("16")
                     .title("sixteen")
                     .build());

        return Mono.just(d);


    }


    /*
Get customerInfo details
 */
    public Mono<List<MenuItem>> getMenuItems() {

        List<MenuItem> d = new ArrayList<>();
        List<ItemOptionSection> d1 = new ArrayList<>();
        List<ItemOption> d2 = new ArrayList<>();

        d2.add(ItemOption.builder()
                         .name("item option")
                         .price(5)
                         .description("itemoption 1")
                         .item_option_section_id("1")
                         .id("1")
                         .build());

        d1.add(ItemOptionSection.builder()
                                .id("1")
                                .name("itemOptionSection")
                                .description("item option secion desc")
                                .required(false)
                                .min_allowed(1)
                                .max_allowed(3)
                                .itemOption(d2)
                                .build());

        d.add(MenuItem.builder()
                      .id("1")
                      .name("item 1")
                      .price((float) 1)
                      .description("Menu item 1")
                      .itemOptionSection(d1)
                      .build());


        // line 2
        d.add(MenuItem.builder()
                      .id("2")
                      .name("item 2")
                      .price((float) 2)
                      .description("Menu item 2")
                      .build());

        return Mono.just(d);


    }




    public Mono<Promotion> createPromotion(Mono<Promotion> promotionMono) {

        return
                promotionMono.map(us -> test12(us))
                             .publishOn(Schedulers.elastic())
                             .map(promotionRepository::save)
                             .publishOn(Schedulers.elastic())
                             .subscribeOn(Schedulers.parallel())
                             .flatMap(userDTO -> userDTO);


    }

    public Promotion test12(Promotion promotion) {
        Promotion df = promotion.toBuilder()
                                .promotionId(12)
                                .build();
        return df;
    }


    public Mono<List<Promotion>> getPromotions(String restuarantId) {

        return promotionRepository.findByRestaurantId(restuarantId);
    }

/*    public Mono<Void> createImage1(Flux<FilePart> files) {

        return Mono.defer(() ->
                                  imageRepository.save(
                                          new Image(
                                                  UUID.randomUUID().toString(),
                                                  ""))
        )

                .publishOn(Schedulers.elastic())
                .subscribeOn(Schedulers.parallel())
                .then();
    }*/

    private void getAuthentication(Flux<FilePart> files) {

    }

    /*public Mono<Void> createImage(Flux<FilePart> files) {
        return files
                .log("createImage-files")
                .flatMap(file -> {
                    Mono<Image> saveDatabaseImage = imageRepository.save(
                            new Image(
                                    UUID.randomUUID().toString(),
                                    file.filename()))
                                                                   .log("createImage-save");

                    Mono<Void> copyFile = Mono.just(Paths.get(UPLOAD_ROOT, file.filename()).toFile())
                                              .log("createImage-picktarget")
                                              .map(destFile -> {
                                                  try {
                                                      destFile.createNewFile();
                                                      return destFile;
                                                  } catch (IOException e) {
                                                      throw new RuntimeException(e);
                                                  }
                                              })
                                              .log("createImage-newfile")
                                              .flatMap(file::transferTo)
                                              .log("createImage-copy");

                    return Mono.when(saveDatabaseImage, copyFile)
                               .log("createImage-when");
                })
                .log("createImage-flatMap")
                .then()
                .log("createImage-done");
    }*/


    public Mono<RestaurantAdminInfo> getRestaurantAdminInfo() {


        List<MonthlySummary> monthlySummaries = new ArrayList<>();
        List<LastOrder> lastOrders = new ArrayList<>();


        monthlySummaries.add(MonthlySummary.builder()
                                           .id("1")
                                           .value("2.56")
                                           .progress("10%")
                                           .difference("29%")
                                           .build());

        monthlySummaries.add(MonthlySummary.builder()
                                           .id("1")
                                           .value("69.6")
                                           .progress("22%")
                                           .difference("84.9%")
                                           .build());

        monthlySummaries.add(MonthlySummary.builder()
                                           .id("1")
                                           .value("923")
                                           .progress("15%")
                                           .difference("8%")
                                           .build());


        lastOrders.add(LastOrder.builder()
                                .id("523")
                                .date("2013/08/08")
                                .name("John Wayne")
                                .status("Completed")
                                .price("23.3")
                                .build());

        lastOrders.add(LastOrder.builder()
                                .id("342")
                                .date("2013/08/08")
                                .name("Robert De Niro")
                                .status("Pending")
                                .price("540.34")
                                .build());

        DailySummay dailySummay = DailySummay.builder()
                                             .build();

        RestaurantAdminInfo d = RestaurantAdminInfo.builder()
                                                   .dailySummary(dailySummay)
                                                   .monthlySummary(monthlySummaries)
                                                   .ordersSummary(lastOrders)
                                                   .build();

        return Mono.just(d);


    }


    /*
    delete User Object
     */
    public void deleteUser() {

        String d = "";

    }


    /*
    login User Object
     */
   /* public Mono<User> loginUser(Mono<User> user) {

        return user
                .map(user1 -> getUserBuild(user1))
                .map(userObject ->
                             userRepository.findByEmailAddressAndPassword
                                     (userObject.getEmailAddress(), userObject.getPassword()))
                .flatMap(userDTOMono -> getUserBuildDTO(userDTOMono));

        *//*return user.map(us -> UserDTO.builder()
                                     .password("123")
                                     .emailAddress("test@gmail.com")
                                     .build())
                   .map(userObject ->
                                userRepository.findByEmailAddressAndPassword
                                        (userObject.getEmailAddress(), userObject.getPassword()))
                   .flatMap(userDTO -> this.getUserBuildDTO(userDTO));*//*


     *//*return user.map(us -> getUserBuild(us))
                .map(userObject ->
                                userRepository.findByEmailAddressAndPassword
                                        (userObject.getEmailAddress(), userObject.getPassword()))
                   .flatMap(userDTO -> this.getUserBuildDTO(userDTO));*//*


    }*/






    public UserDTO test(UserDTO u1) {
        return u1;
    }




    private Mono<UserDTO> getByUserId(User userObject) {
        return userRepository.findByUserId(userObject.getUserId());
    }


    private Mono<Map.Entry<UUID, String>> getNameFromUserId(OrderDTO dit) {
        UUID tt = dit.getUserId();
        return userRepository.findByUserId(dit.getUserId())
                             .map(t -> new AbstractMap.SimpleEntry<>(
                                     tt,
                                     t.getFirstName()));
    }



    /*
    Create User Object
     */

    public Mono<Person> createPerson(Mono<Person> person) {

        return
                person
                        .map(p1 ->this.getPersonBuild(p1))
                        .publishOn(Schedulers.elastic())
                        .map(personRepository::save)
                        .publishOn(Schedulers.elastic())
                        .subscribeOn(Schedulers.parallel())
                        .flatMap(userDTO -> this.getPesonBuildDTO(userDTO));


    }



    public Mono<Person> getPerson(Mono<PersonDTO> p){
        return p.map(p1 -> t(p1));
    }

    public Person t(PersonDTO p){
        return Person.builder().firstname(p.getFirstname()).build();
    }



    /*
    login User Object
     */
    public Mono<Person> loginPerson(Mono<Person> person) {


        return person
                .map(person1 -> getPersonBuild(person1))
                .map(userObject ->
                             personRepository.findByFirstnameAndLastname
                                     (userObject.getFirstname(), userObject.getLastname()))
                .flatMap(userDTO -> this.getPerson(userDTO));


    }





    private PersonDTO getPersonBuild(Person person) {
        return PersonDTO.builder()
                        .id(person.getId())
                        .firstname(person.getFirstname())
                        .lastname(person.getLastname1())
                        .age(person.getAge1())
                        .build();
    }



    private Mono<Person> getPesonBuildDTO(Mono<PersonDTO> personDTOMono) {

        //using callable becuase want lazy mapping once the data is stream

        return personDTOMono.map(personDTO -> getfinalPersonDTO(personDTO))
                            .log("Inside fromCallable");

    }

    private Person getfinalPersonDTO(PersonDTO personDTO) {
        return Person.builder()
                     .id(personDTO.getId())
                     .firstname(personDTO.getFirstname())
                     .lastname1(personDTO.getLastname())
                     .age1(personDTO.getAge())
                     .build();
    }


    /*
    Get customer details
     */
    public Mono<Customer> getCustomer(UUID companyId) {

        return Mono.just(customerRepository.findByCustomerId(companyId))
                   .flatMap(customerDTO -> this.getCustomerDTOBuild(customerDTO));


    }



    /*
    Create Order Object
     */

    public Mono<Order> createOrder(Mono<Order> order) {

        return
                order.map(us -> test(us))
                     .publishOn(Schedulers.elastic())
                     .map(orderRepository::save)
                     .publishOn(Schedulers.elastic())
                     .subscribeOn(Schedulers.parallel())
                     .flatMap(userDTO -> userDTO);


    }

    public Order test(Order order) {
        Order df = order.toBuilder()
                        .orderId(12)
                        .build();
        return df;
    }



    /*
  Get customerInfo details
   */
    public Mono<List<Restaurant>> getRestaurants() {

        List<Restaurant> d = new ArrayList<>();

        d.add(Restaurant.builder()
                        .id("1")
                        .name("test")
                        .address("171 E Broadway, New York, NY 10002")
                        .phone("806-392-5555")
                        .img_url(
                                "https://res.cloudinary.com/grubhub/d_search:browse-images:default.jpg/dpr_auto,f_auto,q_auto,g_auto,w_106,h_106,c_fill,e_improve/search/browse-images/indian.jpg")
                        .cuisine("Indian")
                        .delivery_minimum((float) 1)
                        .delivery_fee((float) 2)
                        .distance((float) 3)
                        .rating_avg("4")
                        .rating_count("5")
                        .latitude((float) 40.704759)
                        .longitude((float) -74.038335)
                        .build());

        return Mono.just(d);


    }

    /*
    Create Customer Object
     */
    public Mono<Customer> createCustomer(Mono<Customer> customer) {

        return
                customer.map(cs -> getCustomerBuild(cs))
                        // this is influenced by subscribeOn
                        //.doOnNext(s -> System.out.println(s + " before publishOn using thread: " + Thread.currentThread().getName()))
                        .publishOn(Schedulers.elastic())
                        .log("publisON")
                        .map(customerRepository::save)
                        .publishOn(Schedulers.elastic())
                        .log("publish after save")
                        // the rest is influenced by publishOn
                        //.doOnNext(s -> System.out.println(s + " after publishOn using thread: " + Thread.currentThread().getName()))
                        .log("SubscriberON")
                        .subscribeOn(Schedulers.parallel())
                        .flatMap(customerDTO -> this.getCustomerDTOBuild(customerDTO));


    }

    private CustomerDTO getCustomerBuild(Customer cs) {
        return CustomerDTO.builder()
                          .adminId(cs.getAdminId())
                          .customerId(cs.getCustomerId())
                          .customerName(cs.getCustomerName())
                          //.createdDate(ZonedDateTime.now())
                          //.updatedDate(ZonedDateTime.now())
                          .build();
    }

    public Mono<Customer> getCustomerDTOBuild(Mono<CustomerDTO> dto) {

        //using callable becuase want lazy mapping once the data is stream

        return dto.map(customerDto -> test(customerDto))
                  .log("Inside fromCallable");

    }

    private Customer test(CustomerDTO customerDto) {
        return Customer.builder()
                       .adminId(customerDto.getAdminId())
                       .customerId(customerDto.getCustomerId())
                       .createdDate(customerDto.getCreatedDate())
                       .updatedDate(customerDto.getUpdatedDate())
                       .customerName(customerDto.getCustomerName())
                       .build();
    }



    private UserDTO getUserBuild(User user) {
        return UserDTO.builder()
                      .userId(user.getUserId())
                      .firstName(user.getFirst_name())
                      .lastName(user.getLast_name())
                      .emailAddress(user.getEmail())
                      .password(user.getPassword())
                      //.session_token(UUID.randomUUID())
                      //.createdDate(ZonedDateTime.now())
                      //.updatedDate(ZonedDateTime.now())
                      .build();
    }


    private Mono<User> getUserBuildDTO(Mono<UserDTO> userDTO) {

        //using callable becuase want lazy mapping once the data is stream

        return userDTO.map(user -> getfinalUserDTO(user))
                      .log("Inside fromCallable");

    }

    private User getfinalUserDTO(UserDTO user) {
        return User.builder()
                   .userId(user.getUserId())
                   .first_name(user.getFirstName())
                   .last_name(user.getLastName())
                   .email(user.getEmailAddress())
                   .password(user.getPassword())
                   .session_token(UUID.randomUUID().toString())
                   .build();
    }




    /*
    Create User Object
     */

    public Mono<User> createUser(Mono<User> user) {

        return
                user.map(us -> getUserBuild(us))
                    .publishOn(Schedulers.elastic())
                    .map(userRepository::save)
                    .publishOn(Schedulers.elastic())
                    .subscribeOn(Schedulers.parallel())
                    .flatMap(userDTO -> this.getUserBuildDTO(userDTO));


    }

}
