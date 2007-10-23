package com.yoursway.model.sample;

import java.text.MessageFormat;

import com.yoursway.model.repository.IConsumer;
import com.yoursway.model.repository.IResolver;

public class MegaModelPrinter implements IConsumer {
    
    public void consume(IResolver resolver) {
        System.out.println();
        System.out.println("ALL FILES WITH CLASS NAMES");
        System.out.println("--------------------------");
        IResourceModelRoot resourceModelRoot = resolver.obtainRoot(IResourceModelRoot.class);
        for (IResourceProject project : resolver.get(resourceModelRoot.projects())) {
            for (IResourceFile file : resolver.get(project.files())) {
                IAST ast = resolver.get(file.ast());
                System.out.println(MessageFormat.format("{0}/{1} - {2}", project.getName(), file.getName(),
                        ast.getTopLevelClassName()));
            }
        }
    }
    
}
