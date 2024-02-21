package io.github.zekerzhayard.npe_eventloader.gradle.unsafe.modifiers;

public interface IClassModifier {
    String getClassName();

    byte[] modify(byte[] classBytes);
}
