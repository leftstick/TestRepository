call mvn -f %1\%3\pom.xml clean install
echo "%~2"\%3-%4
rmdir /S /Q "" "%2\%3-%4"
copy %1\%3\target\%3-%4.war %2

