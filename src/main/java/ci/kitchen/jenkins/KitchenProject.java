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

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.ItemGroup;
import hudson.model.Project;
import hudson.model.TopLevelItem;
import hudson.model.TopLevelItemDescriptor;
import jenkins.model.Jenkins;

/**
 * Represents a specific instance of a Test Kitchen build project.
 *
 * @author Tyler Fitch
 */
public class KitchenProject extends Project<KitchenProject, KitchenBuild> implements TopLevelItem {

    /**
     * Constructor that specifies the {@link ItemGroup} for this project and the
     * project name.
     *
     * @param parent - the project's parent {@link ItemGroup}
     * @param name   - the project's name
     */
    public KitchenProject(ItemGroup parent, String name) {
        super(parent, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TopLevelItemDescriptor getDescriptor() {
        return (DescriptorImpl) Jenkins.getInstance().getDescriptorOrDie(
                KitchenProject.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<KitchenBuild> getBuildClass() {
        return KitchenBuild.class;
    }

    /**
     * Our project's descriptor.
     */
    @Extension
    public static class DescriptorImpl extends AbstractProjectDescriptor {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getDisplayName() {
            return Messages.KitchenProject_DisplayName();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TopLevelItem newInstance(ItemGroup parent, String name) {
            return new KitchenProject(parent, name);
        }
    }

}

