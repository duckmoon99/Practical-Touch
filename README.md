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

## Project Scope

The application provides an alternative interface for users to navigate quicker and easier between different applications.

Currently, the prototype is able to support multiple apps without a limit, and the dock can easily be moved around like a Facebook chat bubble. It can also store recently launched sets, so that it can be easily reused later. So the proposed core features have mostly been implemented successfully. 

The remainder of the project will be mostly spent on polishing, bug fixing, and implementing more features. Some of the upcoming features that could be implemented include dock customisation and user profile for a more personalised experience.

We will keep track of the remaining time available, and implement more necessary features after we gather feedback from users.

## What advantage does our application have over the in-built system (app drawer and task manager)?

The old method of using multiple apps would be to search for each of them individually through the app drawer, open one of them, then search for another one to open it, rinse and repeat. If you want to switch back to some recently used app, you might use the task manager provided, however the task manager can be rather cluttered. We believe our application will be able to provide a cleaner and smoother experience as it only requires a one-time setup for repeated use of the same set of apps, and the interface provided is simple enough so as to not be intrusive or cluttered.

## Tech Stack

Android Studio - for the main application

Room database (SQLite) - to manage application sets and user profile
