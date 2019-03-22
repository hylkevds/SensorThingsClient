package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import de.fraunhofer.iosb.ilt.sta.model.Task;

public abstract class AbstractTaskBuilder<U extends AbstractTaskBuilder<U>> extends EntityBuilder<Task, U>  {

    /**
     * Create the new instance that will be build by this
     * {@link AbstractBuilder}
     *
     * @return the new instance that will be build by this
     * {@link AbstractBuilder}
     */
    @Override
    protected Task newBuildingInstance() {
        return new Task();
    }


}
