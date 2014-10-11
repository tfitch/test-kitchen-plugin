package ci.kitchen.jenkins;

import hudson.DescriptorExtensionList;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;

/**
 * Descriptor for {@link KitchenConfigurationSorter}.
 *
 * @author Kohsuke Kawaguchi
 * @since 1.439
 */
public abstract class KitchenConfigurationSorterDescriptor extends Descriptor<KitchenConfigurationSorter> {
    protected KitchenConfigurationSorterDescriptor(Class<? extends KitchenConfigurationSorter> clazz) {
        super(clazz);
    }

    protected KitchenConfigurationSorterDescriptor() {
    }

    /**
     * Returns all the registered {@link KitchenConfigurationSorterDescriptor}s.
     */
    public static DescriptorExtensionList<KitchenConfigurationSorter,KitchenConfigurationSorterDescriptor> all() {
        return Jenkins.getInstance().<KitchenConfigurationSorter,KitchenConfigurationSorterDescriptor>getDescriptorList(KitchenConfigurationSorter.class);
    }
}