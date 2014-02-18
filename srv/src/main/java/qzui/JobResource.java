package qzui;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import restx.annotations.DELETE;
import restx.annotations.GET;
import restx.annotations.POST;
import restx.annotations.RestxResource;
import restx.factory.Component;

import java.util.Set;

/**
 * Date: 18/2/14
 * Time: 21:31
 */
@RestxResource
@Component
public class JobResource {
    private final Scheduler scheduler;

    public JobResource(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /*
    Example:

        {"type":"log", "name":"test2", "group":"log", "triggers": [{"cron":"0/2 * * * * ?"}]}

        {"type":"http", "name":"google-humans", "method":"GET", "url":"http://www.google.com/humans.txt", "triggers": [{"when":"now"}]}
     */
    @POST("/groups/{group}/jobs")
    public JobDescriptor addJob(String group, JobDescriptor jobDescriptor) {
        try {
            jobDescriptor.setGroup(group);
            Set<Trigger> triggers = jobDescriptor.buildTriggers();
            JobDetail jobDetail = jobDescriptor.buildJobDetail();
            if (triggers.isEmpty()) {
                scheduler.addJob(jobDetail, false);
            } else {
                scheduler.scheduleJob(jobDetail, triggers, false);
            }
            return jobDescriptor;
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @GET("/groups/{group}/jobs")
    public Set<JobKey> getJobKeys(String group)  {
        try {
            return scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @DELETE("/groups/{group}/jobs/{name}")
    public void deleteJob(String group, String name) {
        try {
            scheduler.deleteJob(new JobKey(name, group));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
