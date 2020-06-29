# Practical-Touch

## Introduction

We are creating an alternative overlay as a mean of navigating between apps as our submission for the NUS Orbital Program. It is currently a WIP, and we expect to complete it by the end of July 2020.

## Problem Motivation 

Everyday, we use a handful of apps, including social media platforms such as Facebook, Instagram, and Reddit, frequently hopping between them. Sometimes, we even quickly switch between apps, such as shopping apps like Shopee and Lazada to compare prices of the same product, or different cab hailing apps to compare the rates. However, the process can prove to be troublesome, either due to our clumsiness or slow phone.

## Aim 

Our aim is to implement a customisable app drawer overlay to allow users to quickly switch between apps (similar to Facebook Messaging app or Apple’s AssistiveTouch (dock)).

## User Stories

As a tourist, I would like to quickly compare fares of different taxi hailing apps without going through the hassle of searching for them in the app drawer and opening each of them individually.

As a hungry customer who does not leave the comfort of their home, I would like to quickly compare prices between different food delivery services.

As a gamer, I would like to be able to quickly switch between game guide and the game itself.

As a social media consumer, I would like to be able to quickly switch between different social media apps to keep up with what everyone is up to.

## How does our app compare to the in-built system, and similar app switchers/dock overlay?

Let us consider a use case where a user wants to launch multiple apps and switch between them, let’s say A, B, and C. With the normal android, their flow will go be something like open app drawer and find A -> launch A and do relevant stuff -> click home button -> open app drawer and find B -> launch B and do relevant stuff -> click home button -> open app drawer and find C -> launch C and do relevant stuff. And if they want to go back to a previous app (ie. after they are done with C, they want to go back to A), they will need to navigate through the task manager.

What if they then want to move on back to some of their frequently used apps like D and E? They will need to go through the whole routine again.

Our app eliminates the need to go back to the app drawer and task manager every time they want to switch apps, making for a much smoother experience and saves more time. For the use case above, the user simply needs to go through a one-time setup to open our app and create an appset with A, B, and C. After that, they can quickly switch between them without going through the app drawer or task manager every time, and they can launch the appset again easily because it is saved. One might argue going through the app drawer/task manager can be eliminated by placing the apps on the home screen, perhaps even in folders. But this will introduce clutter and disturb the user’s desired home screen look.

We have taken a look at some similar apps, namely Taskbar, EAS, and some Assistive Touch “clones”. There are some features we can learn from them, perhaps by automatically generating a recent AppSet for recent activities, and setting docks to be translucent. We have found at least one of the following issues with the apps

- dock having unintuitive controls
- dated overlay design
- limited size for the favorite menu
- not providing a way to categorise the apps into different sets, which will result in one big favorite menu with a lot of apps. This can slow down the process of switching apps and defeats the purpose of the app.

Our dock is relatively straightforward to use, and the overlay has a minimalistic design. Our appset does not have a cap for the number of apps in it, and you can categorise into different appsets, which also does not have a cap. 

## Project Scope

Features completed in Milestone 1:

| Features          | Description                                                                                                                                                                                                                                                                                     |
|-------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Base app layout   | Viewpager with 2 tabs (created sets tab and create new tab).                                                                                                                                                                                                                                    |
| Created sets tab  | Displays user’s recently created AppSets which are stored locally using the Room Database technology. Users can quickly relaunch their previously created AppSets here.                                                                                                                         |
| Create new tab    | Allows users to create their own AppSets by selecting apps from the AppDrawer which displays their list of installed apps. The selected apps will be shown in the AppTray and the user can click on the launch button to deploy the Dock which will hold the list of apps in the AppTray.       |
| Dock              | It is a small floating bubble icon that overlays over the user’s phone screen and can be dragged around by the user’s finger. When tapped, it displays the list of apps, the user has chosen, at the top of the screen and the user can click on the corresponding apps to deploy them quickly. |
| MVVM architecture | An architecture that follows the basic OOP principles and helps make our code more readable and reduces spaghetti code, making the app more stable and bug-free.                                                                                                                                |

Features completed in Milestone 2:

| Features                    | Description                                                                                                    |
|-----------------------------|----------------------------------------------------------------------------------------------------------------|
| Edit function               | Allowed the users to edit AppSets.                                                                             |
| AppSet Names                | Allowed users to add AppSets names.                                                                            |
| AppDrawer improvement       | AppDrawer now displays the name of the app icons and is sorted alphabetically.                                 |
| App performance improvement | Moved required preprocessing data to a singleton class and replaced findViewbyId() with data and view binding. |

Some QoL changes include:

- The AppTray will scroll to the right when users add an app to the AppTray. 
- When an AppSet is created, in the created sets tab, it will trigger a scroll to the top to display the latest AppSet created.  
- Converted all displays from px(pixel) to dp(density-independent pixels) so that the layouts would remain uniform throughout different types of devices.  
- Made the toolbar hide itself when users scroll down. 
- Made it so that when users rotate our application, it will not crash our application and the AppTray would still be able to display the apps the user had selected. 
- Added notification and modifies service to be foreground (to prevent our application from being killed when the user closes his phone). 
- Added delete all AppSets functionality. 

Features to be completed in Milestone 3:

| Features        | Description                                                                                                                                                                                                                                                                                          |
|-----------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Widget          | An even more convenient feature we intend to implement where the users do not have to open our application to launch their desired AppSets. Instead, they can assign a widget icon to an AppSet, so that they can launch that AppSet directly from the home screen without going to Practical Touch. |
| Movable AppSets | Allow users to manually move the position of the AppSets in the list.                                                                                                                                                                                                                                |

## Challenges faced

When implementing the app drawer to include the names, the app was noticeably laggy during scrolling. We solved this by using Android Jetpack’s RecyclerView library.

There were also some bugs regarding configuration changes (for example, screen rotation) where the selected apps will disappear. This is due to the logic of the tray being handled in the activity of that page, which turns out to get destroyed and rebuilt when there is a configuration change such as screen rotating. This was solved by storing the data in the activity’s viewmodel and making our layouts observe data from there.

## Tech Stack

### Android Studio 

We used Android Studio because we wanted to create an app that can rival the convenience brought about by Apple’s AssistiveTouch, however, we didn’t want to include all of Apple’s AssistiveTouch’s amazing features. We decided to focus on a single convenience portion to tackle and we eventually decided to work on the convenience of switching of apps. Taking inspiration from mainly Facebook’s messaging app, we wanted an app that was similar to the chat bubble overlay in that app but replacing chats with apps. We also wanted our apps to store sets of apps that the user chose to display.

### Room database (SQLite)

We needed a database to keep track of the user’s AppSets and by using Android Studio, there were not a lot of software available that supported databases except for the Room database which was very simple to use and eliminated a lot of boilerplate code found in SQLite database. Since Room abstracts out a lot of features from SQLite, it was the best option to keep track of the user’s data. Another option we could maybe look into was Google’s Firebase database, however, it would just be more cumbersome for us as we did not need our data to be stored in a website.
