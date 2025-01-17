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

    class DashboardRequestsAndPending {
        -id_responsible_user: Integer
        -responsible_username: String
        -count_requests = 0
        -count_pending = 0
        -count_subscriptions = 0
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

    class DashboardEvents {
        -id_dashboard_events: Integer
        -id_responsible_user: Integer
        -responsible_username: String
        -count_events = 0
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
