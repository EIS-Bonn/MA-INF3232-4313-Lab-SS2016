
---
##Semantic Lifting of Budget Data
####University of Bonn, EIS working group
#####October 2016



---
####Group E

###Mentor: 
Fathoni Musyaffa

###Members: 
1. Florian Weile (L)
2. Tatiana Novikova
3. Aberham Gebreyohannes
4. Samuel Y. Ayele

---
More information about the project can be found in our 
[BitBucket Repository
](https://bitbucket.org/cowclaw/semweblab2016) and 
[Google Drive Folder](https://drive.google.com/open?id=0B0dpa_yvb1I6dlIwNGRBZkJ0VnM)



<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br /><span xmlns:dct="http://purl.org/dc/terms/" property="dct:title">ABuDaT - Administrative Budget Data Transformer</span> by <a xmlns:cc="http://creativecommons.org/ns#" href="https://github.com/EIS-Bonn/MA-INF3232-4313-Lab-SS2016/tree/master/Ge" property="cc:attributionName" rel="cc:attributionURL">Florian Weile, Tatiana Novikova, Aberham Gebreyohannes, Samuel Y. Ayele</a> is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.

---


###ABuDaT (administrative budget data transformer)


###0. Prerequisites
* Have git installed.
* Have a JDK (v8) installed.

###1. Installation
Clone the sources

    $ git clone https://cowclaw@bitbucket.org/cowclaw/semweblab2016.git

Running ABuDaT standalone (Tomcat included)
  
    $ cd semweblab2016/ABuDaT
    $ ./gradlew bootRun

Abudat should be up and running on http://localhost:9000/

Configuration file: *semweblab2016/ABuDaT/src/main/resources/application.properties*, defaults are:
    
    linkedpipes.etl.host=localhost
    linkedpipes.etl.port=8080
    
    #Path to the abudat output dir.
    abudat.output-dir=/tmp/abudat

    #The data endpoint of the fuseki installation.
    fuseki.data.endpoint=http://localhost:3030/ds/data
    
    server.port=${port:9000}
    spring.thymeleaf.cache=false
    
    spring.datasource.url=jdbc:mysql://localhost/abudatdata?autoReconnect=true&useSSL=false
    spring.datasource.username=abudat
    spring.datasource.password=abudat
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    
    spring.jpa.hibernate.ddl-auto = update
    spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect

*Note: assumes running linkedpipes etl on localhost, see installation instructions below.* 

*Note: Due to limitations and security concerns, linkedpipes etl needs to run on the same host as abudat. Otherwise the functionality for download of data and upload of data to a triple store will not work.*

#####Running in a dedicated Tomcat
It is possible to run ABuDaT in a dedicated tomcat server. Therefor simply assemble a war file:

    $ cd semweblab2016/ABuDaT
    $ ./gradlew war

You can find it the war here: semweblab2016/ABuDaT/build/libs/ABuDaT.war. It can then be copied to tomcats webapp folder.
Note that in this case the server.port=${port:9000} setting will have no effect.

###2. Database Setup
Install mysql-server

    $ sudo apt-get install mysql-server
    
Create the database

    $ mysql -u root -p
    mysql> create database abudatdata default character set utf8 default collate utf8_bin;
    mysql> create user 'abudat'@'localhost' identified by 'abudat';
    mysql> grant all on abudatdata.* to 'abudat'@'localhost';
    mysql> flush privileges;
    mysql> quit;


#####Troubleshooting
If during container startup, you get exceptions regarding timezone check:
    
    $ mysql_tzinfo_to_sql /usr/share/zoneinfo | mysql -u root mysql -p
    
Then add the default time zone to /etc/mysql/mysql.conf.d/mysqld.cnf, e.g.:

    [mysqld]
    ...
    default-time-zone='Europe/Berlin'
    ...
see: http://dev.mysql.com/doc/refman/5.7/en/time-zone-support.html
see: http://stackoverflow.com/a/32736024/4098376

####Install fuseki as a triple store

see: https://jena.apache.org/documentation/fuseki2/#getting-started-with-fuseki

Download apache jena fuseki 2 from https://jena.apache.org/download/. Unpack the dowloaded archive.
On linux, cd into the unpacked directory. Then run:

    $ chmod +x fuseki-server

to make the server executable. To start the server run:

    $ ./fuseki-server --update --mem /tmp/ds/

this create an non-persitent in-memory dataset for the server. To create a persistent file-based dataset run:

    $ ./fuseki-server --update --loc=/tmp/ds /ds

The parameter given along with --loc is the path to the backing file.

####Local LinkedPipes ETL installation

Setup process for Ubuntu Linux.

#####Prerequisites:
- have maven installed
- have nodejs-legacy, npm and nodejs installed

Create a Linkedpipes ETL data and working dir root and make the owner the current user:

    $ sudo mkdir /usr/local/linkedpipes_etl
    $ sudo mkdir /usr/local/linkedpipes_etl/working
    $ sudo mkdir /usr/local/linkedpipes_etl/pipelines
    $ sudo chown -R $USER.$USER /usr/local/linkedpipes_etl

Clone the sources:

    $ cd /usr/local/linkedpipes_etl
    $ git clone https://github.com/linkedpipes/etl.git

Deploy LinkedPipes

    $ cd /usr/local/linkedpipes_etl/etl
    $ mvn install

Create a configuration file:

    $ cd deploy
    $ touch configuration.properties

Create a *configuration.properties* under /usr/local/linkedpipes_etl/etl/deploy. It may look like this:

    executor.webserver.port = 8085
    executor.webserver.uri = http://localhost:8085
    
    executor.execution.working_directory = /usr/local/linkedpipes_etl/working
    executor.execution.uriPrefix = http://localhost:8080/resources/executions/
    
    executor.log.directory = /var/log
    executor.log.core.level = DEBUG
    
    executor.osgi.lib.directory = /usr/local/linkedpipes_etl/etl/deploy/osgi
    executor.osgi.working.directory = .felix/
    
    executor-monitor.webserver.port = 8081
    executor-monitor.webserver.uri = http://localhost:8081/api/v1/
    executor-monitor.log.directory = /var/log
    executor-monitor.log.core.level = DEBUG
    executor-monitor.ftp.command_port = 2221
    executor-monitor.ftp.data_ports_interval.start = 2222
    executor-monitor.ftp.data_ports_interval.end = 2225
    executor-monitor.ftp.uri = ftp://localhost:2221
    
    frontend.webserver.port = 8080
    
    storage.components.directory = /usr/local/linkedpipes_etl/etl/deploy/components
    storage.components.path.prefix = file://
    storage.pipelines.directory = /usr/local/linkedpipes_etl/pipelines
    
    domain.uri = http://localhost:8080
    
    external.fuseki.path = 
    external.working =
    external.port.start = 3300
    external.port.end = 3400

#####LinkedPipes ETL Start / Stop script
Save this script as /usr/local/bin/linkedpipes_etl.sh

    #!/bin/bash
    
    linkedpipes_etl_path="/usr/local/linkedpipes_etl/etl/deploy"
    
    usage="Usage: linkedpipes_etl.sh [start|stop]"
    
    if [ $# -eq 0 ]
    then
            echo "No arguments given, "$usage
            exit
    fi
    
    if [ $1 == "start" ]
    then
            cd $linkedpipes_etl_path
            
            echo Running executor
            ./executor.sh >> /tmp/lp_executor.log &
    
            echo Running executor-monitor
            ./executor-monitor.sh >> /tmp/lp_executor-monitor.log &
    
            echo Running frontend
            ./frontend.sh >> /tmp/lp_frontend.log &
    
    elif [ $1 == "stop" ]
    then
            echo Killing Executor
            kill `ps ax | grep /executor.jar | grep -v grep | awk '{print $1}'`
    
            echo Killing Executor-monitor
            kill `ps ax | grep /executor-monitor.jar | grep -v grep | awk '{print $1}'`
    
            echo Killing Executor-view
            kill `ps ax | grep node | grep -v grep | awk '{print $1}'`
    else
            echo "Unknown argument "$1" "$usage
    fi
Make it executable

    $ sudo chmod +x /usr/local/bin/linkedpipes_etl.sh
    
Now you can start linkedpipes etl with:

    $ linkedpipes_etl.sh start
    
To stop it run:

    $ linkedpipes_etl.sh stop


###3.Testing ABuDat With Selenium IDE
 * Assuming Firefox Browser Already installed,
 * ABuDat running on http://localhost:9000/
 * Linkedpipes_etl running on http://localhost:8080/
##### Install Selenium IDE
 * Using Firefox, download Latest Version of Selenium IDE Here 
 https://addons.mozilla.org/en-US/firefox/addon/selenium-ide/   
 * Select Install Now.  Firefox Add-ons window pops up, when the download is complete,
 * Restart Firefox
##### Open Selenium IDE 
 * To run the Selenium-IDE, simply select it from the Firefox Tools menu
##### Start Testing ABuDat
 * File menu->open a Test Case, From  semweblab2016/ABuDaT_Test/ABuDaT_UI_Test1.html
 * ABuDat will run Automatically  on http://localhost:9000/ and Selenium will start testing
