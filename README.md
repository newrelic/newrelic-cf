[![Archived header](https://github.com/newrelic/open-source-office/raw/master/examples/categories/images/Archived.png)](https://github.com/newrelic/open-source-office/blob/master/examples/categories/index.md#archived)

# New Relic Service Broker for Cloud Foundry
This repository contains a New Relic Service Broker for Cloud Foundry that allows you to automatically bind New Relic language agents with your applications in Cloud Foundry environments. 

##Prerequisites

*    One or more New Relic accounts/sub-accounts
*    A New Relic valid license key for each account.  
 You can obtain the license key for each account from your New Relic account under 'Account Settings'
*    A running Cloud Foundry environment
*    Proxy host and port details if your PCF environment is behind a firewall



##Installation Overview

1.  Download the New Relic Service Broker JAR file and the manifest.yml file
2.  Edit the manifest.yml file
3.  Push the Service Broker jar file to Cloud Foundry as an application
4.  Create a Service Broker
5.  Enable Access to the Service Broker
6.  Create a Service for each New Relic Account
7.  Bind the Service to your application
8.  (Optional)  Add Proxy configuration
9.  Re-Stage or re push your application

##Installation Instructions
#####1.  Download the 2 files
* new-relic-service-broker.jar
* manifest.yml

#####2.  Edit the manifest.yml file
  1.  Specify the correct "domain" for your PCF environment
  2.  Add a section at the end of the file to define the following 3 environment variables:
```
env:
    SECURITY_USER_NAME: "<DIRECTOR_USER_NAME>"
    SECURITY_USER_PASSWORD: "<DIRECTOR_PASSWORD>"
    NRPLANS: '<A JSON ARRAY CONTAINING ONE OR MORE PLANS/LICENSE KEYS>'
```

**Note 1:** "env:" must be preceded with exactly 2 spaces, and each variable name must be preceded with exactly 4 spaces.

**Note 2:** The "NRPLANS" environment variable is a JSON array object. In this object you will define a key: value pair for each New Relic account you wish to provide access to.   The structure is:
>
{"planName" : "the name you want to display", "licenseKey" : "the license key from your New Relic account" } 
>

The “planName” is how your developers will know which New Relic account to use for their applications.  
Name it such that users will know which New Relic account to use.  
**Note 3.** "planName" cannot contain any spaces (you can use dashes or camelCase to separate words).  

The "licenseKey" value can be found in the "Account Administration" menu option from the top right corner of New Relic. 
Plan names are free form text with no spaces, you can use dashes between words.   
**Note 4** NRPLANS json array must be defined all in one line.

Here is a sample "NRPLANS" JSON array object:
```
NRPLANS: '[{"planName" : "New-Relic-Test", "licenseKey" : "712345678901234567890123456789012345e21d"}, {"planName" : "New-Relic-Production", "licenseKey" : "79999999999999999999999999999999999e21d"}}]'
```

#####3.  Push the Service Broker jar file to Cloud Foundry as an application
```
cd '<SERVICE_BROKER_DIRECTORY>'
cf push
```
This will push the service broker into Cloud Foundry and start it running.  
sample output:
```
broker: test-sb
service         plan                    access orgs   
newrelic-test   New-Relic-Test          none        
newrelic-test   New-Relic-Production    none        
````  

**Notes:**    
>
* Make a note of the URL for the service broker app   
* Make a note of the "service" name    
>

#####4.  Create a Service Broker    
```cf create-service-broker  <SERVICE_BROKER_NAME> <USER> <PASSWORD> <SERVICE_BROKER_URL>```
    
* SERVICE_BROKER_NAME: pick a name for your service broker
* USER: an admin user who has privileges to create service brokers
* PASSWORD: password for the admin user
* SERVICE_BROKER_URL: the url that was returned as a result of pushing the service broker app    


#####5.  Enable Access to the Service Broker    
```
cf enable-service-access <SERVICE>
```    

* SERVICE: the service name that was returned from "cf push" (i.e. "newrelic-test")    
You can also run "cf marketplace" to get the service name This command ensures that access to all plans offered by the service are available for use.



#####6.  Create a Service for each New Relic Account
Create a service for "each" of the plans (in our example you need to run “cf create-service” 2 times)
```
cf create-service <SERVICE_BROKER_SERVICE_NAME> <SERVICE_BROKER_PLAN> <SERVICE_INSTANCE>
```
* SERVICE_BROKER_SERVICE_NAME: the service name for service broker (i.e. "newrelic-test")
* SERVICE_BROKER_PLAN: plan name offered by the service broker (i.e. "New-Relic-Test")
* SERVICE_INSTANCE: descriptive name of the service instance as you wish to set (i.e. "New Relic Account for our Test Environment")

Run "cf services" to make sure all your service instances have properly been created

#####7.  Bind the Service to your application
Assuming that your applications are already pushed to Cloud Foundry, these services can be bound to your applications to use New Relic language agents and monitor your applications
```
cf bind-service <APP_NAME> <SERVICE_INSTANCE>
```
#####8.  (Optional) If you are behind a proxy, add the proxy settings to your application
```
cf set-env <APP_NAME> JAVA_OPTS "-Dnewrelic.config.proxy_host=proxy.yourCompany.com -Dnewrelic.config.proxy_port=nnn"
```
**Note:** If you're using a proxy across all of your applications, you may want to implement a PCF 'Environment Variable Group' for the staging process.
```
$ cf ssevg '{"JAVA_OPTS":"-Dnewrelic.config.proxy_host=proxy.yourCompany.com -Dnewrelic.config.proxy_port=nnn"}'
Setting the contents of the running environment variable group as admin...
OK
```
```
$ cf sevg
Retrieving the contents of the running environment variable group as admin...
OK
Variable Name   Assigned Value
JAVA_OPTS           -Dnewrelic.config.proxy_host=proxy.yourCompany.com -Dnewrelic.config.proxy_port=nnn
```
This will enable you to set the JAVA_OPTS parameters on a more global basis such that all applications would inherit the settings without the need to add application level settings to each application.   You can find more details on that here:
https://docs.pivotal.io/pivotalcf/devguide/deploy-apps/environment-variable.html#evgroups






#####9.  Re-Stage or re push your application
```
cf restage MY_SAMPLE_APP
```

**Additional Note**
If you would like to add more Services / Accounts after completing this process, it's easy.  You'll simply need to add your new plan(s), restage the broker and then enable the access. 
Here's how:
```
cf env NRPLANS: '[{"planName" : "My-New-Plan", "licenseKey" : "71234567890123456789012345678901234aabbc"}]'
cf restage MY_SAMPLE_APP
cf enable-service-access <SERVICE>
```
where JSON_OBJ = the new set of plans with new license keys.     (The old ones will stay intact).     
