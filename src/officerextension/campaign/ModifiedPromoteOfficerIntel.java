package officerextension.campaign;

import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.intel.PromoteOfficerIntel;
import com.fs.starfarer.api.ui.ButtonAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.input.Keyboard;

import java.awt.Color;

public class ModifiedPromoteOfficerIntel extends PromoteOfficerIntel {
    public ModifiedPromoteOfficerIntel(PersonAPI person, TextPanelAPI text) {
        super(text);
        this.person = person;
    }

    @Override
    public void createSmallDescription(TooltipMakerAPI info, float width, float height) {
        Color h = Misc.getHighlightColor();
        Color tc = Misc.getTextColor();
        float opad = 10f;

        info.addImage(person.getPortraitSprite(), width, 128, opad);

        info.addPara(getDescText(), tc, opad);

        addBulletPoints(info, ListInfoMode.IN_DESC);
        info.addPara(person.getPersonalityAPI().getDescription(), opad);

        float days = DURATION - getDaysSincePlayerVisible();
        info.addPara("该机会仍将保留 %s 天。", opad, tc, h, getDays(days));

        Color color = Misc.getStoryOptionColor();
        Color dark = Misc.getStoryDarkColor();

        ButtonAPI button = addGenericButton(info, width, color, dark, "晋升为舰船指挥官", BUTTON_PROMOTE);
        button.setShortcut(Keyboard.KEY_T, true);

        info.addSpacer(-10f);
        addDeleteButton(info, width, "忽略");
    }
}
