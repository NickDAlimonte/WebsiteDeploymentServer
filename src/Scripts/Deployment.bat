@echo off
echo ==================================
echo        Deploying Website
echo ==================================

set "REPO=C:\Users\Nick\Desktop\RealProjects\Nicksproject.ca"
set "DEST=C:\inetpub\wwwroot"

echo Pulling latest from GitHub...
cd /d "%REPO%"
git pull

echo Syncing files to wwwroot...
robocopy "%REPO%" "%DEST%" /MIR /R:0 /W:0 /XD ".git" /XF ".gitignore"

echo ==================================
echo        Deployment Complete!
echo ==================================
pause