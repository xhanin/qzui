package qzui;

import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 19/2/14
 * Time: 06:30
 */
public abstract class AbstractJobDefinition implements JobDefinition {
    protected <T extends JobDescriptor> T setupDescriptorFromDetail(T jobDescriptor, JobDetail jobDetail, List<? extends Trigger> triggersOfJob) {
        jobDescriptor.setGroup(jobDetail.getKey().getGroup())
                .setName(jobDetail.getKey().getName())
                .setData(jobDetail.getJobDataMap().getWrappedMap());

        List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();

        for (Trigger trigger : triggersOfJob) {
            triggerDescriptors.add(TriggerDescriptor.buildDescriptor(trigger));
        }

        jobDescriptor.setTriggerDescriptors(triggerDescriptors);

        return jobDescriptor;
    }
}
