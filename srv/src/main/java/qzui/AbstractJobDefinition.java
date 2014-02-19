package qzui;

import org.quartz.JobDetail;

/**
 * Date: 19/2/14
 * Time: 06:30
 */
public abstract class AbstractJobDefinition implements JobDefinition {
    protected <T extends JobDescriptor> T setupDescriptorFromDetail(JobDetail jobDetail, T jobDescriptor) {
        jobDescriptor.setGroup(jobDetail.getKey().getGroup())
                .setName(jobDetail.getKey().getName())
                .setData(jobDetail.getJobDataMap().getWrappedMap());

        return jobDescriptor;
    }
}
