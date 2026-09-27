package officerextension.listeners;

import officerextension.UtilReflection;
import officerextension.ui.OfficerUIElement;

public class DismissOfficer extends ActionListener {

    private final OfficerUIElement uiElement;

    public DismissOfficer(OfficerUIElement uiElement) {
        this.uiElement = uiElement;
    }

    @Override
    public void trigger(Object... args) {
        ConfirmDismissOfficer confirmListener = new ConfirmDismissOfficer(uiElement);
        String name = uiElement.getOfficerData().getPerson().getNameString();
        int level = uiElement.getOfficerData().getPerson().getStats().getLevel();
        String str = "确定要解雇 " + name + " (等级 " + level + ")?\n这一决定不可撤销。";
        UtilReflection.showConfirmationDialog(str, "解雇", "取消", 650f, 160f, confirmListener);
    }
}
