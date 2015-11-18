package qzui.domain;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import static org.quartz.JobBuilder.newJob;

/**
 * Date: 19/2/14
 * Time: 06:34
 */
@Component
public class ShellJobDefinition extends AbstractJobDefinition {
    private static final Logger logger = LoggerFactory.getLogger(ShellJobDefinition.class);

    @Override
    public boolean acceptJobClass(Class<? extends Job> jobClass) {
        return jobClass.getName() == ShellJob.class.getName();
    }

    @Override
    public JobDescriptor buildDescriptor(JobDetail jobDetail, List<? extends Trigger> triggersOfJob) {
        ShellJobDescriptor jobDescriptor = setupDescriptorFromDetail(new ShellJobDescriptor(), jobDetail, triggersOfJob);

        return jobDescriptor
                .setScript((String) jobDescriptor.getData().remove("script"));
    }

    public static class ShellJobDescriptor extends JobDescriptor {

        private String script;

        public String getScript() {
            return script;
        }

        public ShellJobDescriptor setScript(final String script) {
            this.script = script;
            return this;
        }

        @Override
        public JobDetail buildJobDetail() {
            JobDataMap dataMap = new JobDataMap(getData());
            dataMap.put("script", script);

            return newJob(ShellJob.class)
                    .withIdentity(getName(), getGroup())
                    .usingJobData(dataMap)
                    .build();
        }

        @Override
        public String toString() {
            return "ShellJob{" +
                    "script='" + script + '\'' +
                    '}';
        }

    }

    public static class ShellJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {

            JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
            String script = jobDataMap.getString("script");

            String output = executeCommand(script);


            logger.info("{} => {}", script, output);
        }

        private String executeCommand(String command) {

            StringBuilder output = new StringBuilder();

            Process p;
            try {
                p = Runtime.getRuntime().exec(command);
                p.waitFor();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line;
                while ((line = reader.readLine())!= null) {
                    output.append(line + "\n");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return output.toString();

        }
    }

}

