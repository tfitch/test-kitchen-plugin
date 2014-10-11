/*
 * The MIT License
 *
 * Copyright (c) 2012, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ci.kitchen.jenkins;

import hudson.ExtensionPoint;
import ci.kitchen.jenkins.KitchenBuild.KitchenBuildExecution;
import hudson.model.AbstractDescribableImpl;
import hudson.model.BuildListener;
import hudson.model.Result;

import java.io.IOException;
import java.util.List;

/**
 * Controls the execution sequence of {@link KitchenConfiguration} when {@link KitchenProject} builds,
 * including what degree it gets serialized/parallelled, how the whole build is abandoned when
 * some fails, etc.
 *
 * @author Kohsuke Kawaguchi
 * @since 1.456
 */
public abstract class KitchenExecutionStrategy extends AbstractDescribableImpl<KitchenExecutionStrategy> implements ExtensionPoint {
    public Result run(KitchenBuildExecution execution) throws InterruptedException, IOException {
        return run(execution.getBuild(), execution.getAggregators(), execution.getListener());
    }

    /**
     * @deprecated
     *      Override {@link #run(KitchenBuild.KitchenBuildExecution)}
     */
    public Result run(KitchenBuild build, List<KitchenAggregator> aggregators, BuildListener listener) throws InterruptedException, IOException {
        throw new UnsupportedOperationException(getClass()+" needs to override run(KitchenBuildExecution)");
    }

    @Override
    public KitchenExecutionStrategyDescriptor getDescriptor() {
        return (KitchenExecutionStrategyDescriptor)super.getDescriptor();
    }
}