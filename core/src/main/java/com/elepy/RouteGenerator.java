package com.elepy;

import com.elepy.concepts.ObjectEvaluator;
import com.elepy.concepts.describers.StructureDescriber;
import com.elepy.dao.Crud;
import com.elepy.http.HttpMethod;
import com.elepy.models.AccessLevel;
import com.elepy.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.elepy.http.RouteBuilder.anElepyRoute;

public class RouteGenerator<T> {
    private static final Logger logger = LoggerFactory.getLogger(RouteGenerator.class);
    private final ResourceDescriber<T> restModel;
    private final Class<T> clazz;
    private final String baseSlug;
    private final Elepy elepy;


    public RouteGenerator(Elepy elepy, ResourceDescriber<T> resourceDescriber, Class<T> tClass) {
        this.restModel = resourceDescriber;

        this.clazz = tClass;
        this.baseSlug = elepy.getBaseSlug();
        this.elepy = elepy;

    }

    private String evaluateHasIdField(Class cls) {

        return ClassUtils.getPropertyName(ClassUtils.getIdField(cls).orElseThrow(() -> new IllegalStateException(cls.getName() + " doesn't have a valid identifying field, please annotate a String field with @Identifier")));

    }


    public Map<String, Object> setupPojo() {
        evaluateHasIdField(clazz);

        try {
            List<ObjectEvaluator<T>> evaluators = restModel.getObjectEvaluators();

            final Crud<T> dao = elepy.getCrudFor(clazz);

            //POST
            elepy.addRouting(anElepyRoute()
                    .accessLevel(restModel.getCreateAccessLevel())
                    .path(baseSlug + restModel.getSlug())
                    .method(HttpMethod.POST)
                    .route(ctx -> restModel.getService().handleCreate(ctx.request(), ctx.response(), dao, elepy.getContext(), evaluators, clazz))
                    .build()
            );

            // PUT
            elepy.addRouting(anElepyRoute()
                    .accessLevel(restModel.getUpdateAccessLevel())
                    .path(baseSlug + restModel.getSlug() + "/:id")
                    .method(HttpMethod.PUT)
                    .route(ctx -> restModel.getService().handleUpdate(ctx.request(), ctx.response(), dao, elepy.getContext(), evaluators, clazz))
                    .build()
            );

            //PATCH
            elepy.addRouting(anElepyRoute()
                    .accessLevel(restModel.getUpdateAccessLevel())
                    .path(baseSlug + restModel.getSlug() + "/:id")
                    .method(HttpMethod.PATCH)
                    .route(ctx -> restModel.getService().handleUpdate(ctx.request(), ctx.response(), dao, elepy.getContext(), evaluators, clazz))
                    .build()
            );

            // DELETE
            elepy.addRouting(anElepyRoute()
                    .accessLevel(restModel.getDeleteAccessLevel())
                    .path(baseSlug + restModel.getSlug() + "/:id")
                    .method(HttpMethod.DELETE)
                    .route(ctx -> restModel.getService().handleDelete(ctx.request(), ctx.response(), dao, elepy.getContext(), evaluators, clazz))
                    .build()
            );

            //GET PAGE
            elepy.addRouting(anElepyRoute()
                    .accessLevel(restModel.getFindAccessLevel())
                    .path(baseSlug + restModel.getSlug())
                    .method(HttpMethod.GET)
                    .route(ctx -> restModel.getService().handleFind(ctx.request(), ctx.response(), dao, elepy.getContext(), evaluators, clazz))
                    .build()
            );

            //GET ONE
            elepy.addRouting(anElepyRoute()
                    .accessLevel(restModel.getFindAccessLevel())
                    .path(baseSlug + restModel.getSlug() + "/:id")
                    .method(HttpMethod.GET)
                    .route(ctx -> restModel.getService().handleFind(ctx.request(), ctx.response(), dao, elepy.getContext(), evaluators, clazz))
                    .build()
            );

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return getPojoDescriptor(restModel, clazz);
    }

    private Map<String, Object> getPojoDescriptor(ResourceDescriber restModel, Class<?> clazz) {
        Map<String, Object> model = new HashMap<>();
        if (baseSlug.equals("/")) {
            model.put("slug", restModel.getSlug());

        } else {
            model.put("slug", baseSlug + restModel.getSlug());
        }
        model.put("name", restModel.getName());

        model.put("javaClass", clazz.getName());

        model.put("actions", getActions(restModel));
        model.put("idField", evaluateHasIdField(clazz));
        model.put("fields", new StructureDescriber(clazz).getStructure());
        return model;
    }

    private Map<String, AccessLevel> getActions(ResourceDescriber restModel) {
        Map<String, AccessLevel> actions = new HashMap<>();
        actions.put("findOne", restModel.getFindAccessLevel());
        actions.put("findAll", restModel.getFindAccessLevel());
        actions.put("update", restModel.getUpdateAccessLevel());
        actions.put("DELETE", restModel.getDeleteAccessLevel());
        actions.put("create", restModel.getCreateAccessLevel());
        return actions;
    }
}
