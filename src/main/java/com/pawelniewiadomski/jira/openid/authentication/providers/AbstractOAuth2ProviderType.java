package com.pawelniewiadomski.jira.openid.authentication.providers;

import com.atlassian.fugue.Either;
import com.atlassian.sal.api.message.I18nResolver;
import com.google.common.collect.ImmutableMap;
import com.pawelniewiadomski.jira.openid.authentication.activeobjects.OpenIdDao;
import com.pawelniewiadomski.jira.openid.authentication.activeobjects.OpenIdProvider;
import com.pawelniewiadomski.jira.openid.authentication.rest.responses.ProviderBean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.defaultString;
import static org.apache.commons.lang.StringUtils.isEmpty;

public abstract class AbstractOAuth2ProviderType extends AbstractProviderType implements OAuth2ProviderType {
    public AbstractOAuth2ProviderType(I18nResolver i18nResolver, OpenIdDao openIdDao) {
        super(i18nResolver, openIdDao);
    }

    @Override
    public boolean isSkipClientInfo() {
        return false;
    }

    @Override
    public boolean isSkipCallback() {
        return false;
    }

    @Override
    public boolean isSkipUrl() {
        return true;
    }

    @Nonnull
    public abstract String getAuthorizationUrl();

    @Nonnull
    public abstract String getCallbackId();

    @Nullable
    @Override
    public String getCreatedProviderName() {
        return getName();
    }

    @Override
    public Either<Errors, Map<String, Object>> validateCreateOrUpdate(@Nullable OpenIdProvider provider, ProviderBean providerBean) {
        Errors errors = new Errors();

        validateName(provider, getCreatedProviderName(), errors);

        if (isEmpty(providerBean.getClientId())) {
            errors.addError("clientId", i18nResolver.getText("configuration.clientId.empty"));
        }
        if (isEmpty(providerBean.getClientSecret())) {
            errors.addError("clientSecret", i18nResolver.getText("configuration.clientSecret.empty"));
        }

        if (errors.hasAnyErrors()) {
            return Either.left(errors);
        } else {
            final Map<String, Object> map = new HashMap<>();
            map.put(OpenIdProvider.NAME, getCreatedProviderName());
            map.put(OpenIdProvider.ENDPOINT_URL, getAuthorizationUrl());
            map.put(OpenIdProvider.CALLBACK_ID, getCallbackId()); // changing the id will break all existing urls
            map.put(OpenIdProvider.ALLOWED_DOMAINS, providerBean.getAllowedDomains());
            map.put(OpenIdProvider.PROVIDER_TYPE, getId());
            map.put(OpenIdProvider.CLIENT_ID, providerBean.getClientId());
            map.put(OpenIdProvider.CLIENT_SECRET, providerBean.getClientSecret());
            return Either.right(map);
        }
    }
}
