package com.yoursway.model.repository;

public abstract class BackgroundConsumer {
    
    volatile IResolver resolver;

    public BackgroundConsumer(IRepository repository) {
        resolver = repository.addBackgroundConsumer(new IConsumer() {

            public void consume(IResolver resolver$) {
                resolver = resolver$;
                somethingChanged(resolver$);
            }
            
        });
    }
    
    protected void somethingChanged(IResolver resolver) {
        
    }
    
    public IResolver resolver() {
        return resolver;
    }
    
}
