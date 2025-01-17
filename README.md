# Backend API for 'Wishlyapp-frontend with Angular/Typescript' project.
This API was developed for an Angular project (where I developed a front with a login screen with Authentication and Authorization)
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

``` mermaid

classDiagram
    class User {
        -firstName: String
        -lastName: String
        -email: String
        -username: String
        -password: String
        -identity: String
        -status: String
        -profileRole: String
        -Dashbboard[] dashboard
    }

    class Dashboard {
        -Informations[] informationsBasics
    }


   User "1" *-- "1" Dashboard

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
✅ Front-End Repository: [https://github.com/devluanna/project-angular-login](https://github.com/devluanna/wishlyapp-frontend)
<br />
<br />

Screen Login:<br/>
![image](https://github.com/user-attachments/assets/1ebf1c68-2b46-44ac-aaee-b39ef4ede902)
<br /><br />
Screen Signup: <br/>
![image](https://github.com/user-attachments/assets/221c2aba-6059-4aed-899c-e57000f8af3d)
