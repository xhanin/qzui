package qzui;

import com.github.kevinsawicki.http.HttpRequest;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.quartz.JobBuilder.newJob;

/**
 * Date: 18/2/14
 * Time: 21:51
 */
public class HttpJobDescriptor extends JobDescriptor {
    private static final Logger logger = LoggerFactory.getLogger(HttpJobDescriptor.class);

    private String url;
    private String method = "POST";
    private String body;

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }

    public HttpJobDescriptor setBody(final String body) {
        this.body = body;
        return this;
    }

    public HttpJobDescriptor setMethod(final String method) {
        this.method = method;
        return this;
    }

    public HttpJobDescriptor setUrl(final String url) {
        this.url = url;
        return this;
    }

    @Override
    public JobDetail buildJobDetail() {
        JobDataMap dataMap = new JobDataMap(getData());
        dataMap.put("url", url);
        dataMap.put("method", method);
        dataMap.put("body", body);
        return newJob(HttpJob.class)
                .withIdentity(getName(), getGroup())
                .usingJobData(dataMap)
                .build();
    }

    @Override
    public String toString() {
        return "HttpJobDescriptor{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    public static class HttpJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
            String url = jobDataMap.getString("url");
            String method = jobDataMap.getString("method");
            HttpRequest request = new HttpRequest(url, method);
            if (!isNullOrEmpty(jobDataMap.getString("body"))) {
                request.send(jobDataMap.getString("body"));
            }

            int code = request.code();
            String responseBody = request.body();
            logger.info("{} {} => {}\n{}", method, url, code, responseBody);
        }
    }
}
