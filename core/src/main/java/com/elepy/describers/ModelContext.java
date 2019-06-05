package com.elepy.describers;

import com.elepy.dao.Crud;
import com.elepy.evaluators.ObjectEvaluator;
import com.elepy.id.IdentityProvider;

import java.util.List;

public class ModelContext<T> {
    private final Model<T> model;

    private final Crud<T> crud;
    private final IdentityProvider<T> identityProvider;
    private final List<ObjectEvaluator<T>> objectEvaluators;

    public ModelContext(Model<T> model,
                        Crud<T> crud,
                        IdentityProvider<T> identityProvider,
                        List<ObjectEvaluator<T>> objectEvaluators) {
        this.model = model;
        this.crud = crud;
        this.identityProvider = identityProvider;
        this.objectEvaluators = objectEvaluators;
    }


    public Crud<T> getCrud() {
        return crud;
    }

    public void changeModel(ModelChange modelChange) {
        modelChange.change(this);
    }

    public Model<T> getModel() {
        return model;
    }


    public IdentityProvider<T> getIdentityProvider() {
        return identityProvider;
    }

    public List<ObjectEvaluator<T>> getObjectEvaluators() {
        return objectEvaluators;
    }

    public String getName() {
        return model.getName();
    }

    public String getSlug() {
        return model.getSlug();
    }

    public Class<T> getModelType() {
        return model.getJavaClass();
    }

    public String getIdField() {
        return model.getIdField();
    }
}