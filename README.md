qzui
====

Qzui, a basic REST and Web front end over Quartz Scheduler.

## Features

### Simple UI

* simple UI to get the list of jobs and their triggers
* delete (cancel) a job

### Jobs type

* Log job: log something, mainly used for testing
* HTTP jobs: make HTTP request

### Timers

* 'now'
* at specified time
* using cron syntax

### REST API

* create new jobs
* get list of jobs
* get job detail
* delete (cancel) a job

### Easy hacking

As of Feb 2014, Qzui has been developed in a couple of hours, the server part consists of less than 500 LOC:

```
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                            10            110             63            480
```


## Hacking


### get, build and run

```
git clone https://github.com/xhanin/qzui.git
cd qzui
mvn package
```

This will produce a war in the `srv/target/` directory that you can deploy in any servlet 2.5+ container.

You can also run the server using [RESTX](http://restx.io/) with `restx app run` for dev mode (with auto compile for easy hacking), and `restx app run --prod` for production mode (launch it in `srv` directory).

To launch it from your IDE use the `AppServer` class.

### add new job types

Adding a new job type is simple, take example from existing ones:

[HttpJobDefinition](https://github.com/xhanin/qzui/blob/master/srv/src/main/java/qzui/HttpJobDefinition.java)

And do not forget to add the type json value in [JobDescriptor](https://github.com/xhanin/qzui/blob/master/srv/src/main/java/qzui/JobDescriptor.java)

### add new triggers

Hack the [TriggerDescriptor](https://github.com/xhanin/qzui/blob/master/srv/src/main/java/qzui/TriggerDescriptor.java) class.

### Hack UI

The basic UI is developed using AngularJS + TW Boostrap, check [ui/app](https://github.com/xhanin/qzui/tree/master/ui/app).

## Usage

When the server is launched, open your browser at http://localhost:8080/ and you will get the list of jobs.

To create a job use the REST API: do a POST on http://localhost:8080/groups/:group/jobs

To do so you can use the RESTX API console (login admin/juma) at http://127.0.0.1:8080/api/@/ui/api-docs/#/

Note that jobs MUST have unique names.

### Examples:

#### HTTP Job, scheduled at fixed date/time:

```
{
  "type":"http",
  "name":"google-humans",
  "method":"GET",
  "url":"http://www.google.com/humans.txt",
  "triggers": [
        {"when":"2014-11-05T13:15:30Z"}
  ]
}
```

#### HTTP Job, scheduled now.

```
{
  "type":"http",
  "name":"google-humans",
  "method":"GET",
  "url":"http://www.google.com/humans.txt",
  "triggers": [
        {"when":"now"}
  ]
}
```

#### HTTP Job, scheduled using cron syntax.

```
{
  "type":"http",
  "name":"google-humans",
  "method":"GET",
  "url":"http://www.google.com/humans.txt",
  "triggers": [
        {"cron":"0/2 * * * * ?"}
  ]
}
```

## Screenshots

![Qzui UI](https://i.cloudup.com/rA5oWU9hqd-2000x2000.png)

![Qzui post jobs](https://i.cloudup.com/ZCkwMOtVpr-3000x3000.png)



