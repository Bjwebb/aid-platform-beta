Some of plugins for Jenkins are needed:
SBT plugin - may be downloaded manually(updates.jenkins-ci.org/latest/sbt.hpi) and added in advanced tab
GIT Plugin 
SSH Transfer plugin

Some work before start:
Create or use existing(used for tests) rsa key (copy to jenkins ssh folder), add github to known hosts (ssh git@github.com)
Copy/Paste key to github user ssh panel
Its all because of bug in GIT plugin that wont allow to use http authentication

While downloading additonal gems for middleman exec - there may ba a need to turn off TTY,
to do that, one can edit /etc/sudoers and make sure this line is commented out
#Defaults    requiretty 
Or create user jenkins, and give him proper rights             
This is due jenkins instance calling scripts that use sudo'ed commands
