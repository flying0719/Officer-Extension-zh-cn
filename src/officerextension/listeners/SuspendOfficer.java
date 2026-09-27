package officerextension.listeners;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.util.Misc;
import officerextension.Settings;
import officerextension.UtilReflection.ConfirmDialogData;
import officerextension.UtilReflection;
import officerextension.ui.Button;
import officerextension.ui.OfficerUIElement;

public class SuspendOfficer extends ActionListener {

    private final OfficerUIElement uiElement;

    public SuspendOfficer(OfficerUIElement uiElement) {
        this.uiElement = uiElement;
    }

    @Override
    public void trigger(Object... args) {
        ConfirmSuspendOfficer confirmListener = new ConfirmSuspendOfficer(uiElement);
        PersonAPI officer = uiElement.getOfficerData().getPerson();
        String name = officer.getNameString();
        int level = officer.getStats().getLevel();
        String suspendCost = Misc.getDGSCredits(Settings.SUSPEND_OFFICER_COST_MULTIPLIER * Misc.getOfficerSalary(officer));
        String salaryPercent = (int) (100f * Settings.SUSPENDED_SALARY_FRACTION) + "%";
        String str = String.format("确定要停用 %s (等级 %s)?" +
                "\n停职军官仅需支付正常薪酬的 %s。" +
                "\n停用该军官需要支付 %s 补偿。" +
                "停职军官随时可免费复职。",
                name,
                level,
                salaryPercent,
                suspendCost);
        boolean canAfford = Global.getSector().getPlayerFleet().getCargo().getCredits().get()
                >= Settings.SUSPEND_OFFICER_COST_MULTIPLIER * Misc.getOfficerSalary(officer);
        ConfirmDialogData data = UtilReflection.showConfirmationDialog(
                str,
                "停职",
                "取消",
                650f,
                230f,
                confirmListener);
        if (data == null) {
            return;
        }
        LabelAPI label = data.textLabel;
        label.setHighlight(salaryPercent, suspendCost);
        label.setHighlightColors(
                Misc.getHighlightColor(),
                canAfford ? Misc.getHighlightColor() : Misc.getNegativeHighlightColor());
        Button yesButton = data.confirmButton;
        if (!canAfford) {
            yesButton.setEnabled(false);
        }
    }
}
