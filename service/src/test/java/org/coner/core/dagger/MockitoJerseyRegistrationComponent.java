package org.coner.core.dagger;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MockitoJerseyRegistrationModule.class)
public interface MockitoJerseyRegistrationComponent extends JerseyRegistrationComponent {
}
