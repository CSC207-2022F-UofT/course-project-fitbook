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

- String id
- String name
- String password
- Date joinDate
- int totalLikes
- List<String> followingIdList
- List<String> followerIdList
- List<String> postIdList
- List<String> likedPostIdList

### 1.2 Post

Post is a class that is used to store the information of any user's post in the application.

The Post object has the following attributes:

- String id
- String authorId
- int likes
- Date postDate
- List<String> exerciseIdList
- String description

### 1.3 Exercise

Exercise is a class that is used to store the information of any user-created exercise in the application.

The Exercise object has the following attributes:

- String id
- ExerciseType type   (either TEMPORAL or REPETITIVE)
- String name
- List<String> keywords
- List<String> bodyParts

### 1.4 RepeptitiveExercise
 
RepetitiveExrcise is a child of the Exercise Class with an ExerciseType of REPETITIVE.
 
The RepetitiveExrcise object has the following attributes:
 
- int reps
- int sets

### 1.5 TemporalExercise
 
TemporalExercise is a child of the Exercise Class with an ExerciseType of TEMPORAL.
 
 The TemporalExercise object has the following attributes:
- int time

# 2. Usecases

### 2.1 Login/Register
 - TODO

### 2.2 Personalized Feed
- TODO

### 2.3 Upvote Post
- TODO

### 2.4 Profile View
- TODO

### 2.5 Follow
- TODO
