package ci.kitchen.jenkins;

import hudson.ExtensionPoint;
import hudson.model.AbstractDescribableImpl;
import hudson.util.FormValidation;

import java.util.Comparator;

/**
 * Add sorting for configurations {@link KitchenConfiguration}s of matrix job {@link KitchenProject}
 *
 * @since 1.439
 * @author Lucie Votypkova
 * @see KitchenConfigurationSorterDescriptor
 */
public abstract class KitchenConfigurationSorter extends AbstractDescribableImpl<KitchenConfigurationSorter> implements ExtensionPoint, Comparator<KitchenConfiguration> {

    /**
     * Checks if this sorter is properly configured and applicable for the given project.
     *
     * <p>
     * This method is invoked when the configuration is submitted to ensure that the sorter is compatible
     * with the current project configuration (most probably with its {@link Axis}.)
     *
     * @param p
     *      Project for which this sorter is being used for.
     * @throws FormValidation
     *      If you need to report an error to the user and reject the form submission.
     */
    public abstract void validate(KitchenProject p) throws FormValidation;

    @Override
    public KitchenConfigurationSorterDescriptor getDescriptor() {
        return (KitchenConfigurationSorterDescriptor)super.getDescriptor();
    }
}