package qzui.domain;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Optional;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.Component;

import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.quartz.JobBuilder.newJob;

/**
 * Date: 19/2/14
 * Time: 06:34
 */
@Component
public class HttpJobDefinition extends AbstractJobDefinition {

    private static final Logger logger = LoggerFactory.getLogger(HttpJobDefinition.class);

    @Override
    public boolean acceptJobClass(Class<? extends Job> jobClass) {
        return jobClass.getName() == HttpJob.class.getName();
    }

    @Override
    public JobDescriptor buildDescriptor(JobDetail jobDetail, List<? extends Trigger> triggersOfJob) {
        HttpJobDescriptor jobDescriptor = setupDescriptorFromDetail(new HttpJobDescriptor(), jobDetail, triggersOfJob);

        return jobDescriptor
                .setUrl((String) jobDescriptor.getData().remove("url"))
                .setMethod((String) jobDescriptor.getData().remove("method"))
                .setBody((String) jobDescriptor.getData().remove("body"));
    }

    public static class HttpJobDescriptor extends JobDescriptor {

        private String url;
        private String method = "POST";
        private String body;
        private String contentType;
        private String login;
        private String pwdHash;

        private HttpConfiguration httpConfiguration;

        public String getUrl() {
            return url;
        }

        public String getMethod() {
            return method;
        }

        public String getBody() {
            return body;
        }

        public String getContentType() {
            return contentType;
        }

        public String getLogin() {
            return login;
        }

        public String getPwdHash() {
            return pwdHash;
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

        public HttpJobDescriptor setContentType(final String contentType) {
            this.contentType = contentType;
            return this;
        }

        public HttpJobDescriptor setPwdHash(final String pwdHash) {
            this.pwdHash = pwdHash;
            return this;
        }

        public HttpJobDescriptor setLogin(final String login) {
            this.login = login;
            return this;
        }

        public HttpConfiguration getHttpConfiguration() {
            return httpConfiguration;
        }

        public HttpJobDescriptor setHttpConfiguration(final HttpConfiguration httpConfiguration) {
            this.httpConfiguration = httpConfiguration;
            return this;
        }

        @Override
        public JobDetail buildJobDetail() {

            JobDataMap dataMap = new JobDataMap(getData());
            dataMap.put("url", url);
            dataMap.put("method", method);
            dataMap.put("body", body);
            dataMap.put("contentType", contentType);
            dataMap.put("login", login);
            dataMap.put("pwd", pwdHash);

            configure(dataMap);

            return newJob(HttpJob.class)
                    .withIdentity(getName(), getGroup())
                    .usingJobData(dataMap)
                    .build();
        }

        private void configure(JobDataMap dataMap) {
            Optional<String> trustAllCerts;
            Optional<String> trustAllHosts;
            if (getHttpConfiguration() == null) {
                trustAllCerts = Optional.fromNullable(System.getProperty(HttpConfiguration.TRUST_ALL_CERTS_PROPERTY));
                trustAllHosts = Optional.fromNullable(System.getProperty(HttpConfiguration.TRUST_ALL_HOSTS_PROPERTY));
            } else {
                trustAllCerts = Optional.of(Boolean.toString(getHttpConfiguration().isTrustAllCerts()));
                trustAllHosts = Optional.of(Boolean.toString(getHttpConfiguration().isTrustAllHosts()));
            }
            dataMap.put(HttpConfiguration.TRUST_ALL_CERTS_FIELD, trustAllCerts.or(Boolean.toString(false)));
            dataMap.put(HttpConfiguration.TRUST_ALL_HOSTS_FIELD, trustAllHosts.or(Boolean.toString(false)));
        }

        @Override
        public String toString() {
            return "HttpJobDescriptor{" +
                    "url='" + url + '\'' +
                    ", method='" + method + '\'' +
                    ", body='" + body + '\'' +
                    ", contentType='" + contentType + '\'' +
                    '}';
        }

    }

    public static class HttpJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {

            JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
            String url = jobDataMap.getString("url");
            String method = jobDataMap.getString("method");
            HttpRequest request = new HttpRequest(url, method);

            String body = "";
            if (!isNullOrEmpty(jobDataMap.getString("body"))) {
                body = jobDataMap.getString("body");
            }

            setContentType(jobDataMap, request);
            setCrendentials(jobDataMap, request);
            setSecurityParams(jobDataMap, request);

            request.send(body);
            int code = request.code();
            String responseBody = request.body();

            logger.info("{} {} => {}\n{}", method, url, code, responseBody);
        }

        private void setSecurityParams(JobDataMap jobDataMap, HttpRequest request) {
            if (jobDataMap.getBooleanFromString(HttpConfiguration.TRUST_ALL_HOSTS_FIELD)) {
                request.trustAllHosts();
            }
            if (jobDataMap.getBooleanFromString(HttpConfiguration.TRUST_ALL_CERTS_FIELD)) {
                request.trustAllCerts();
            }
        }

        private void setCrendentials(JobDataMap jobDataMap, HttpRequest request) {
            String login = jobDataMap.getString("login");
            String pwd = jobDataMap.getString("pwd");
            if (!isNullOrEmpty(login) && !isNullOrEmpty(pwd)) {
                request.basic(login, pwd);
            }
        }

        private void setContentType(JobDataMap jobDataMap, HttpRequest request) {
            String contentType = jobDataMap.getString("contentType");
            if (!isNullOrEmpty(contentType)) {
                request.contentType(contentType);
            }
        }
    }

}
