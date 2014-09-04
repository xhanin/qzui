package qzui.rest;

import com.google.common.base.Optional;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import qzui.domain.JobDefinition;
import qzui.domain.JobDescriptor;
import restx.annotations.DELETE;
import restx.annotations.GET;
import restx.annotations.POST;
import restx.annotations.RestxResource;
import restx.factory.Component;

import java.util.Collection;
import java.util.Set;

/**
 * Date: 18/2/14
 * Time: 21:31
 */
@RestxResource
@Component
public class JobResource {
    private final Scheduler scheduler;
    private final Collection<JobDefinition> definitions;

    public JobResource(Scheduler scheduler, Collection<JobDefinition> definitions) {
        this.scheduler = scheduler;
        this.definitions = definitions;
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

    @GET("/jobs")
    public Set<JobKey> getJobKeys()  {
        try {
            return scheduler.getJobKeys(GroupMatcher.anyJobGroup());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @GET("/groups/{group}/jobs")
    public Set<JobKey> getJobKeysByGroup(String group)  {
        try {
            return scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @GET("/groups/{group}/jobs/{name}")
    public Optional<JobDescriptor> getJob(String group, String name) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(new JobKey(name, group));

            if (jobDetail == null) {
                return Optional.absent();
            }

            for (JobDefinition definition : definitions) {
                if (definition.acceptJobClass(jobDetail.getJobClass())) {
                    return Optional.of(definition.buildDescriptor(
                            jobDetail, scheduler.getTriggersOfJob(jobDetail.getKey())));
                }
            }

            throw new IllegalStateException("can't find job definition for " + jobDetail
                    + " - available job definitions: " + definitions);
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
