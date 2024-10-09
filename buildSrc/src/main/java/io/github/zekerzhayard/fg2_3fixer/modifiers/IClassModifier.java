package io.github.zekerzhayard.fg2_3fixer.modifiers;

public interface IClassModifier {
    String getClassName();

    byte[] modify(byte[] classBytes);
}
