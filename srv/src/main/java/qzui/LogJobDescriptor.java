package qzui;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.JobBuilder.newJob;

/**
 * Date: 18/2/14
 * Time: 21:51
 */
public class LogJobDescriptor extends JobDescriptor {
    private static final Logger logger = LoggerFactory.getLogger(LogJobDescriptor.class);

    @Override
    public JobDetail buildJobDetail() {
        return newJob(LogJob.class)
                .withIdentity(getName(), getGroup())
                .usingJobData(new JobDataMap(getData()))
                .build();
    }

    public static class LogJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            logger.info("{} - {}", context.getJobDetail().getKey(), context.getJobDetail().getJobDataMap().getWrappedMap());
        }
    }
}
