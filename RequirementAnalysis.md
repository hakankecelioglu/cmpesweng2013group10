# 1.Introduction #

Purposeful Communities is an crowd sourcing application whose goal is to create an collaborative online environment that will enable people to publish and fulfill their needs and act together to solve a problem . As the name implies, system is based on communities consisting of a group of users who have a common aim such  as animal rights, anti-pollution, and charity.Some applications such as Twitter and Facebook are being used similarly but they are not able to solve coordination problem (for example, old “need” keeps getting re-tweeted even after that need is met). Purposeful Communities will also solve this problem by enabling coordination among users and building dependencies between tasks and tracking the completion statuses of tasks.

## Definitions ##
  * **Community:** The place where group of people having a common purpose communicates and creates and completes tasks .
  * **Task:**  Publications of a need created by a community member and demanded from members of the community and information about how need should be provided.
  * **Task Type:** A generic definition which determines how tasks are created and replied. Each task is assoicated with a task type and created depending on it.
  * **Need type:** A need can be a service or a good. Goods are  countable needs specified with a quantity (Ex: 50 tents needed for an help after an earthquake ). Services are generally actions such as cleaning of a place or treatment of wounded people.


# 2.Functional requirements #

> ## 2.1 Communities ##

2.1.1. Communities shall be created by registered users providing a name and description which states its purpose

2.1.2. Communities shall support membership. Users shall be able to join a community

2.1.3. Creator of a community shall become automatically community admin and a member of that community.

2.1.4. Community admins shall be able to create task types of community.

2.1.4. Community members shall be able to create community tasks.

2.1.5. Communities members list shall be available to other community members.

2.1.6. Community tasks with  progress statuses and remaining day information shall be listed in main community interface and tracked by community members.

2.1.7. Community tasks shall be sortable by two criteria: latest(determined by task creation date), and urgent (determined by task deadline)

2.1.8. Community members shall be able to contribute to community tasks.

2.1.9. Communities members shall be notified when a new community task is created.

> ## 2.2 Task Types ##

2.2.1. Community admins shall be able to create task types in their communities.

2.2.2. Tasks types shall consist of generic information about how a task is created on replied and what is type of need of task.

2.2.3. Task type creator shall specify need type as service, goods or only form.

2.2.4. Task type creator shall be able to specify to form fields which will be filled by a possible task creator using this task type and  reply fields which will be filled by users replying a task created from this task type.

> 2.2.4.1 Forms shall consists of form fields which consists of a field label and a field type
> 2.2.4.2 Single text line, text area, photo, multiple choice, single choice and optional set field types shall be provided

> ## 2.3 Tasks ##
2.3.1. Tasks shall be created by logged users.

2.3.2  A task type shall be selected in order to persist structured data

2.3.3. Following information should be provided by user to create a task.

> 2.3.3.1. task name which states task's identity.

> 2.3.3.2. task description which describes task more detailed and provides necessary information to possible task contributers.

> 2.3.3.3. deadline: latest day of for task to be done. After deadline tasks shall be closed.

> 2.3.3.4. Form fields determined in selected task type.

2.3.4. Tasks shall be done by users providing task need and completing task reply form

2.3.5. Completion status(Percentage of needs satisfied) of the task shall automatically be tracked by the system if task's is in good need type.

2.3.6. Tasks shall be monitored with their name, description, completion status, follower number, and remaning day information.

2.3.7. Task followers and task owner shall receive notification when a new reply is posted to that task.

2.3.8. Users shall be able to give upvote or downvote to a task except those owned by them.


> ## 2.4 Users ##
Users There should be 4 different user types: general admin, user, guest and community  admin

2.4.1. Guests shall be able to browse the system but not create content and participate

2.4.2. Guests should provide username, email, and password to become registered users.

2.4.3. Personal information and communities of an user shall be presented publicly to the other users in user profile pages.

2.4.4. Users shall have a reputation score calculated based on the tasks he helped and up/down votes given to tasks created by him.

2.4.5. Users shall login system providing email/username and password.

2.4.6. Users shall be provided with the facilities to edit their profiles.

2.4.7. General admins shall have, in addition to normal users' abilities, authorities  to  ban users, and remove all types of content

> ## 2.5 Tagging ##

2.5.1. Tasks and communities shall be tagged by the ones who create them.

2.5.2. Semantic tagging shall be supported (tags shall be stored and processed with their semantics)

2.5.3. Users shall be encouraged  to use semantic tags using a semantic tag interface.

2.5.4. Number of tags for a task or community shall be restricted.


> ## 2.6 Searching ##

2.6.1. Community and task search with a basic keyword shall be supported


> ## 2.7 Follow / Share ##

2.7.1. Users shall be able to follow tasks. Followers shall get notifications when a progress occurs in a task.

> ## 2.8 Recommendation system ##

2.8.1. Communities similar to another community shall be recommended.

2.8.2. Tasks shall be recommended to users using users task follow/community join history (by tags).


> ## 2.9. Reputation System ##

2.9.1. A task owner shall gain +5 reputation score when his task is upvoted.

2.9.2. A task owner shall loose -1 reputation score when his task is downvoted.



> # 3.Non-Functional Requirements #

> ## 3.1 Operating & Platform constraints ##

3.1.1. The application should be supported by Google Chrome (All version), Safari (4.1.3 or higher), Internet Explorer (10 or higher) and Mozilla Firefox (12 or higher).

> ## 3.2 Modifiability ##
3.2.1. The system should be designed in a modular manner so that the developers can modify it to respond the varying needs of users.

> ## 3.3 Portability ##
3.3.1. A mobile application to interact with users shall be supported with less features relative to the web application.
3.3.2. Mobile application shall be implemented for Android devices.

> ## 3.4 Security ##
3.4.1. The application shall be accessed via a user authentification process. (i.e. sign in). The contents shall be public. Only members shall manipulate the contents.
3.4.2. Registration shall be validated.

> ## 3.5 Usability ##
3.5.1. The user interface of the application should be similar to popular task management applications (e.g. PivotalTracker) at the launch stage to achieve ease of use.
3.5.2. There shall be more visual design to focus than the content.

> ## 3.6 Language ##
3.6.1. The language of the application shall be in English.

> ## 3.7 Response Time ##
3.7.1. Response time of the web pages and database queries should be less than five seconds with an average quality Internet connection.

> ## 3.8 Reliability ##
3.8.1. All the records and database shall be copied for possible failures and the copied data should be updated periodically (daily).

> ## 3.9 System Availability ##
3.9.2. The user interface of the system shall be available during day and the maintenance shall be performed at nights when necessary.

> ## 3.10 Physical Requirements ##
3.10.1. System shall be written using an IDE in Windows, OS X or Linux operating systems.

> ## 3.11 Design ##
3.11.1. Design of home page should be simple and user friendly.

3.11.2. Color choices should be done consciously.

3.11.3. Homepage shall consist the recent urgent tasks.

> ## 3.12 + Search engine Optimization ##

3.12.1 + When a new task is created the URL should be formed according to
header of the task which is important for search engine indexing.