package com.pawelniewiadomski.jira.openid.activeobjects;

import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.*;

@SuppressWarnings("UnusedDeclaration")
@Table("openid_providers")
@Preload
public interface OpenIdProvider extends Entity {

    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String ENDPOINT_URL = "ENDPOINT_URL";
    public static final String EXTENSION_NAMESPACE = "EXTENSION_NAMESPACE";
    public static final String ENABLED = "ENABLED";

    @NotNull
    @PrimaryKey
    @AutoIncrement
    Long getId();
    void setId(Long id);

    @NotNull
    @Unique
    @StringLength(value = 100)
    String getName();
    void setName(final String name);

    @NotNull
    @StringLength(value = StringLength.MAX_LENGTH)
    String getEndpointUrl();
    void setEndpointUrl(String endpointUrl);

    @StringLength(value = 25)
    String getExtensionNamespace();
    void setExtensionNamespace(String namespace);

    @NotNull
    boolean isEnabled();
    void setEnabled(boolean enabled);
}