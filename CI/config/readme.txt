Some of plugins for Jenkins are needed:
SBT plugin
GIT Plugin
SSH Transfer plugin

Some work before start:
Create or use existing(used for tests) rsa key (copy to jenkins ssh folder), add github to known hosts (ssh git@github.com)
Copy/Paste key to github user ssh panel
Its all because of bug in GIT plugin that wont allow to use http authentication
