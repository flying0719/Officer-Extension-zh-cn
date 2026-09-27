package officerextension.listeners;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.util.Misc;
import officerextension.Settings;
import officerextension.UtilReflection;
import officerextension.ui.Button;
import officerextension.ui.OfficerUIElement;
import officerextension.ui.SkillButton;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ForgetSkills extends ActionListener {

    private final OfficerUIElement uiElement;

    public ForgetSkills(OfficerUIElement uiElement) {
        this.uiElement = uiElement;
    }

    @Override
    public void trigger(Object... args) {
        PersonAPI officerPerson = uiElement.getOfficerData().getPerson();
        int numForgetting = 0;
        for (SkillButton button : uiElement.getWrappedSkillButtons()) {
            if (button.isSelected()) {
                numForgetting++;
            }
        }
        StringBuilder confirmSB = new StringBuilder();
        List<String> highlights = new ArrayList<>();
        List<Color> colors = new ArrayList<>();
        confirmSB.append("确定要将 ")
                .append(officerPerson.getNameString())
                .append(" (等级 ")
                .append(officerPerson.getStats().getLevel())
                .append(") 降级至等级 ")
                .append(officerPerson.getStats().getLevel() - numForgetting)
                .append(" 并遗忘以下技能：\n");
        for (SkillButton button : uiElement.getWrappedSkillButtons()) {
            if (button.isSelected()) {
                SkillSpecAPI spec = button.getSkillSpec();
                confirmSB.append("        - ")
                        .append(spec.getName());
                highlights.add(spec.getName());
                colors.add(spec.getGoverningAptitudeColor());
                if (officerPerson.getStats().getSkillLevel(button.getSkillSpec().getId()) > 1) {
                    confirmSB.append(" (精英)");
                    highlights.add("(精英)");
                    colors.add(Misc.getStoryOptionColor());
                }
                confirmSB.append("\n");
            }
        }
        String bonusXPPercent = (int) (100f * Settings.DEMOTE_BONUS_XP_FRACTION) + "%";
        int numStoryPoints = Global.getSector().getPlayerStats().getStoryPoints();
        int costStoryPoints = Settings.DEMOTE_OFFICER_SP_COST;
        confirmSB.append("军官降级需要 ")
                .append(costStoryPoints)
                .append(" 个故事点，且给予 ")
                .append(bonusXPPercent)
                .append(" 额外经验。\n")
                .append("你目前拥有 ")
                .append(numStoryPoints)
                .append(" 个故事点。");
        highlights.add(costStoryPoints + " 个故事点");
        colors.add(Misc.getStoryOptionColor());
        highlights.add(bonusXPPercent);
        colors.add(Misc.getStoryOptionColor());
        highlights.add("" + numStoryPoints);
        colors.add(numStoryPoints >= Settings.DEMOTE_OFFICER_SP_COST ? Misc.getStoryOptionColor() : Misc.getNegativeHighlightColor());
        ConfirmForgetSkills confirmListener = new ConfirmForgetSkills(uiElement);
        UtilReflection.ConfirmDialogData data = UtilReflection.showConfirmationDialog(
                confirmSB.toString(),
                "解雇",
                "取消",
                650f,
                250f + 20f * numForgetting,
                confirmListener);
        if (data == null) {
            return;
        }
        LabelAPI label = data.textLabel;
        label.setHighlight(highlights.toArray(new String[0]));
        label.setHighlightColors(colors.toArray(new Color[0]));
        Button yesButton = data.confirmButton;
        if (numStoryPoints < Settings.DEMOTE_OFFICER_SP_COST) {
            yesButton.setEnabled(false);
        }
    }
}
