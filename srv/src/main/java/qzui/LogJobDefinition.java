package qzui;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.Component;

import java.util.List;

import static org.quartz.JobBuilder.newJob;

/**
 * Date: 19/2/14
 * Time: 06:24
 */
@Component
public class LogJobDefinition extends AbstractJobDefinition {
    private static final Logger logger = LoggerFactory.getLogger(LogJobDefinition.class);

    @Override
    public boolean acceptJobClass(Class<? extends Job> jobClass) {
        return jobClass.getName() == LogJob.class.getName();
    }

    @Override
    public JobDescriptor buildDescriptor(JobDetail jobDetail, List<? extends Trigger> triggersOfJob) {
        return setupDescriptorFromDetail(new LogJobDescriptor(), jobDetail, triggersOfJob);
    }

    public static class LogJobDescriptor extends JobDescriptor {

        @Override
        public JobDetail buildJobDetail() {
            return newJob(LogJob.class)
                    .withIdentity(getName(), getGroup())
                    .usingJobData(new JobDataMap(getData()))
                    .build();
        }

    }

    public static class LogJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            logger.info("{} - {}", context.getJobDetail().getKey(),
                    context.getJobDetail().getJobDataMap().getWrappedMap());
        }
    }
}
