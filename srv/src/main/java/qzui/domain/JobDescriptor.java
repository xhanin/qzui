package qzui.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.util.*;

/**
 * Date: 18/2/14
 * Time: 21:35
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HttpJobDefinition.HttpJobDescriptor.class, name = "http"),
        @JsonSubTypes.Type(value = LogJobDefinition.LogJobDescriptor.class, name = "log"),
        @JsonSubTypes.Type(value = ShellJobDefinition.ShellJobDescriptor.class, name = "shell")
})
public abstract class JobDescriptor {
    private String name;
    private String group;
    private Map<String, Object> data = new LinkedHashMap<>();

    @JsonProperty("triggers")
    private List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public List<TriggerDescriptor> getTriggerDescriptors() {
        return triggerDescriptors;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public JobDescriptor setName(final String name) {
        this.name = name;
        return this;
    }

    public JobDescriptor setGroup(final String group) {
        this.group = group;
        return this;
    }

    public JobDescriptor setTriggerDescriptors(final List<TriggerDescriptor> triggerDescriptors) {
        this.triggerDescriptors = triggerDescriptors;
        return this;
    }

    public JobDescriptor setData(final Map<String, Object> data) {
        this.data = data;
        return this;
    }

    @JsonIgnore
    public Set<Trigger> buildTriggers() {
        Set<Trigger> triggers = new LinkedHashSet<>();
        for (TriggerDescriptor triggerDescriptor : triggerDescriptors) {
            triggers.add(triggerDescriptor.buildTrigger());
        }

        return triggers;
    }

    public abstract JobDetail buildJobDetail();

    @Override
    public String toString() {
        return "JobDescriptor{" +
                "name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", data=" + data +
                ", triggerDescriptors=" + triggerDescriptors +
                '}';
    }
}
