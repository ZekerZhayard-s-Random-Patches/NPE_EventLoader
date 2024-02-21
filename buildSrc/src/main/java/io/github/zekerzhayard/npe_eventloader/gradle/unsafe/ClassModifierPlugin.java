package io.github.zekerzhayard.npe_eventloader.gradle.unsafe;

import java.util.ServiceLoader;

import io.github.zekerzhayard.npe_eventloader.gradle.unsafe.modifiers.IClassModifier;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ClassModifierPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        try {
            ClassModifierManager.init();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        ServiceLoader<IClassModifier> loader = ServiceLoader.load(IClassModifier.class);
        for (IClassModifier modifier : loader) {
            ClassModifierManager.registerModifier(modifier);
        }
    }
}
