@echo off
echo        Deploying Website

set "REPO=C:\Users\Nick\Desktop\RealProjects\dev\Nicksproject.ca"
set "DEST=C:\inetpub\wwwroot"

echo Pulling from github
cd /d "%REPO%"
git pull

echo Syncing files to wwwroot
robocopy "%REPO%" "%DEST%" /MIR /R:0 /W:0 /XD ".git" /XF ".gitignore"

echo        Deployment Complete!
pause