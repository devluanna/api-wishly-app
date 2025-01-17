# Backend API for 'Wishlyapp-frontend with Angular/Typescript' project.
This API was developed for an Angular project (where I developed a front with a login screen with Authentication and Authorization) [most updated branch > DEVELOPER]
<br />
💻 Technologies used:
- Java 17
- Spring Boot Framework
- Maven
- In-memory database using H2
- Database
- Local/HOM MYSQL database (PROD in RDS on AWS)

📶 The idea behind the system is: Wishlist creation system, where we have the following functionalities:<br />
- Create wishlist;
- Subscribe to a Wishlist;
  - When subscribing to a PRIVATE wishlist, the Wishlist OWNER must APPROVE or DENY;
- GUEST can indicate a product to the Wishlist OWNER
  - The product indication flow is being done using AWS Lambda, Step Function and SQS.
- Create events;
- Link events to a WISHLIST.
<br />
<br />
The code has been under development for longer than the repository is showing, and it shows my evolution from the beginning until now. It has not been refactored, because later I want to understand what needs to be improved.
<br />
So initially, some Services and business rules will not be in perfect condition, and I decided to leave them that way for now. In the last commits, I have already been working hard to make the code cleaner, more practical,<br />
respecting DESIGN PATTERNS AND SOLID PRINCIPLES.
<br />
<br />

The project is still under development and some features are still being adjusted for Step Function and Lambdas.
<br />
<br />
# Below is the UML diagram with the classes and their relationships.
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
<br />
✨ This is a project for my personal development as FullStack, using Java on the backend, and Angular with Typescript on the front end
<br />
<br />
💻 Technologies used in the frontend:<br />
- Angular
<br />
- Typescript
<br />
✅ Front-End Repository: (https://github.com/devluanna/wishlyapp-frontend)
<br />
<br />

Screen Login:<br/>
![image](https://github.com/user-attachments/assets/1ebf1c68-2b46-44ac-aaee-b39ef4ede902)
<br /><br />
Screen Signup: <br/>
![image](https://github.com/user-attachments/assets/221c2aba-6059-4aed-899c-e57000f8af3d)
