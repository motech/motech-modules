package org.motechproject.commcare.tasks.builder;

import org.motechproject.commcare.tasks.builder.model.CaseTypeWithDisplayName;
import org.motechproject.commcare.tasks.builder.model.FormWithDisplayName;

import java.util.Set;

/**
 * Helper class used for storing configuration name as well as all forms and cases that originated from the said
 * configuration.
 */
public class ConfigurationData {

    private String configName;

    private Set<FormWithDisplayName> formsWithApplication;

    private Set<CaseTypeWithDisplayName> casesWithApplication;

    /**
     * Creates an instance of the {@link ConfigurationData} class. It will store the given {@code formsWithApplication}
     * , {@code casesWithApplication} mapped to application names for the configuration with the given {@code configName}.
     * @param configName  the name of the configuration
     * @param formsWithApplication  the list of all forms that originate from the given configuration
     * @param casesWithApplication  the list of all cases that originate from the given configuration
     */
    public ConfigurationData(String configName, Set<FormWithDisplayName> formsWithApplication, Set<CaseTypeWithDisplayName> casesWithApplication) {
        this.configName = configName;
        this.formsWithApplication = formsWithApplication;
        this.casesWithApplication = casesWithApplication;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Set<FormWithDisplayName> getFormsWithApplication() {
        return formsWithApplication;
    }

    public void setFormsWithApplication(Set<FormWithDisplayName> formsWithApplication) {
        this.formsWithApplication = formsWithApplication;
    }

    public Set<CaseTypeWithDisplayName> getCasesWithApplication() {
        return casesWithApplication;
    }

    public void setCasesWithApplication(Set<CaseTypeWithDisplayName> casesWithApplication) {
        this.casesWithApplication = casesWithApplication;
    }
}
