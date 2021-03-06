package com.pawelniewiadomski.jira.openid.authentication.services;

import com.atlassian.sal.api.message.I18nResolver;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.pawelniewiadomski.jira.openid.authentication.PluginKey;
import com.pawelniewiadomski.jira.openid.authentication.ReturnToHelper;
import com.pawelniewiadomski.jira.openid.authentication.activeobjects.OpenIdDao;
import com.pawelniewiadomski.jira.openid.authentication.providers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@Service
public class ProviderTypeFactory {
    @Autowired
    protected I18nResolver i18nResolver;

    @Autowired
    protected OpenIdDiscoveryDocumentProvider discoveryDocumentProvider;

    @Autowired
    protected OpenIdDao openIdDao;

    @Autowired
    protected ReturnToHelper returnToHelper;

    @Autowired
    protected PluginKey pluginKey;

    @Nonnull
    public ProviderType getProviderTypeById(@Nonnull String id) throws IllegalArgumentException {
        final ProviderType providerType = ID_MAP.get().get(id);
        if (providerType != null) {
            return providerType;
        }
        throw new IllegalArgumentException("No such ProviderType with id " + id);
    }

    @Nonnull
    public Map<String, ProviderType> getAllProviderTypes() {
        return ID_MAP.get();
    }

    private final Supplier<Map<String, ProviderType>> ID_MAP = Suppliers.memoize(new Supplier<Map<String, ProviderType>>() {
        @Override
        public Map<String, ProviderType> get() {
            final ImmutableList.Builder<ProviderType> providerTypes = ImmutableList.builder();

            providerTypes.add(
                    new GoogleProviderType(i18nResolver, openIdDao, returnToHelper),
                    new FacebookProviderType(i18nResolver, openIdDao, returnToHelper),
                    new LinkedInProviderType(i18nResolver, openIdDao, returnToHelper),
                    new GitHubProviderType(i18nResolver, openIdDao, returnToHelper),
                    new VkProviderType(i18nResolver, openIdDao, returnToHelper));

            if (!pluginKey.areCustomProvidersDisabled()) {
                providerTypes.add(
                        new OpenIdProviderType(i18nResolver, openIdDao),
                        new DiscoverablyOauth2ProviderType(i18nResolver, openIdDao, discoveryDocumentProvider, returnToHelper));
            }

            return ImmutableMap.<String, ProviderType>builder().putAll(Maps.uniqueIndex(providerTypes.build(), new Function<ProviderType, String>() {
                @Override
                public String apply(@Nullable ProviderType abstractProviderType) {
                    return abstractProviderType.getId();
                }
            })).build();
        }
    });
}
