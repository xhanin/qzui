package qzui;

import org.quartz.Job;
import org.quartz.JobDetail;

/**
 * Date: 19/2/14
 * Time: 06:23
 */
public interface JobDefinition {
    boolean acceptJobClass(Class<? extends Job> jobClass);
    JobDescriptor buildDescriptor(JobDetail jobDetail);
}
