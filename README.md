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
