FoodPlease™
===================


Software made by **Bits Please** for **Healthy Catering Limited.**


Contents
-------------

 1. Disclaimer
 2. Overview
 3. Features
 4. Installation
 5. Support
 6. History

Disclaimer
-------------
This is a group project made by students at IIE NTNU as a way to experience larger programming assignments where team-effort, organization and skills are pushed to new heights. Project revolves around a fictional client that are expanding their catering business, and need new management software to decrease the workload as demand increases.

----------
<div class="pagebreak"></div>

Overview
-------------------
FoodPlease (FP) is a All-in-One Management System for catering businesses.

  What makes our software special? 
 
 - cross platform - FP runs on Windows, Mac OS X and Linux. 
 
 - simple setup - minimal database knowledge required. 
 
 - Decentralized data storage - This feature allows your to keep you data in sync across several devices using a MySQL database.

Features
-------------------
- Automatic validation of database settings on startup.
- Small, fast and lean.
- Uses PBKDF2 for password encryption.
- Sync across multiple PCs/devices.
- Written in Java/SWING.
- Automatic MySQL structure setup.
- Windows and Mac OS X native feeling versions available.
- Powerful search function to quickly find any customer, order etc.
- Graphical representation of useful statistics.

----------
<div class="pagebreak"></div>
Installation
-------------------

> **Tip:** The User Interface aims to give you useful tooltips, hover over a component to get more infomation. 

 1. Extract the downloaded archive to your preferred installation path.
      > **Note:** Only the file "**foodplease-X.X.X.jar**" and the folder "**libs**" are required to run this software. The rest is mostly for documentation.
      
 2. Run **foodplease-X.X.X.jar**
 
 3. If this is the first run or your database settings are invalid you should get a pop-up asking you to (re)configure your database. These settings need to be validated by clicking "save" before you can continue.

 4. To continue close the settings window. If you want to automatically set-up your database structure, click yes on the pop-up. 
 **This is a requirement if the software never have been used on that database previously!**
> **Note:** If unsure, leave the default settings.
> However for testing purposes we recommend checking all boxes.
> > **Note:** You can set-up the database manually by using the SQL scripts located in **classes\sqlscripts**.
 
 5. Close the window to continue. The login window should now appear.
 A default user with full access have been created for your convenience;
 
 *User:* **admin** 
 *Password*: **admin**
 > **Note:** This password should be changed or the user should be removed after the inital setup.
 6. **Congratulations**! Software is ready to be used.
<div class="pagebreak"></div>

Support
-------------------
If any issues are found, please submit a bug report on our GitHub-repo: https://github.com/Gruppe01BitsPlease/HCL

Roadmap
-------------------
After this project is concluded and released to client, FP will most likely be discontinued.

History
-------------------
27-Apr-2016 : Version 1.0 Final release.

--------------