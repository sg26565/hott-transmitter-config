@echo off
start /D "%~dp0" /B javaw -cp ${project.build.finalName}.jar gde.mdl.ui.SimpleGUI