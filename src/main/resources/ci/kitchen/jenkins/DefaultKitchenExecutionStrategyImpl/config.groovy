package ci.kitchen.jenkins.DefaultKitchenExecutionStrategyImpl;

import ci.kitchen.jenkins.KitchenConfigurationSorterDescriptor
import hudson.model.Result;

def f = namespace(lib.FormTagLib)

f.optionalBlock (field:"runSequentially", title:_("Run each configuration sequentially"), inline:true) {
    if (KitchenConfigurationSorterDescriptor.all().size()>1) {
        f.dropdownDescriptorSelector(title:_("Execution order of builds"), field:"sorter")
    }
}

f.optionalBlock (field:"hasTouchStoneCombinationFilter", title:_("Execute touchstone builds first"), inline:true) {
    // TODO: help="/help/kitchen/touchstone.html">
    // TODO: move l10n from KitchenProject/configEntries.jelly

    f.entry(title:_("Filter"), field:"touchStoneCombinationFilter") {
        f.textbox()
    }

    f.entry(title:_("Required result"), field:"touchStoneResultCondition", description:_("required.result.description")) {
        select(name:"touchStoneResultCondition") {
            f.option(value:"SUCCESS",  selected:my.touchStoneResultCondition==Result.SUCCESS,  _("Stable"))
            f.option(value:"UNSTABLE", selected:my.touchStoneResultCondition==Result.UNSTABLE, _("Unstable"))
        }
    }
}
