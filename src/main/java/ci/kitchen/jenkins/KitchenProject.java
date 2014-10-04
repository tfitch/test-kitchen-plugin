/*
 * The MIT License
 *
 * Copyright (c) 2013, CloudBees, Inc.
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

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.model.Project;
import hudson.model.Queue;
import hudson.model.SCMedItem;
import hudson.model.TopLevelItem;
import hudson.model.TopLevelItemDescriptor;
import hudson.security.Permission;
import jenkins.branch.Branch;
import jenkins.model.Jenkins;

/**
 * Represents a specific instance of a Test Kitchen build project.
 *
 * @author Tyler Fitch
 */
public class KitchenProject extends Project<KitchenProject, KitchenBuild>
        implements TopLevelItem, SCMedItem,
        Queue.FlyweightTask {
//        ItemGroup<KitchenEnvironmentProject>, 

    /**
     * Hack to prevent the Configure link showing up in the sidebar.
     */
    public static final Permission CONFIGURE = null;

    /**
     * The branch that we are tracking.
     */
    @NonNull
    private Branch branch;

    /**
     * Constructor.
     *
     * @param parent the parent.
     * @param branch the branch.
     */
    public KitchenProject(@NonNull KitchenMultibranchProject parent, @NonNull Branch branch) {
        super(parent, branch.getName());
        this.branch = branch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public KitchenMultibranchProject getParent() {
        return (KitchenMultibranchProject) super.getParent();
    }

    /**
     * Returns the branch.
     *
     * @return the branch.
     */
    public synchronized Branch getBranch() {
        return branch;
    }

    /**
     * Sets the branch.
     *
     * @param branch the branch.
     */
    public synchronized void setBranch(@NonNull Branch branch) {
        branch.getClass();
        this.branch = branch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    protected Class<KitchenBuild> getBuildClass() {
        return KitchenBuild.class;
    }

    /**
     * {@inheritDoc}
     */
    // TODO - Hack - child items of an item group that is a view container must to implement TopLevelItem
    public TopLevelItemDescriptor getDescriptor() {
        return (TopLevelItemDescriptor) Jenkins.getInstance().getDescriptorOrDie(KitchenProject.class);
    }
}

