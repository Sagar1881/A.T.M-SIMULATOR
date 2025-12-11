🏦 ATM Simulator in Java

A desktop-based ATM Simulator System built using Java, Swing GUI, and Object-Oriented Programming principles.
The project demonstrates core banking operations, user account management, and persistent storage using Object Serialization.

Features

User Account Management

Create new user accounts

Secure login using a 4-digit PIN

Data persistence across sessions


ATM Functionalities

Deposit money

Withdraw money with balance validation

Check current balance

View last 5 transactions


PIN Security

Validates old PIN before changes

Rejects mismatched new PIN entries

Ensures only numeric 4-digit PINs


Data Persistence (Serialization)

User data stored in users.dat file

Automatically saved after each transaction

Loads previous user accounts on startup


Java Swing GUI

Simple, user-friendly ATM interface

Includes screens for:

Welcome

Login

Registration

Main ATM Menu

Transaction messages


Tech Stack

Java SE

Swing / AWT (GUI)

OOP Concepts

Abstraction

Inheritance

Encapsulation

Interfaces


Collections Framework

HashMap<Integer, ATM> for user accounts

ArrayList<String> for transaction history


File Handling (Serialization)

ObjectInputStream

ObjectOutputStream


System Architecture

User → GUI (Java Swing) → ATM Logic → Serialization → users.dat

Includes:

Abstract class: ATMOperations

Interface: TransactionHandler

Classes: User, ATM, ATMSimulatorGUI

GUI-based event handling using ActionListener



Project Structure

ATM Simulator/
│
├── ATMSimulatorGUI.java   # Main GUI application
├── ATM.java               # Core ATM logic
├── User.java              # User model
├── users.dat              # Serialized user database (auto-generated)
└── README.md              # Project documentation


Testing Summary

Verified deposit, withdrawal, and balance updates

Tested PIN validation and incorrect login attempts

Confirmed data persistence across sessions

Verified transaction history (last 5 entries maintained)


How to Run

1. Install JDK 8+


2. Compile the program:

javac ATMSimulatorGUI.java


3. Run it:

java ATMSimulatorGUI


Learning Outcomes

This project helps understand:

Object-Oriented Programming in real-world applications

GUI development in Java Swing

Data persistence using Serialization

Collections framework (HashMap, ArrayList)

Modular and event-driven programming

Future Enhancements

PIN change feature (already supported in extended version)

Add mini-statement download

Improve UI with modern styling

Add admin dashboard

Database integration (MySQL) instead of serialization

