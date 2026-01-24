# Social-Media-Content-Planner

A simple JavaSwing application meant to help a user plan, create, edit, and delete social media posts.
The program maintains a running list of posts and displays the upcoming content in a table. 

## Features
* Create Post: Add new social media post with platform, date, category, status, and concept.
* Edit Post: Modify existing posts while validating that fields are not empty.
* Delete Post: Remove posts permanently from storage. 
* Persistent Storage: Posts are saved to a file and loaded automatically when the program is restarted. 
* Post Table: View all upcoming posts in a table, sortable by date.
* User-Friendly Interface: Swing GUI with consistent layout across pages. 

## File Structure 
/src
├── ContentPlannerGUI.java    # Main GUI class
├── ContentPlanner.java       # Stores posts ArrayList and nextId
├── Post.java                 # Post model class
├── PostStorage.java          # Handles saving/loading posts
├── Category.java             # Enum for post categories
└── Status.java               # Enum for post status
