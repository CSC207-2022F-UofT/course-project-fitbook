# Fitbook

Fitbook is a web application for users to share and see each other's exercise routines! The goal of this project is to provide users the opportunity to share exercise routines that they have found success with, and to take inspiration from others!

Throughout the development, our team has faced many obstacles trying to get all of the features working for Fitbook to combine into a smooth experience for users to share and be guided by the experiences of others. Fitbook provides you with multiple features to allow you to interact with and take inspiration from the many users of Fitbook.

Fitbook allows you to:

 - Create an account and login/register!
 - View a personalized feed of user's post when you enter the app!
 - Make your own posts to share with other users!
 - Like/upvote the posts of users' that you've found useful or enjoyed!
 - Keep track of your posts and those you've liked!
 - Search for posts of interest based on the exercises' keywords!
 - Follow users' who interest you, and build a following!
 - View your own and other users' profiles!

**NOTE: When searching, the query must contain a body part (case sensitive) in order to see body parts, refer to the bubbled body parts in existing posts.**

# Getting Started
Fitbook requires Java 11, which can be installed [here](oracle.com/java/technologies/downloads/#java11).

Fitbook is a Spring application that runs on the **web**, where you can login/register, view and upvote posts, follow users, search for posts, create posts, and more!

To run the Spring application, first clone this GitHub repository to wherever you desire on your computer!

```shell
cd ~/PROJECT_REPOSITORY_LOCATION
git clone https://github.com/CSC207-2022F-UofT/course-project-fitbook.git
```

Next, open the project in IntelliJ, which can be installed [here](https://www.jetbrains.com/idea/download/#section=mac)!

After doing so, open the file, course-project-fitbook/src/main/java/ca/utoronto/fitbook/FitbookApplication.java. and run `FitbookApplication.main()`!

Wait a few seconds while the application builds and starts up!

By default, the website should be using port 8080. so access it by visiting http://localhost:8080 in your web browser of choice!

Welcome to Fitbook!

# 1. Entities

### 1.1 User

User is a class that is used to store the information of each individual user of the application.

The User object has the following attributes:

- ```String id```
- ```String name```
- ```String password```
- ```Date joinDate```
- ```int totalLikes```
- ```List<String> followingIdList```
- ```List<String> followerIdList```
- ```List<String> postIdList```
- ```List<String> likedPostIdList```

### 1.2 Post

Post is a class that is used to store the information of any user's post in the application.

The Post object has the following attributes:

- ```String id```
- ```String authorId```
- ```int likes```
- ```Date postDate```
- ```List<String> exerciseIdList```
- ```String description```

### 1.3 Exercise

Exercise is a class that is used to store the information of any user-created exercise in the application.

The Exercise object has the following attributes:

- ```String id```
- ```ExerciseType type```   (either TEMPORAL or REPETITIVE)
- ```String name```
- ```List<String> keywords```
- ```List<String> bodyParts```

### 1.4 RepeptitiveExercise
 
RepetitiveExrcise is a child of the Exercise Class with an ExerciseType of REPETITIVE.
 
The RepetitiveExrcise object has the following attributes:
 
- ```int reps```
- ```int sets```

### 1.5 TemporalExercise
 
TemporalExercise is a child of the Exercise Class with an ExerciseType of TEMPORAL.
 
 The TemporalExercise object has the following attributes:
- ```int time```

# 2. Use Cases

### 2.1 Register

The Register use case is an interface providing method(s) to create a user.
* **createUser()**
  * Takes in the user information and attempts to create a user and return a response object containing the newly created user's id.

### 2.2 Login
The Login use case is an interface providing method(s) to login a user.
* **loginUser()**
  * Takes in the user information and attempts to login a user and return a response object containing the newly logged in user's id.

### 2.3 Post Creation
The PostCreation use case is an interface providing method(s) for a user to create a post.
* **createPost()**
  * Takes in the user's id, the exercises of the post, and a description, and attempts to create and save a post and return a response object containing the created post's id.

### 2.4 Personalized Feed
The PersonalizedFeed use case is an interface providing method(s) to get a feed of posts.
* **getFeed()**
  * Takes in the user information, limits to posts fetched, and last post fetched, and attempts to get a list of posts and return a response object containing a list of posts, and the last post fetched.

### 2.5 Upvote Post
The UpvotePost use case is an interface providing method(s) for a user to upvote a post.
* **upvotePost()**
  * Takes in the user's id and the post's id to upvote, and attempts to upvote the post and return a response object containing the user's and post's ids.

### 2.6 User Profile
The UserProfile use case is an interface providing method(s) for a user to find and display the profile of any user.
* **findProfile()**
  * Takes in the user's id and the user's id of the profile they wish to view, and attempts to find the profile and return a response object containing the user's profile information.

### 2.7 Follow
The Follow use case is an interface providing method(s) for a user to follow another user.
* **followUser()**
  * Takes in the user's id and the id of the user they wish to follow, and attempts to follow the user and return a response object containing the user's id.

### 2.8 Search
The Search use case is an interface providing method(s) for a user to search for posts
* **search()**
  * Takes in a query string and attempts to fetch posts by their descriptions, and exercises' keywords and body parts, and returns a response object containing a list of relevant posts.

### Spring documentation for annotations
* **@Bean** 
 Bean instantiates a static instance of a class, hence following the singleton pattern. See more at https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-factory-class-static-factory-method.

* **@Component**
  Component is a generic stereotype for any Spring-managed component that provides exception translation for that stereotype. Spring provides further   stereotype annotations: @Repository, @Service, and @Controller that are more suited towards their layer. See more at https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-factory-scopes
  
* **@ResponseStatus**
 Marks an exception for a class to return reason() and code(), when an exception annotated with @ResponseStatus an http status code is automatically    returned. See more at https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/ResponseStatus.html.
  
* **@PostMapping**
 Maps a post request to specific handlers for that request. See more at https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/PostMapping.html.
 
* **@GetMapping** 
 Maps a get request to specific handlers for that request. See more at https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/GetMapping.html. 
 
* **@Configuration**
 Spring Boot lets you externalize your configuration so that you can work with the same application code in different environments. See more at  https://docs.spring.io/spring-boot/docs/2.1.13.RELEASE/reference/html/boot-features-external-config.html#:~:text=Spring%20Boot%20lets%20you%20externalize,line%20arguments%20to%20externalize%20configuration.
 
* **@RequestParam**
 Annotation for mapping web requests onto methods in request-handling classes with flexible method signatures. See more at https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/RequestMapping.html.
 
* **@RequestBody**
 Annotation indicating a method parameter should be bound to the body of the web request. See more at https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/RequestBody.html.
 
* **@Autowired**
 Marks a constructor, field, setter method, or config method as to be autowired by Spring's dependency injection facilities. See more at https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/GetMapping.html.

### Lombok documentation for annotations  
* **@RequiredArgs**
 RequiredArgs generates a constructor for each field in a class marked as final. See more at https://projectlombok.org/features/constructor.
 
* **@Builder**
 Builder.Default generates methods for a class following the builder pattern. See more at https://projectlombok.org/features/Builder.
 
* **@Data** 
  Data is an annotation that generates getter, setter, toString and toHashCode methods for a class that is annotated with it. See more at https://projectlombok.org/features/Data
  
* **@NoArgsConstructor**
 NoArgsContructor generates a contructor with not parameters. See more at https://projectlombok.org/features/constructor.
 
* **@NonNull**
 NonNull enforces that a field cannot be of value null. See more at https://projectlombok.org/features/NonNull.
 
* **@Value**
 Generates an immutable class with all its fields marked final and no setter generated. See more at https://projectlombok.org/features/Value.
