package qzui;

import org.joda.time.DateTime;
import org.quartz.Trigger;

import java.util.UUID;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Date: 18/2/14
 * Time: 21:55
 */
public class TriggerDescriptor {
    private String name;
    private String group;

    private String when;
    private String cron;

    public Trigger buildTrigger() {
        if (!isNullOrEmpty(cron)) {
            return newTrigger()
                    .withIdentity(buildName(), group)
                    .withSchedule(cronSchedule(cron))
                    .build();
        } else if (!isNullOrEmpty(when)) {
            if ("now".equalsIgnoreCase(when)) {
                return newTrigger()
                        .withIdentity(buildName(), group)
                        .build();
            }

            DateTime dateTime = DateTime.parse(when);
            return newTrigger()
                    .withIdentity(buildName(), group)
                    .startAt(dateTime.toDate())
                    .build();
        }
        throw new IllegalStateException("unsupported trigger descriptor " + this);
    }

    private String buildName() {
        return isNullOrEmpty(name) ? "trigger-" + UUID.randomUUID() : name;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getWhen() {
        return when;
    }

    public String getCron() {
        return cron;
    }

    public TriggerDescriptor setName(final String name) {
        this.name = name;
        return this;
    }

    public TriggerDescriptor setGroup(final String group) {
        this.group = group;
        return this;
    }

    public TriggerDescriptor setWhen(final String when) {
        this.when = when;
        return this;
    }

    public TriggerDescriptor setCron(final String cron) {
        this.cron = cron;
        return this;
    }

    @Override
    public String toString() {
        return "TriggerDescriptor{" +
                "name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", when='" + when + '\'' +
                ", cron='" + cron + '\'' +
                '}';
    }
}
