This will help you run this app quickly:

# Prerequisites #
  * [Get GAE/J](http://code.google.com/appengine/downloads.html) (latest version, last tested on 1.2.8)

# Installing and Running #

  * When you [check out the code in eclipse](http://code.google.com/p/appengine-springmvc3-starter-app/source/checkout), eclipse the project settings should be recognized.
  * In eclipse go to project properties, Libraries, Add Variable, Configure Variables, New and add
    * GAE\_HOME with the value pointing to your appengine (1.2.x) installation
  * You'll also have to set an environment variable on your system called `GAE_HOME` and `GWT_HOME` pointing to your installations and restart eclipse.
  * You might have to adjust the referenced JDK (point to JDK6).
  * Start the app with
    * `ant server_run` (and see the app at http://localhost:8080),
    * `ant server_stop(windows)` to stop it on windows.  On Mac you can kill the process by clicking the 'stop' button on the console.
    * `ant server_debug` to run the server in debug mode so you can attach remotely in eclipse.


# Running the Tests #

  * Run the unit tests with `ant runtests`.
  * You can run (or debug!) the tests from eclipse (right click on a test and Run as JUnit test) but you have to make sure you run ant compile-tests first (and every time you make any code changes).

## Brief Intro to the code ##
See [this page](http://code.google.com/p/swagswap/wiki/Instructions) on the swagswap wiki.  That intro is relevant for this code.