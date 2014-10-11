/*
 * The MIT License
 *
 * Copyright (c) 2014, Tyler Fitch
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
import hudson.Extension;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.model.Items;
import hudson.model.TopLevelItem;
import hudson.scm.NullSCM;
import hudson.scm.SCM;
import hudson.scm.SCMDescriptor;

import jenkins.branch.Branch;
import jenkins.branch.BranchProjectFactory;
import jenkins.branch.BranchProjectFactoryDescriptor;
import jenkins.branch.MultiBranchProject;
import jenkins.branch.MultiBranchProjectDescriptor;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A multiple branch Test Kitchen build project type.
 *
 * @author Tyler Fitch
 */
public class KitchenMultibranchProject extends
		MultiBranchProject<KitchenProject, KitchenBuild> {

    /**
     * The default marker file.
     */
    private static final String DEFAULT_MARKER_FILE = "cloudbees";

    /**
     * Constructor.
     *
     * @param parent the parent container.
     * @param name   our name.
     */
    public KitchenMultibranchProject(ItemGroup parent, String name) {
        super(parent, name);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    protected BranchProjectFactory<KitchenProject, KitchenBuild> newProjectFactory() {
        return new ProjectFactoryImpl();
    }

    /**
     * Our descriptor
     */
    @Extension
    public static class DescriptorImpl extends MultiBranchProjectDescriptor {

        /**
         * The global default marker file.
         */
        private String markerFile;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDisplayName() {
            return Messages.KitchenMultibranchProject_DisplayName();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TopLevelItem newInstance(ItemGroup parent, String name) {
            return new KitchenMultibranchProject(parent, name);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @NonNull
        public List<SCMDescriptor<?>> getSCMDescriptors() {
            List<SCMDescriptor<?>> result = new ArrayList<SCMDescriptor<?>>(SCM.all());
            for (Iterator<SCMDescriptor<?>> iterator = result.iterator(); iterator.hasNext(); ) {
                SCMDescriptor<?> d = iterator.next();
                if (NullSCM.class.equals(d.clazz)) {
                    iterator.remove();
                }
            }
            return result; // todo figure out filtering
        }

        /**
         * Returns the type of project factory supported by this project type.
         *
         * @return the type of project factory supported by this project type.
         */
        @SuppressWarnings("unused") // stapler
        public BranchProjectFactoryDescriptor getProjectFactoryDescriptor() {
            return Jenkins.getInstance().getDescriptorByType(ProjectFactoryImpl.DescriptorImpl.class);
        }

        /**
         * Returns the global default marker file.
         *
         * @return the global default marker file.
         */
        public String getMarkerFile() {
            return StringUtils.isBlank(markerFile) ? DEFAULT_MARKER_FILE : markerFile;
        }

        /**
         * Sets the global default marker file.
         *
         * @param markerFile the global default marker file.
         */
        public void setMarkerFile(String markerFile) {
            this.markerFile = StringUtils.isBlank(markerFile) ? DEFAULT_MARKER_FILE : markerFile.trim();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            JSONObject specifyMarkerFile = json.optJSONObject("specifyMarkerFile");
            if (specifyMarkerFile == null) {
                setMarkerFile(null);
            } else {
                setMarkerFile(specifyMarkerFile.optString("markerFile", ""));
            }
            return true;
        }
    }

    /**
     * Our {@link BranchProjectFactory}.
     */
    public static class ProjectFactoryImpl extends BranchProjectFactory<KitchenProject, KitchenBuild> {

        /**
         * Constructor.
         */
        @DataBoundConstructor
        public ProjectFactoryImpl() {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public KitchenProject newInstance(final Branch branch) {
            return new KitchenProject((KitchenMultibranchProject) getOwner(), branch);
        }

        /**
         * {@inheritDoc}
         */
        @NonNull
        @Override
        public Branch getBranch(@NonNull KitchenProject project) {
            return project.getBranch();
        }

        /**
         * {@inheritDoc}
         */
        @NonNull
        @Override
        public KitchenProject setBranch(@NonNull KitchenProject project, @NonNull Branch branch) {
            if (!project.getBranch().equals(branch)) {
                project.setBranch(branch);
                try {
                    project.save();
                } catch (IOException e) {
                    // TODO log
                }
            } else {
                project.setBranch(branch);
            }
            return project;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isProject(Item item) {
            return item instanceof KitchenProject;
        }

        /**
         * Our descriptor.
         */
        @Extension
        @SuppressWarnings("unused")// instantiated by Jenkins
        public static class DescriptorImpl extends BranchProjectFactoryDescriptor {

            /**
             * {@inheritDoc}
             */
            @Override
            public String getDisplayName() {
                return "Fixed configuration";
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public boolean isApplicable(Class<? extends MultiBranchProject> clazz) {
                return KitchenMultibranchProject.class.isAssignableFrom(clazz);
            }
        }
    }

    /**
     * Give us a nice XML alias
     */
    @Initializer(before = InitMilestone.PLUGINS_STARTED)
    @SuppressWarnings("unused") // called by Jenkins
    public static void registerXStream() {
        Items.XSTREAM.alias("kitchen-multibranch", KitchenMultibranchProject.class);
    }

}