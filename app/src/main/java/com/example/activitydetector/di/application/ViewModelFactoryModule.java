package com.example.activitydetector.di.application;

import androidx.lifecycle.ViewModel;
import com.example.activitydetector.di.ViewModelFactory;
import dagger.Module;
import dagger.Provides;
import java.util.Map;
import javax.inject.Provider;

@Module
public class ViewModelFactoryModule {

    @Provides
    ViewModelFactory viewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap) {
        return new ViewModelFactory(providerMap);
    }

}
