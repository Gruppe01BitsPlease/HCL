# FoodPlease™
_Software made by Bits Please for Healthy Catering Limited_

Contents
--------
1. Disclaimer
1. Overview
2. Features
3. Installation
4. Support
6. History
--------

1. Disclaimer
   --------
   This is a fictional group project made by students at IIE NTNU as a
   way to experience larger programming assignments where team-effort, organization and
   skills are pushed to new heights. Project revolves around a fictional client that
   are expanding their catering business, and need new management software to decrease
   the workload as demand increases.


2. Overview
   --------
   FoodPlease (FP) is a All-in-One Management System for catering businesses.

   What makes our software special?

    * cross platform - FP runs on Windows, Mac OS X and Linux.

    * its simple setup - minimal database knowledge required.

    * decentralized data storage - This feature allows your to keep you data in sync
      across several devices using a mySQL database.

3. Features
   --------
  - Automatic validation of database settings on startup.
  - Small, fast and lean.
  - Uses PBKDF2 for password encryption.
  - Sync across multiple PCs/devices.
  - Written in Java/SWING.
  - Automatic MySQL structure setup.
  - Windows and Mac OS X native feeling versions available.
  - Powerful search function to quickly find any customer, order etc.
  - Graphical representation of useful statistics.

4. Installation
   -------

   1. Extract archive to preferred installation path.
       1. NOTE!: only the jar file and libs folder are required to run this software.
   2. Run .jar file
   3. On first run you should get a pop-up asking you to provide your database settings.
      These settings need to be validated by clicking "save" before you can continue.
   4. Click on the red close window button. If you want to setup your database structure,
      click yes on the following popup. This is a requirement if the software never have been used
      on that database previously.
      1. NOTE!: Leave settings at default if you are unsure! This will not give any dataloss,
       and ensures that the required structure is present on the database.
   5. After settings are validated a config.properties should be created in the root folder.
      Steps 3-5 need to be repeated if settings get corrupted.
   6. Log with: user 'admin', password 'admin'.
   7. This password should be changed because this is the only account that got
      full access to every function of the software.

5. Support
   --------------------
     If any issues are found, please submit a bug report on our GitHub-repo
     https://github.com/Gruppe01BitsPlease/HCL

6. Roadmap
   -------
   After this project is concluded and released to client,
   FP will most likely be discontinued.

7. History
   -------

   27-Apr-2016 : Version 1.0
      First release.