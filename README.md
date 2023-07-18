# [Ant Media Server](https://antmedia.io/) Streaming Application with [Okta](https://www.okta.com/) authentication

This Streaming Application is derived from Ant Media Server base [Stream Application](https://github.com/ant-media/StreamApp)
To access sample webpages in this application you have to login with your Okta (Identity Service) account.

# How to use?

### Step 1: Add a SAML application on Okta

To begin, you’ll need an Okta developer account. You can create one at developer.okta.com/signup.

Then, log in to your account and go to Applications > Create App Integration. Select SAML 2.0 and click Next. Name your app something like Spring Boot SAML and click Next.

Use the following settings:

Single sign on URL: http://localhost:5080/OktaApp/saml/SSO

Use this for Recipient URL and Destination URL: ✅ (the default)

Audience URI: http://localhost:5080/OktaApp/saml/metadata

Then click Next. Select the following options:

I’m an Okta customer adding an internal app

This is an internal app that we have created

Select Finish.

Okta will create your app, and you will be redirected to its Sign On tab. Copy metadata url. It should be like:

https://dev-88110941.okta.com/app/exkaf2qeilk3fwVgw5d7/sso/saml/metadata

Go to your app’s Assignment tab and add your account to assignment.

### Step 2: Build OktaApp (Optional)

Clone this repository
`git clone https://github.com/ant-media/OktaApp.git`

Edit the following file according to the metadata url you got in Step 1.
`src/main/webapp/WEB-INF/red5-web.xml`

You should edit the values in the following part of the file:

```
<bean id="saml.config" class="io.antmedia.saml.SamlSecurityConfig">
  <property name="samlKeystoreLocation" value="/home/burak/temp/keystore.jks" />
  <property name="samlKeystorePassword" value="123456" />
  <property name="samlKeystoreAlias" value="spring" />
  <property name="defaultIdp" value="http://www.okta.com/exkaf2qeilk3fwVgw5d7" />
  <property name="metadataUrl" value="https://dev-88110941.okta.com/app/exkaf2qeilk3fwVgw5d7/sso/saml/metadata" />
</bean>
<bean id="saml.webConfig" class="io.antmedia.saml.WebSecurityConfig">
  <property name="samlAudience" value="http://localhost:5080/OktaApp/saml/metadata" />
</bean>
```

Then run build command (requires Java 11 and maven):

`mvn clean install -DskipTests -Dgpg.skip=true`

The inf build is successfull you will have `OktaApp.war` in `target` directory.

### Step 3: Install OktaApp.war

If you build it by yourself as in Step 2, you already have war file. Otherwise you can use the OktaApp.war in this repository.

- Login Ant Media Server Dashboard
- Click **New Application** Button
- Select your war file
- Name your application as `StreamApp` (note that this application name should be compatible with the URL you set in Step 1)

If you don't do it in Step 2 you should edit the application settings according to the metadata url you got in Step 1. Edit `/usr/local/antmedia/webapp/OktaApp/WEB-INF/red5-web.xml` file. (to see where you should edit, please check Step 2) After saving settings, then you should restart Ant Media Server.

### Step 4: Test

Now, if everything is done until now we can test if it works.

Try to open WebRTC publish sample page `http://localhost:5080/OktaApp/samples/publish_webrtc.html` in your browser. If everything is ok, browser forward you to Okta login page. After logging in you should see the publish page.
