@echo off
cd /d "D:\AI\cursor\agent1"
"C:\Program Files\Git\cmd\git.exe" init
"C:\Program Files\Git\cmd\git.exe" add .
"C:\Program Files\Git\cmd\git.exe" commit -m "初始提交"
"C:\Program Files\Git\cmd\git.exe" remote add origin https://github.com/1061164446/agent-demo.git
"C:\Program Files\Git\cmd\git.exe" push -u origin main
pause 