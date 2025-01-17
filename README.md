## 📜 Project Description
Backend API for 'Wishlyapp-frontend with Angular/Typescript' project.<br />
WishlyApp is a wishlist creation and management system, with advanced features for authentication, authorization, and product and event management.<br />
This repository represents the backend of the application, developed with Java 17 and Spring Boot. It was designed to be integrated with the frontend, which uses Angular and TypeScript.<br />
[most updated branch > DEVELOPER]
<br /><br />

## 💻 Technologies Used
Backend:
 - Java 17;
 - Spring Boot Framework;
 - Maven;
Database:
 - H2 (in-memory, for local testing);
 - MySQL (in production, hosted on Amazon RDS);
Frontend:
 - Angular;
 - TypeScript;
 - Repositório do Frontend: (https://github.com/devluanna/wishlyapp-frontend)
<br />
<br />

## 🎯 Features
<br />
📶 Where we have the following functionalities:<br />
- ** Wishlist Creation: ** <br />
 - Users can create public or private wishlists by associating categories, products, and events;<br />
**- Wishlist Subscription:<br />**
 - Users can subscribe to public wishlists automatically;<br />
 - Subscriptions to private wishlists require owner approval;
**- Product Recommendations;<br />**
 - Guests can recommend products to the wishlist owner.<br />
 - The recommendation flow uses AWS Lambda, Step Functions, and Amazon SQS to manage requests and approvals;<br />
**- Event Creation:<br />**
Events can be created and associated with a specific wishlist, allowing for the management of dates, categories, and participants;
<br />
<br />

## 📚 Business Rule
<br />
- Creating Wishlists:
 - Any authenticated user can create wishlists.
 - A wishlist can be public or private.
- Subscribing to Wishlists:
 - Public wishlists: Subscription is automatic.
 - Private wishlists: The wishlist owner must approve or deny the subscription.
- Guests and Products:
 - Users with approved connections can be invited to a private wishlist.
 - Only users connected to the owner can nominate products.
- Events and Wishlists:
 - Each event must be linked to a single wishlist.
 - The status of an event can be updated by the owner.
<br />
<br />

## 🛠️ Observations on the Code
<br />
The development of this project was started with the purpose of learning and applying backend and systems architecture concepts.<br />
The code reflects my evolution as a developer since the beginning. Some parts were intentionally left unrefactored so that I can revisit and improve later.
<br />
The latest commits already include significant efforts to adopt good practices, such as Design Patterns and SOLID principles.
<br />
<br />

## 📶 Project Status
<br />
The project is under continuous development, with additional features being implemented, such as:
<br />
Full integration with AWS Step Functions and Lambda Functions.
Refining business rules for subscriptions and products.
<br />
<br />

## 📊 Below is the UML diagram with the classes and their relationships.
<br />

``` mermaid

classDiagram
    class User {
        -Integer id_user
        -String first_name
        -String last_name
        -String username
        -String email
        -Date date_birthday
        -String gender
        -String password
        -String confirm_password
        -UserRole role
        -Status status
        -Boolean tokenValidate
        -Date tokenExpiration
        -Integer count_notifications_total
        -Integer count_notifications_read
        -Integer count_notifications_unread
    }

    class ConnectionsDashboard {
        -id_dashboard: Integer: Integer
        -id_responsible_user: Integer
        -responsible_username: String
        -responsible_user_emai: String
        -count_friends = 0
        -count_requests_by_you = 0
        -count_requests_by_others = 0
    }


    class Connections {
        - Integer id_connection
        - Integer id_user_connection
        - String name
        - String username
        - StatusConnections statusConnections
        - Boolean profileIsOpenForConnections
        - Date connection_date
    }

   
    class RequestsByYou {
        - Integer id_requests
        - Integer id_user_to_add
        - Integer id_dashboard_user_to_add
        - String username
        - Date connection_date
        - StatusConnections statusConnections
        - ConnectionsDashboard dashboardRequester
    }

    class RequestsByOthers {
        - Integer id_requests_pending
        - Integer id_user_requestor
        - Integer id_dashboard_user_requestor
        - String username
        - Date connection_date
        - StatusConnections statusConnections
        - ConnectionsDashboard dashboard
    }

    class DashboardRequestsAndPending {
        -id_responsible_user: Integer
        -responsible_username: String
        -count_requests = 0
        -count_pending = 0
        -count_subscriptions = 0
    }

    class Pending {
        - Integer id_pending
        - Integer id_user_guest
        - String username_guest
        - Integer id_owner_user
        - String username_owner
        - Integer id_wishlist
        - String wishlist_name
        - Date date_invited
        - StatusSubscribers statusSubscribers
        - boolean isUserWithConnection
    }

    class Requests {
        - Integer id_request
        - Integer id_user
        - String username
        - Integer id_owner_user
        - String username_owner
        - Integer id_wishlist
        - String wishlist_name
        - Date date_of_request
        - String statusSubscribers
        - boolean isUserWithConnection
    }

    class MySubscriptions {
        - Integer id_my_subscription
        - Integer id_user
        - String username
        - Date date_you_joined
        - String uri_img_wishlist
        - Integer id_wishlist
        - String name_wishlist
        - Integer identity_wishlist
        - Visibility Visibility
        - String category
        - boolean isUserWithConnectionOwner
        - boolean hasProductsByRecommendationPending
        - Integer count_products_by_recommendation_pending
        - Integer count_products_by_recommendation_total
        - Integer count_likes
        - Integer count_subscribers_wishlist
        - Date creation_date_wishlist
        - StatusSubscribers statusSubscribers
        - SubscriptionType subscriptionType
    }

    class NotificationsUser {
       -id_notification: Integer
       -id_dashboard_user: Integer
       -username: String
       -notification_name: String
       -notification_description: String
       -Boolean notificationWasViewed = false
       -date_of_notification: Date
       -Boolean notification_reminder
    }

    class DashboardWishlists {
        -id_dashboard_wishlists: Integer
        -id_responsible_user: Integer
        -responsible_username: String
        -count_wishlists = 0
    }

    class Wishlist {
        - Integer id_wishlist
        - Integer wishlist_identity
        - String wishlist_name
        - String url_img
        - String description
        - Integer id_owner
        - String username_owner
        - String visibility
        - Boolean isRequiredRequest
        - String url_share
        - String category
        - String sub_category
        - String status_wishlist
        - Date creation_date
        - Date last_update_date
        - Boolean haveLinkedEvent
        - String name_event_linked
        - Date start_date
        - Date end_date
        - Boolean useEventDate
        - Boolean enableProductsByRecommendation
        - Boolean enablesProductReservations
        - Boolean isACopiedWishlist
        - Integer count_likes
        - Integer count_shares
        - Integer count_copies
        - Integer count_total_subscribers
        - Integer count_recommended_products_pending
    }

     class DashboardRequestsSubscribers {
        - Integer id_dashboard_requests
        - Integer id_responsible_user
        - Integer id_wishlist
        - String username_responsible
        - String name_wishlist
        - Integer identity_wishlist
        - Integer count_subscriber_requests
        - Integer count_pending_invitations
    }

     class SubscriberRequests {
        - Integer id_request
        - Integer id_user
        - String username
        - String status_request
    }

     class PendingInvitations {
        - Integer id_invitation
        - Integer id_user
        - String username
        - String status_invitation
    }


    class EventsInWishlists {
        - Integer id_events_in_wishlists
        - Integer id_event
        - String event_name
        - String status
        - Date creation_date
        - Date last_update_date
        - Date start_date
        - Date end_date
    }

    class WishlistSubscribers {
        - Integer id_subscriber
        - Integer id_user
        - String username
        - Date date_you_joined
        - StatusSubscribers statusSubscribers
        - boolean isUserWithConnection
    }

    class PendingInvitations {
        - Integer id_pending_invitation
        - Integer id_user_guest
        - String username_guest
        - Date invitation_date
        - boolean isUserWithConnection
        - StatusSubscribers statusSubscribers
        - Integer id_wishlist
        - String name_wishlist
        - Integer id_owner
    }

    class SubscriberRequests {
        - Integer id_subscriber_request
        - Integer id_user
        - String username
        - Date date_user_requested
        - Integer id_wishlist
        - String name_wishlist
        - boolean isUserWithConnection
        - StatusSubscribers statusSubscribers
    }

    class Tags {
        - Integer id_tag
        - String tag_name
    }

    class DashboardProducts {
        - Integer id_dashboard_products
        - Integer id_wishlist
        - Integer count_products
        - Integer count_recommended_products_pending
        - Integer count_recommended_products_approved
        - Integer count_reserved_products
        - List~ProductList~ productsList
        - List~RequestsProducts~ requestsProducts
    }

     class DashboardEvents {
        -id_dashboard_events: Integer
        -id_responsible_user: Integer
        -responsible_username: String
        -count_events = 0
    }

     class Events {
        - Integer id_event
        - String event_name
        - String event_description
        - String category
        - Integer id_owner
        - String username_owner
        - String status
        - Date creation_date
        - Date start_date
        - Date end_date
        - Date last_update_date
    }

    User "1" --> "1" ConnectionsDashboard
    User "1" --> "1" DashboardRequestsAndPending
    User "1" --> "*" NotificationsUser
    User "1" --> "1" DashboardWishlists
    User "1" --> "1" DashboardEvents
    ConnectionsDashboard "1" --> "*" Connections
    ConnectionsDashboard "1" --> "*" RequestsByYou
    ConnectionsDashboard "1" --> "*" RequestsByOthers
    DashboardRequestsAndPending "1" --> "*" Requests
    DashboardRequestsAndPending "1" --> "*" Pending
    DashboardRequestsAndPending "1" --> "*" MySubscriptions
    DashboardWishlists "1" --> "*" Wishlist
    DashboardEvents "1" --> "*" Events
    Wishlist "1" --> "*" Tags
    Wishlist "1" --> "1" EventsInWishlists
    Wishlist "1" --> "*" WishlistSubscribers
    Wishlist "1" --> "1" DashboardRequestsSubscribers
    Wishlist "1" --> "1" DashboardProducts
    DashboardRequestsSubscribers "1" --> "1" Wishlist
    DashboardRequestsSubscribers "1" --> "*" SubscriberRequests
    DashboardRequestsSubscribers "1" --> "*" PendingInvitations
    DashboardProducts  "1" --> "*" ProductList 
    DashboardProducts  "1" --> "*" RequestsProducts

```

Screen Login:<br/>
![image](https://github.com/user-attachments/assets/1ebf1c68-2b46-44ac-aaee-b39ef4ede902)
<br /><br />
Screen Signup: <br/>
![image](https://github.com/user-attachments/assets/221c2aba-6059-4aed-899c-e57000f8af3d)
<br/>
<br/>
## 🌟 Este projeto é um marco na minha evolução como desenvolvedora FullStack. Feedbacks e contribuições são sempre bem-vindos! 😊
