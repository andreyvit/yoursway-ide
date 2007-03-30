package com.yoursway.utils;

import org.jvyaml.Composer;
import org.jvyaml.DefaultYAMLConfig;
import org.jvyaml.DefaultYAMLFactory;
import org.jvyaml.YAMLConfig;
import org.jvyaml.YAMLFactory;
import org.jvyaml.nodes.Node;

public class YamlUtil {
    public static Node parseYaml(String yaml) {
        YAMLConfig cfg = new DefaultYAMLConfig();
        YAMLFactory fact = new DefaultYAMLFactory();
        final Composer composer = fact.createComposer(fact.createParser(fact.createScanner(yaml), cfg), fact
                .createResolver());
        
        if (composer.checkNode()) {
            return composer.getNode();
        } else
            return null;
    }
}
