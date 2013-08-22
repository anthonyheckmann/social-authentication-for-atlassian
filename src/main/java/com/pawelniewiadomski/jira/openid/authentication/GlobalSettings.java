package com.pawelniewiadomski.jira.openid.authentication;

import com.atlassian.jira.util.JiraUtils;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO: Document this class / interface here
 *
 * @since v5.2
 */
@Service
public class GlobalSettings {

    public static final String SHOULD_CREATE_USERS = "should.create.users";
    public static final String ADVANCED_SETTINGS_ON = "advanced.settings.on";

    @Autowired
    PluginSettingsFactory pluginSettingsFactory;

    public boolean isAdvanced() {
        return Boolean.valueOf((String) pluginSettingsFactory.createGlobalSettings().get(ADVANCED_SETTINGS_ON));
    }

    public void setAdvanced(boolean value) {
        pluginSettingsFactory.createGlobalSettings().put(ADVANCED_SETTINGS_ON, Boolean.toString(value));
    }

    public boolean isCreatingUsers() {
        return JiraUtils.isPublicMode() || Boolean.valueOf((String) pluginSettingsFactory.createGlobalSettings().get(SHOULD_CREATE_USERS));
    }

    public void setCreatingUsers(boolean createUsers) {
        pluginSettingsFactory.createGlobalSettings().put(SHOULD_CREATE_USERS, Boolean.toString(createUsers));
    }
}
