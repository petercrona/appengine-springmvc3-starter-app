To depoy if credentials are expired during an ant update:

in a terminal window, make sure the java\bin dir is in your path

for windoze
%GAE_HOME%\bin\appcfg.cmd update C:\workspace\appengine-springmvc3-starter-app\target\appengine-springmvc3-starter-app

for mac/linux
$GAE_HOME/bin/appcfg.sh update ~/Documents/workspace/appengine-springmvc3-starter-app/target/appengine-springmvc3-starter-app/

To deploy through a firewall use the --proxy flag:
%GAE_HOME%\bin\appcfg.cmd --proxy=172.23.100.53:8080 ...